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
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;

@Rule(
  key = "S1465",
  name = "LocalConnection should be configured to narrowly specify the domains with which local connections to other Flex application are allowed",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.SECURITY_FEATURES)
@SqaleConstantRemediation("10min")
public class StarUseForLocalConnectionCheck extends SquidCheck<LexerlessGrammar> implements AstAndTokenVisitor {

  private Token previousToken = null;

  private enum State {
    EXPECTING_DOT,
    EXPECTING_ALLOW_DOMAIN,
    EXPECTING_LCURLY,
    EXPECTING_STAR,
    FOUND_ISSUE
  }

  private enum Symbol {
    OTHER,
    DOT,
    ALLOW_DOMAIN,
    RCURLY,
    STAR
  }

  private static final State[][] TRANSITIONS = new State[State.values().length][Symbol.values().length];

  static {
    for (int i = 0; i < TRANSITIONS.length; i++) {
      for (int j = 0; j < TRANSITIONS[i].length; j++) {
        TRANSITIONS[i][j] = State.EXPECTING_DOT;
      }
    }

    TRANSITIONS[State.EXPECTING_DOT.ordinal()][Symbol.DOT.ordinal()] = State.EXPECTING_ALLOW_DOMAIN;
    TRANSITIONS[State.EXPECTING_ALLOW_DOMAIN.ordinal()][Symbol.ALLOW_DOMAIN.ordinal()] = State.EXPECTING_LCURLY;
    TRANSITIONS[State.EXPECTING_LCURLY.ordinal()][Symbol.RCURLY.ordinal()] = State.EXPECTING_STAR;
    TRANSITIONS[State.EXPECTING_STAR.ordinal()][Symbol.STAR.ordinal()] = State.FOUND_ISSUE;
  }

  private State currentState;

  @Override
  public void visitFile(@Nullable AstNode node) {
    currentState = State.EXPECTING_DOT;
  }

  @Override
  public void visitToken(Token token) {
    currentState = TRANSITIONS[currentState.ordinal()][getSymbol(token.getOriginalValue()).ordinal()];

    if (currentState == State.FOUND_ISSUE) {
      getContext().createLineViolation(this, "Replace this wildcard character '*' with a well defined domain", token);
      currentState = State.EXPECTING_DOT;
    }
    previousToken = token;
  }

  private Symbol getSymbol(String value) {
    Symbol result = Symbol.OTHER;

    if (".".equals(value) && previousToken != null && !"Security".equals(previousToken.getValue())) {
      result = Symbol.DOT;
    } else if ("allowDomain".equals(value)) {
      result = Symbol.ALLOW_DOMAIN;
    } else if ("(".equals(value)) {
      result = Symbol.RCURLY;
    } else if ("\"*\"".equals(value)) {
      result = Symbol.STAR;
    }

    return result;
  }
}
