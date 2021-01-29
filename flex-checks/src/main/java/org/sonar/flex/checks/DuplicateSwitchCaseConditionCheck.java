/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2021 SonarSource SA
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
import com.sonar.sslr.api.AstNodeType;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Expression;

@Rule(key = "S1950")
public class DuplicateSwitchCaseConditionCheck extends FlexCheck {

  private Map<String, AstNode> casesByCondition = Maps.newHashMap();

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.SWITCH_STATEMENT);
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
      addIssue(MessageFormat.format("This case duplicates the case on line {0} with condition \"{1}\".", duplicateCase.getTokenLine(), expression), caseLabel);
    } else {
      casesByCondition.put(expression, caseLabel);
    }
  }

}
