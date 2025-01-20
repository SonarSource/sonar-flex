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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.grammar.GrammarRuleKey;

@Rule(key = "S1952")
public class InstantiationInLoopCheck extends FlexCheck {


  private static final GrammarRuleKey[] ITERATION_NODES = {
    FlexGrammar.DO_STATEMENT,
    FlexGrammar.WHILE_STATEMENT,
    FlexGrammar.FOR_STATEMENT};

  private int loopLevel = 0;

  @Override
  public List<AstNodeType> subscribedTo() {
    List<AstNodeType> types = new ArrayList<>();
    Collections.addAll(types,
      FlexGrammar.FULL_NEW_EXPR,
      FlexGrammar.SHORT_NEW_EXPR,
      FlexGrammar.OBJECT_INITIALISER);
    Collections.addAll(types, ITERATION_NODES);
    return types;
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
      addIssue(MessageFormat.format("Move the instantiation of this \"{0}\" outside the loop.", getClassName(astNode)), astNode);
    }
  }

  private static boolean isNestedNewExpression(AstNode newExpression) {
    return newExpression.getParent().is(FlexGrammar.FULL_NEW_SUB_EXPR, FlexGrammar.SHORT_NEW_SUB_EXPR);
  }

  private static Object getClassName(AstNode astNode) {
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
