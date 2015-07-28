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
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S1951",
  name = "The \"trace\" function should not be used",
  tags = {Tags.CWE, Tags.SECURITY},
  priority = Priority.CRITICAL)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.CRITICAL)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.SECURITY_FEATURES)
@SqaleConstantRemediation("2min")
public class TraceUseCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.POSTFIX_EXPR);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (isFunctionCall(astNode) && "trace".equals(astNode.getFirstChild().getTokenValue())) {
      getContext().createLineViolation(this, "Remove this use of the \"trace\" function.", astNode);
    }
  }

  private static boolean isFunctionCall(AstNode postfixExpr) {
    return postfixExpr.getFirstChild().is(FlexGrammar.PRIMARY_EXPR) && postfixExpr.getLastChild().is(FlexGrammar.ARGUMENTS);
  }

}
