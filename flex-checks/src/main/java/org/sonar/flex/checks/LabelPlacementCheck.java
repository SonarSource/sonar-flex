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
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S1439",
  name = "Only while/do/for statements may be labelled",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LOGIC_RELIABILITY)
@SqaleConstantRemediation("20min")
public class LabelPlacementCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.LABELED_STATEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (!isIterationStatement(astNode.getFirstChild(FlexGrammar.SUB_STATEMENT))) {
      getContext().createLineViolation(this, "Remove this ''{0}'' label.", astNode, astNode.getFirstChild(FlexGrammar.IDENTIFIER).getTokenValue());
    }

  }

  private static boolean isIterationStatement(AstNode subStatement) {
    AstNode astNode = subStatement.getFirstChild();
    return astNode.is(FlexGrammar.STATEMENT)
      && astNode.getFirstChild().is(FlexGrammar.WHILE_STATEMENT, FlexGrammar.DO_STATEMENT, FlexGrammar.FOR_STATEMENT);
  }

}
