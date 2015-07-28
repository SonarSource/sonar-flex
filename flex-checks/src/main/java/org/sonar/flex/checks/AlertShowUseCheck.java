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

import com.sonar.sslr.api.AstAndTokenVisitor;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;
import org.sonar.api.server.rule.RulesDefinition.SubCharacteristics;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;

@Rule(
  key = "S1442",
  name = "\"Alert.show(...)\" should not be used",
  tags = {Tags.CWE, Tags.SECURITY, Tags.USER_EXPERIENCE},
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.BLOCKER)
@SqaleSubCharacteristic(SubCharacteristics.SECURITY_FEATURES)
@SqaleConstantRemediation("10min")
public class AlertShowUseCheck extends SquidCheck<LexerlessGrammar> implements AstAndTokenVisitor {

  private enum State {
    EXPECTING_ALERT,
    EXPECTING_DOT,
    EXPECTING_SHOW,
    FOUND_ISSUE
  }

  private enum Symbol {
    OTHER,
    ALERT,
    DOT,
    SHOW
  }

  private static final State[][] TRANSITIONS = new State[State.values().length][Symbol.values().length];

  static {
    for (int i = 0; i < TRANSITIONS.length; i++) {
      for (int j = 0; j < TRANSITIONS[i].length; j++) {
        TRANSITIONS[i][j] = State.EXPECTING_ALERT;
      }
    }

    TRANSITIONS[State.EXPECTING_ALERT.ordinal()][Symbol.ALERT.ordinal()] = State.EXPECTING_DOT;
    TRANSITIONS[State.EXPECTING_DOT.ordinal()][Symbol.DOT.ordinal()] = State.EXPECTING_SHOW;
    TRANSITIONS[State.EXPECTING_SHOW.ordinal()][Symbol.SHOW.ordinal()] = State.FOUND_ISSUE;
  }

  private State currentState;

  @Override
  public void visitFile(@Nullable AstNode node) {
    currentState = State.EXPECTING_ALERT;
  }

  @Override
  public void visitToken(Token token) {
    currentState = TRANSITIONS[currentState.ordinal()][getSymbol(token.getOriginalValue()).ordinal()];

    if (currentState == State.FOUND_ISSUE) {
      getContext().createLineViolation(this, "Remove this usage of Alert.show().", token);
      currentState = State.EXPECTING_ALERT;
    }
  }

  private static Symbol getSymbol(String value) {
    Symbol result = Symbol.OTHER;

    if (".".equals(value)) {
      result = Symbol.DOT;
    } else if ("Alert".equals(value)) {
      result = Symbol.ALERT;
    } else if ("show".equals(value)) {
      result = Symbol.SHOW;
    }

    return result;
  }
}
