/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Token;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexKeyword;

@Rule(key = "S1466")
public class ExactSettingsSetToFalseCheck extends FlexCheck {

  private enum State {
    EXPECTING_SECURITY,
    EXPECTING_DOT,
    EXPECTING_EXACT_SETTINGS,
    EXPECTING_EQUAL,
    EXPECTING_FALSE,
    FOUND_ISSUE
  }

  private enum Symbol {
    OTHER,
    SECURITY,
    DOT,
    EXACT_SETTINGS,
    EQUAL,
    FALSE
  }

  private static final State[][] TRANSITIONS = new State[State.values().length][Symbol.values().length];

  static {
    for (int i = 0; i < TRANSITIONS.length; i++) {
      for (int j = 0; j < TRANSITIONS[i].length; j++) {
        TRANSITIONS[i][j] = State.EXPECTING_SECURITY;
      }
    }

    TRANSITIONS[State.EXPECTING_SECURITY.ordinal()][Symbol.SECURITY.ordinal()] = State.EXPECTING_DOT;
    TRANSITIONS[State.EXPECTING_DOT.ordinal()][Symbol.DOT.ordinal()] = State.EXPECTING_EXACT_SETTINGS;
    TRANSITIONS[State.EXPECTING_EXACT_SETTINGS.ordinal()][Symbol.EXACT_SETTINGS.ordinal()] = State.EXPECTING_EQUAL;
    TRANSITIONS[State.EXPECTING_EQUAL.ordinal()][Symbol.EQUAL.ordinal()] = State.EXPECTING_FALSE;
    TRANSITIONS[State.EXPECTING_FALSE.ordinal()][Symbol.FALSE.ordinal()] = State.FOUND_ISSUE;
  }

  private State currentState;

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.emptyList();
  }

  @Override
  public void visitFile(@Nullable AstNode node) {
    currentState = State.EXPECTING_SECURITY;
  }


  @Override
  public void visitToken(Token token) {
    currentState = TRANSITIONS[currentState.ordinal()][getSymbol(token.getOriginalValue()).ordinal()];

    if (currentState == State.FOUND_ISSUE) {
      addIssue("Set flash.system.Security.exactSettings to true", token);
      currentState = State.EXPECTING_SECURITY;
    }
  }


  private static Symbol getSymbol(String value) {
    Symbol result = Symbol.OTHER;

    if (".".equals(value)) {
      result = Symbol.DOT;
    } else if ("Security".equals(value)) {
      result = Symbol.SECURITY;
    } else if ("exactSettings".equals(value)) {
      result = Symbol.EXACT_SETTINGS;
    } else if ("=".equals(value)) {
      result = Symbol.EQUAL;
    } else if (FlexKeyword.FALSE.getValue().equals(value)) {
      result = Symbol.FALSE;
    }

    return result;
  }
}
