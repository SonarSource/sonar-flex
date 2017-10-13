/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2017 SonarSource SA
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

import com.google.common.collect.Maps;
import com.sonar.sslr.api.AstNode;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Expression;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.Map;

@Rule(
  key = "S1950",
  name = "Cases in a \"switch\" should not have the same condition",
  priority = Priority.CRITICAL)
@ActivatedByDefault
@SqaleConstantRemediation("15min")
public class DuplicateSwitchCaseConditionCheck extends SquidCheck<LexerlessGrammar> {

  private Map<String, AstNode> casesByCondition = Maps.newHashMap();

  @Override
  public void init() {
    subscribeTo(FlexGrammar.SWITCH_STATEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {

    for (AstNode caseElement : astNode.getChildren(FlexGrammar.CASE_ELEMENT)) {
      for (AstNode caseLabel : caseElement.getChildren(FlexGrammar.CASE_LABEL)) {

        if (!caseLabel.getFirstChild().is(FlexKeyword.DEFAULT)) {
          checkCondition(caseLabel);
        }
      }
    }
    casesByCondition.clear();
  }

  private void checkCondition(AstNode caseLabel) {
    String expression = Expression.exprToString(caseLabel.getFirstChild(FlexGrammar.LIST_EXPRESSION));
    AstNode duplicateCase = casesByCondition.get(expression);

    if (duplicateCase != null) {
      getContext().createLineViolation(this, "This case duplicates the case on line {0} with condition \"{1}\".", caseLabel,
        duplicateCase.getTokenLine(), expression);
    } else {
      casesByCondition.put(expression, caseLabel);
    }
  }

}
