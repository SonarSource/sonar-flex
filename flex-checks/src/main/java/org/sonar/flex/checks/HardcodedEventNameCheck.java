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
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

@Rule(
  key = "S1462",
  name = "Event names should not be hardcoded in event listeners",
  tags = Tags.DESIGN,
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)

@SqaleSubCharacteristic(SubCharacteristics.ARCHITECTURE_CHANGEABILITY)
@SqaleConstantRemediation("5min")
public class HardcodedEventNameCheck extends SquidCheck<LexerlessGrammar> implements AstAndTokenVisitor {

  private static final Pattern STRING_PATTERN = Pattern.compile(FlexGrammar.STRING_REGEXP);

  private enum State {
    EXPECTING_ADD_EVENT,
    EXPECTING_BRACE,
    EXPECTING_STRING,
    FOUND_ISSUE
  }

  private enum Symbol {
    OTHER,
    ADD_EVENT,
    BRACE,
    STRING
  }

  private static final State[][] TRANSITIONS = new State[State.values().length][Symbol.values().length];

  static {
    for (int i = 0; i < TRANSITIONS.length; i++) {
      for (int j = 0; j < TRANSITIONS[i].length; j++) {
        TRANSITIONS[i][j] = State.EXPECTING_ADD_EVENT;
      }
    }

    TRANSITIONS[State.EXPECTING_ADD_EVENT.ordinal()][Symbol.ADD_EVENT.ordinal()] = State.EXPECTING_BRACE;
    TRANSITIONS[State.EXPECTING_BRACE.ordinal()][Symbol.BRACE.ordinal()] = State.EXPECTING_STRING;
    TRANSITIONS[State.EXPECTING_STRING.ordinal()][Symbol.STRING.ordinal()] = State.FOUND_ISSUE;
  }

  private State currentState;

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    currentState = State.EXPECTING_ADD_EVENT;
  }

  @Override
  public void visitToken(Token token) {
    currentState = TRANSITIONS[currentState.ordinal()][getSymbol(token.getValue()).ordinal()];

    if (currentState == State.FOUND_ISSUE) {
      getContext().createLineViolation(this, "The event name {0} should be defined in a constant variable.", token, token.getValue());
      currentState = State.EXPECTING_ADD_EVENT;
    }
  }

  private static Symbol getSymbol(String value) {
    Symbol result = Symbol.OTHER;

    if ("(".equals(value)) {
      result = Symbol.BRACE;
    } else if ("addEventListener".equals(value)) {
      result = Symbol.ADD_EVENT;
    } else if (STRING_PATTERN.matcher(value).matches()) {
      result = Symbol.STRING;
    }

    return result;
  }
}
