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

import com.google.common.collect.Iterables;
import com.sonar.sslr.api.AstNode;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S1151",
  name = "\"switch case\" clauses should not have too many lines",
  tags = Tags.BRAIN_OVERLOAD,
  priority = Priority.MAJOR)
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("5min")
public class TooManyLinesInCaseCheck extends SquidCheck<LexerlessGrammar> {

  private static final int DEFAULT = 5;

  @RuleProperty(
    key = "max",
    description = "Maximum number of lines",
    defaultValue = "" + DEFAULT)
  int max = DEFAULT;


  @Override
  public void init() {
    subscribeTo(FlexGrammar.CASE_ELEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    AstNode lastLabelNode = Iterables.getLast(astNode.getChildren(FlexGrammar.CASE_LABEL));
    int lines = Math.max(astNode.getNextAstNode().getTokenLine() - lastLabelNode.getTokenLine(), 1);
    if (lines > max) {
      getContext().createLineViolation(this, "Reduce this switch case number of lines from {0} to at most {1}, for example by extracting code into methods.",
        lastLabelNode, lines, max);
    }
  }

}
