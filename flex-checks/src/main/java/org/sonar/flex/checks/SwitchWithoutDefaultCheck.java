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

import com.sonar.sslr.api.AstNode;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.FlexPunctuator;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "SwitchWithoutDefault",
  name = "\"switch\" statements should end with a \"default\" clause",
  priority = Priority.MAJOR,
  tags = {Tags.CWE, Tags.CERT, Tags.MISRA})
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LOGIC_RELIABILITY)
@SqaleConstantRemediation("5min")
public class SwitchWithoutDefaultCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.SWITCH_STATEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    AstNode defaultCaseElement = null;
    for (AstNode caseElementNode : astNode.getChildren(FlexGrammar.CASE_ELEMENT)) {
      for (AstNode caseLabelNode : caseElementNode.getChildren(FlexGrammar.CASE_LABEL)) {
        if (caseLabelNode.getFirstChild().is(FlexKeyword.DEFAULT)) {
          defaultCaseElement = caseElementNode;
          break;
        }
      }
    }

    if (defaultCaseElement == null) {
      getContext().createLineViolation(this, "Avoid switch statement without a \"default\" clause.", astNode);
    } else if (defaultCaseElement.getNextSibling().isNot(FlexPunctuator.RCURLYBRACE)) {
      getContext().createLineViolation(this, "\"default\" clause should be the last one.", astNode);
    }
  }

}
