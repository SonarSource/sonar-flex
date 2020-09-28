/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2020 SonarSource SA
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

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Token;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;

@Rule(key = "S1462")
public class HardcodedEventNameCheck extends FlexCheck {

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
  public List<AstNodeType> subscribedTo() {
    return Collections.emptyList();
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    currentState = State.EXPECTING_ADD_EVENT;
  }

  @Override
  public void visitToken(Token token) {
    currentState = TRANSITIONS[currentState.ordinal()][getSymbol(token.getValue()).ordinal()];

    if (currentState == State.FOUND_ISSUE) {
      addIssue(MessageFormat.format("The event name {0} should be defined in a constant variable.", token.getValue()), token);
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
