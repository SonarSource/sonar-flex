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
import com.sonar.sslr.api.Token;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;
import java.util.List;

@Rule(
  key = "S1952",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class InstantiationInLoopCheck extends SquidCheck<LexerlessGrammar> {


  private static final GrammarRuleKey[] ITERATION_NODES = {
    FlexGrammar.DO_STATEMENT,
    FlexGrammar.WHILE_STATEMENT,
    FlexGrammar.FOR_STATEMENT};

  private int loopLevel = 0;

  @Override
  public void init() {
    subscribeTo(
      FlexGrammar.FULL_NEW_EXPR,
      FlexGrammar.SHORT_NEW_EXPR,
      FlexGrammar.OBJECT_INITIALISER);
    subscribeTo(ITERATION_NODES);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    loopLevel = 0;
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(ITERATION_NODES)) {
      loopLevel++;

    } else if (loopLevel > 0 && !isNestedNewExpression(astNode)) {
      getContext().createLineViolation(this, "Move the instantiation of this \"{0}\" outside the loop.", astNode, getClassName(astNode));
    }
  }

  private boolean isNestedNewExpression(AstNode newExpression) {
    return newExpression.getParent().is(FlexGrammar.FULL_NEW_SUB_EXPR, FlexGrammar.SHORT_NEW_SUB_EXPR);
  }

  private Object getClassName(AstNode astNode) {
    if (astNode.is(FlexGrammar.OBJECT_INITIALISER)) {
      return "Object";
    }

    List<Token> tokens = astNode.getTokens();
    int nbToken = tokens.size();
    String currentTokenValue;
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < nbToken; i++) {
      currentTokenValue = tokens.get(i).getValue();
      if ("[".equals(currentTokenValue) || "(".equals(currentTokenValue)) {
        break;

      } else if (!"new".equals(currentTokenValue)) {
        builder.append(currentTokenValue);
      }
    }

    return builder.toString();
  }


  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.is(ITERATION_NODES)) {
      loopLevel--;
    }
  }
}
