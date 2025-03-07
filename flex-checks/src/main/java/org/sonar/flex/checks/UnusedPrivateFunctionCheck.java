/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Function;
import org.sonar.flex.checks.utils.Modifiers;

@Rule(key = "S1144")
public class UnusedPrivateFunctionCheck extends FlexCheck {

  static class PrivateFunction {
    final AstNode declaration;
    int usages;

    public PrivateFunction(AstNode declaration, int usages) {
      this.declaration = declaration;
      this.usages = usages;
    }
  }

  private static class ClassState {
    private final Map<String, PrivateFunction> functions;

    public ClassState(AstNode classDef) {
      this.functions = new HashMap<>();
      retrieveAllPrivateFunctions(classDef);
    }

    private void retrieveAllPrivateFunctions(AstNode classDef) {
      for (AstNode functionDef : Clazz.getFunctions(classDef)) {

        if (!Function.isAccessor(functionDef)
          && Modifiers.getModifiers(functionDef.getPreviousAstNode()).contains(FlexKeyword.PRIVATE)
          && !Function.isEmptyConstructor(functionDef, Clazz.getName(classDef))) {
          AstNode identifierNode = functionDef
            .getFirstChild(FlexGrammar.FUNCTION_NAME)
            .getFirstChild(FlexGrammar.IDENTIFIER);

          functions.put(identifierNode.getTokenValue(), new PrivateFunction(identifierNode, 0));
        }
      }

    }

    private void use(AstNode astNode) {
      PrivateFunction function = functions.get(astNode.getTokenValue());

      if (function != null) {
        function.usages++;
      }
    }
  }

  private Deque<ClassState> classStack = new ArrayDeque<>();
  private boolean inClass;

  @Override
  public List<AstNodeType> subscribedTo() {
    return Arrays.asList(
      FlexGrammar.CLASS_DEF,
      FlexGrammar.FUNCTION_DEF,
      FlexGrammar.QUALIFIED_IDENTIFIER);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    classStack.clear();
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.CLASS_DEF)) {
      inClass = true;
      classStack.push(new ClassState(astNode));
    } else if (inClass && astNode.is(FlexGrammar.QUALIFIED_IDENTIFIER)) {
      classStack.peek().use(astNode);
    }
  }


  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.CLASS_DEF)) {
      reportUnusedPrivateFunction();
      classStack.pop();
      inClass = !classStack.isEmpty();
    }
  }

  private void reportUnusedPrivateFunction() {
    for (Map.Entry<String, PrivateFunction> entry : classStack.peek().functions.entrySet()) {
      if (entry.getValue().usages == 0) {
        addIssue("Remove the declaration of the unused '" + entry.getKey() + "' function.", entry.getValue().declaration);
      }
    }
  }
}
