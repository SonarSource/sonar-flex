/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.squid.checks.SquidCheck;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S1462",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class HardcodedEventNameCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.POSTFIX_EXPR);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (isAddEventFunctionCall(astNode.getFirstChild(FlexGrammar.PRIMARY_EXPR))) {
      AstNode eventName = getEventName(astNode.getFirstChild(FlexGrammar.ARGUMENTS));

      if (eventName != null && eventName.is(FlexGrammar.STRING)) {
        getContext().createLineViolation(this, "The event name {0} should be defined in a constant variable.", astNode, eventName.getTokenValue());
      }
    }
  }

  private static boolean isAddEventFunctionCall(AstNode primaryExpr) {
    return primaryExpr != null && primaryExpr.getTokenValue().equals("addEventListener");
  }

  private static AstNode getEventName(AstNode argumentsNode) {
    if (argumentsNode != null && argumentsNode.getFirstChild(FlexGrammar.LIST_EXPRESSION) != null) {
      AstNode postfixExpr = argumentsNode
        .getFirstChild(FlexGrammar.LIST_EXPRESSION)
        .getFirstChild(FlexGrammar.ASSIGNMENT_EXPR)
        .getFirstChild(FlexGrammar.POSTFIX_EXPR);

      if (postfixExpr.getFirstChild(FlexGrammar.PRIMARY_EXPR) != null) {
        return postfixExpr.getFirstChild(FlexGrammar.PRIMARY_EXPR).getFirstChild();
      }
    }
    return null;
  }

}
