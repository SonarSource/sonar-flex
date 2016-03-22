/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
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
package org.sonar.flex.metrics;

import com.sonar.sslr.api.AstNode;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.FlexPunctuator;
import org.sonar.flex.api.FlexMetric;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.sslr.parser.LexerlessGrammar;

public class ComplexityVisitor extends SquidAstVisitor<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(
      // Entry points
      FlexGrammar.FUNCTION_DEF,
      FlexGrammar.FUNCTION_EXPR,

      // Branching nodes
      FlexGrammar.IF_STATEMENT,
      FlexGrammar.FOR_STATEMENT,
      FlexGrammar.WHILE_STATEMENT,
      FlexGrammar.DO_STATEMENT,
      FlexKeyword.CASE,
      FlexGrammar.CATCH_CLAUSE,
      FlexGrammar.RETURN_STATEMENT,
      FlexGrammar.THROW_STATEMENT,

      // Expressions
      FlexPunctuator.QUERY,
      FlexGrammar.LOGICAL_AND_OPERATOR,
      FlexGrammar.LOGICAL_OR_OPERATOR);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (isAccessor(astNode) || isLastReturnStatement(astNode)) {
      return;
    }
    getContext().peekSourceCode().add(FlexMetric.COMPLEXITY, 1);
  }

  public boolean isAccessor(AstNode astNode) {
    return astNode.is(FlexGrammar.FUNCTION_DEF)
      && astNode.getFirstChild(FlexGrammar.FUNCTION_NAME).getFirstChild(FlexKeyword.GET, FlexKeyword.SET) != null;
  }

  public boolean isLastReturnStatement(AstNode astNode) {
    if (astNode.is(FlexGrammar.RETURN_STATEMENT) && astNode.getNextAstNode().is(FlexPunctuator.RCURLYBRACE)) {
      AstNode parentNode = astNode.getNextAstNode().getParent().getParent();
      return parentNode != null && parentNode.is(FlexGrammar.FUNCTION_COMMON);
    }
    return false;
  }

}
