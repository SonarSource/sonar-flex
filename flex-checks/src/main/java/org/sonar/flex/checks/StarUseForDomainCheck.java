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
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;

@Rule(
  key = "S1468",
  name = "Security.allowDomain(...) should only be used in a tightly focused manner",
  tags = Tags.SECURITY,
  priority = Priority.CRITICAL)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.CRITICAL)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.SECURITY_FEATURES)
@SqaleConstantRemediation("10min")
public class StarUseForDomainCheck extends SquidCheck<LexerlessGrammar> implements AstAndTokenVisitor {

  private enum State {
    EXPECTING_SECURITY,
    EXPECTING_DOT,
    EXPECTING_ALLOW_DOMAIN,
    EXPECTING_LCURLY,
    EXPECTING_STAR,
    FOUND_ISSUE
  }

  private enum Symbol {
    OTHER,
    SECURITY,
    DOT,
    ALLOW_DOMAIN,
    RCURLY,
    STAR
  }

  private static final State[][] TRANSITIONS = new State[State.values().length][Symbol.values().length];

  static {
    for (int i = 0; i < TRANSITIONS.length; i++) {
      for (int j = 0; j < TRANSITIONS[i].length; j++) {
        TRANSITIONS[i][j] = State.EXPECTING_SECURITY;
      }
    }

    TRANSITIONS[State.EXPECTING_SECURITY.ordinal()][Symbol.SECURITY.ordinal()] = State.EXPECTING_DOT;
    TRANSITIONS[State.EXPECTING_DOT.ordinal()][Symbol.DOT.ordinal()] = State.EXPECTING_ALLOW_DOMAIN;
    TRANSITIONS[State.EXPECTING_ALLOW_DOMAIN.ordinal()][Symbol.ALLOW_DOMAIN.ordinal()] = State.EXPECTING_LCURLY;
    TRANSITIONS[State.EXPECTING_LCURLY.ordinal()][Symbol.RCURLY.ordinal()] = State.EXPECTING_STAR;
    TRANSITIONS[State.EXPECTING_STAR.ordinal()][Symbol.STAR.ordinal()] = State.FOUND_ISSUE;
  }

  private State currentState;

  @Override
  public void visitFile(@Nullable AstNode node) {
    currentState = State.EXPECTING_SECURITY;
  }

  @Override
  public void visitToken(Token token) {
    currentState = TRANSITIONS[currentState.ordinal()][getSymbol(token.getOriginalValue()).ordinal()];

    if (currentState == State.FOUND_ISSUE) {
      getContext().createLineViolation(this, "Specifically define the origin domain of SWF files that are allowed to communicate with this one", token);
      currentState = State.EXPECTING_SECURITY;
    }
  }


  private static Symbol getSymbol(String value) {
    Symbol result = Symbol.OTHER;

    if (".".equals(value)) {
      result = Symbol.DOT;
    } else if ("Security".equals(value)) {
      result = Symbol.SECURITY;
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
