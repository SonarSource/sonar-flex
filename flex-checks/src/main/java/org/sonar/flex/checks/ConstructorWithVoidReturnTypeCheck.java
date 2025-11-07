/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
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
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Clazz;

@Rule(key = "S1445")
public class ConstructorWithVoidReturnTypeCheck extends FlexCheck {

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    AstNode constructorDef = Clazz.getConstructor(astNode);

    if (constructorDef != null && hasVoidReturnType(constructorDef)) {
      String className = astNode.getFirstChild(FlexGrammar.CLASS_NAME).getFirstChild(FlexGrammar.CLASS_IDENTIFIERS).getLastChild().getTokenValue();
      addIssue(MessageFormat.format("Remove the \"void\" return type from this \"{0}\" constructor", className), constructorDef);
    }
  }

  private static boolean hasVoidReturnType(AstNode functionDef) {
    AstNode resultTypeNode = functionDef.getFirstChild(FlexGrammar.FUNCTION_COMMON)
      .getFirstChild(FlexGrammar.FUNCTION_SIGNATURE)
      .getFirstChild(FlexGrammar.RESULT_TYPE);

    return resultTypeNode != null && resultTypeNode.getFirstChild(FlexKeyword.VOID) != null;
  }
}
