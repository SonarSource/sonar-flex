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
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Function;

@Rule(key = "S1447")
public class ConstructorNotLightweightCheck extends FlexCheck {

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    AstNode constructorDef = Clazz.getConstructor(astNode);

    if (constructorDef != null && containsBranch(constructorDef)) {
      addIssue(
        MessageFormat.format("Extract the content of this \"{0}\" constructor into a dedicated function", Function.getName(constructorDef)),
        constructorDef);
    }
  }

  private static boolean containsBranch(AstNode constructorDef) {
    AstNode blockNode = constructorDef.getFirstChild(FlexGrammar.FUNCTION_COMMON).getFirstChild(FlexGrammar.BLOCK);

    if (blockNode != null) {

      for (AstNode directive : blockNode.getFirstChild(FlexGrammar.DIRECTIVES).getChildren()) {
        if (isBranch(directive)) {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean isBranch(AstNode directive) {
    AstNode astNode = directive.getFirstChild();
    return astNode.is(FlexGrammar.STATEMENT)
      && astNode.getFirstChild().is(
      FlexGrammar.IF_STATEMENT,
      FlexGrammar.SWITCH_STATEMENT,
      FlexGrammar.DO_STATEMENT,
      FlexGrammar.WHILE_STATEMENT,
      FlexGrammar.FOR_STATEMENT);
  }

}
