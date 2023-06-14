/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2023 SonarSource SA
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
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;

@Rule(key = "S4507")
public class DebugFeaturesCheck extends FlexCheck {

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
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.POSTFIX_EXPR);
  }

  @Override
  public void visitFile(@Nullable AstNode node) {
    currentState = State.EXPECTING_ALERT;
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (isFunctionCall(astNode) && "trace".equals(astNode.getFirstChild().getTokenValue())) {
      addIssue("Make sure this debug feature \"trace()\" is deactivated before delivering the code in production.", astNode);
    }
  }

  @Override
  public void visitToken(Token token) {
    currentState = TRANSITIONS[currentState.ordinal()][getSymbol(token.getOriginalValue()).ordinal()];

    if (currentState == State.FOUND_ISSUE) {
      addIssue("Make sure this debug feature \"Alert.show()\" is deactivated before delivering the code in production.", token);
      currentState = State.EXPECTING_ALERT;
    }
  }

  private static boolean isFunctionCall(AstNode postfixExpr) {
    return postfixExpr.getFirstChild().is(FlexGrammar.PRIMARY_EXPR) && postfixExpr.getLastChild().is(FlexGrammar.ARGUMENTS);
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
