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
import java.util.Collections;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;

@Rule(key = "S1951")
public class TraceUseCheck extends FlexCheck {

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.POSTFIX_EXPR);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (isFunctionCall(astNode) && "trace".equals(astNode.getFirstChild().getTokenValue())) {
      addIssue("Remove this use of the \"trace\" function.", astNode);
    }
  }

  private static boolean isFunctionCall(AstNode postfixExpr) {
    return postfixExpr.getFirstChild().is(FlexGrammar.PRIMARY_EXPR) && postfixExpr.getLastChild().is(FlexGrammar.ARGUMENTS);
  }

}
