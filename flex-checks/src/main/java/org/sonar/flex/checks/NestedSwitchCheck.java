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
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;

@Rule(
  key = "S1821",
  name = "\"switch\" structures should not be nested",
  priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.UNDERSTANDABILITY)
@SqaleConstantRemediation("10min")
public class NestedSwitchCheck extends SquidCheck<LexerlessGrammar> {

  private int switchLevel = 0;

  @Override
  public void init() {
    subscribeTo(FlexGrammar.SWITCH_STATEMENT);
  }

  @Override
  public void leaveFile(@Nullable AstNode astNode) {
    switchLevel = 0;
  }

  @Override
  public void visitNode(AstNode astNode) {
    switchLevel++;

    if (switchLevel > 1) {
      getContext().createLineViolation(this, "Move this \"switch\" to a function or refactor the code to eliminate it.", astNode);
    }
  }

  @Override
  public void leaveNode(AstNode astNode) {
    switchLevel--;
  }
}
