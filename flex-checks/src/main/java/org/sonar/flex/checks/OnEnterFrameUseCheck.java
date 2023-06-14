/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2023 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.util.Collections;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Expression;

@Rule(key = "S1982")
public class OnEnterFrameUseCheck extends FlexCheck {

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.ASSIGNMENT_EXPR);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.getNumberOfChildren() > 1 && isOnEnterFrame(astNode.getFirstChild()) && isFunctionExpr(astNode.getLastChild())) {
      addIssue("Refactor this code to remove the use of \"onEnterFrame\" event handler.", astNode);
    }
  }

  private static boolean isFunctionExpr(AstNode assignementExpr) {
    AstNode assignmentExprChild = assignementExpr.getFirstChild();
    return assignmentExprChild.is(FlexGrammar.POSTFIX_EXPR) && assignmentExprChild.getFirstChild().getFirstChild().is(FlexGrammar.FUNCTION_EXPR);
  }

  private static boolean isOnEnterFrame(AstNode postfixExpr) {
    return Expression.exprToString(postfixExpr).endsWith(".onEnterFrame");
  }

}
