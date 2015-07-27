/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * sonarqube@googlegroups.com
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

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNode;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.Set;

@Rule(
  key = "ActionScript2",
  priority = Priority.BLOCKER)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.BLOCKER)
public class ActionScript2Check extends SquidCheck<LexerlessGrammar> {

  private final Set<String> deprecatedOperators = ImmutableSet.of("or", "and", "ne", "eq", "ge", "gt", "le", "lt", "add", "<>");

  @Override
  public void init() {
    subscribeTo(
//        grammar.setVariableStatement,
      FlexGrammar.UNARY_EXPR,
//        grammar.modifier,
      FlexGrammar.LOGICAL_OR_OPERATOR,
      FlexGrammar.LOGICAL_AND_OPERATOR,
      FlexGrammar.EQUALITY_OPERATOR,
      FlexGrammar.RELATIONAL_OPERATOR,
      FlexGrammar.RELATIONAL_OPERATOR_NO_IN,
      FlexGrammar.ADDITIVE_OPERATOR
    );
  }

  @Override
  public void visitNode(AstNode astNode) {
//    if (astNode.is(grammar.setVariableStatement)) {
//      getContext().createLineViolation(this, "'set variable statement' not available in ActionScript 3.0", astNode);
//    } else if (astNode.is(grammar.modifier) && "intrinsic".equals(astNode.getTokenValue())) {
//      getContext().createLineViolation(this, "'intrinsic' not available in ActionScript 3.0", astNode);
//    } else
    if (astNode.is(FlexGrammar.UNARY_EXPR) && "not".equals(astNode.getFirstChild().getTokenValue())) {
      getContext().createLineViolation(this, "Operator 'not' not available in ActionScript 3.0", astNode.getFirstChild());
    } else {
      String operator = getValue(astNode);
      if (deprecatedOperators.contains(operator)) {
        getContext().createLineViolation(this, "Operator '" + operator + "' not available in ActionScript 3.0", astNode);
      }
    }
  }

  private String getValue(AstNode astNode) {
    StringBuilder sb = new StringBuilder();
    for (AstNode child : astNode.getChildren()) {
      sb.append(child.getTokenValue());
    }
    return sb.toString();
  }

}
