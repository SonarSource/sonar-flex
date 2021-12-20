/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2021 SonarSource SA
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexPunctuator;
import org.sonar.flex.checks.utils.Expression;

@Rule(key = "S127")
public class VariantStopConditionInForLoopCheck extends FlexCheck {

  Set<String> counters = new HashSet<>();
  Set<String> pendingCounters = new HashSet<>();

  @Override
  public List<AstNodeType> subscribedTo() {
    return Arrays.asList(
      FlexGrammar.FOR_STATEMENT,
      FlexGrammar.SUB_STATEMENT,

      FlexGrammar.ASSIGNMENT_EXPR,
      FlexPunctuator.DOUBLE_PLUS,
      FlexPunctuator.DOUBLE_MINUS);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    counters.clear();
    pendingCounters.clear();
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.FOR_STATEMENT)) {
      pendingCounters.addAll(getLoopsCounters(astNode));
      checkLoopsCondition(astNode);

    } else if (astNode.is(FlexGrammar.SUB_STATEMENT) && !pendingCounters.isEmpty()) {
      counters.addAll(pendingCounters);
      pendingCounters.clear();

    } else if (!counters.isEmpty() && astNode.is(FlexGrammar.ASSIGNMENT_EXPR, FlexPunctuator.DOUBLE_PLUS, FlexPunctuator.DOUBLE_MINUS)) {
      checkIfModifyingCounter(astNode);
    }
  }

  private void checkLoopsCondition(AstNode forStatement) {
    AstNode stopConditionExpr = getStopCondition(forStatement);
    if (stopConditionExpr == null) {
      return;
    }

    for (AstNode assignmentExpr : stopConditionExpr.getChildren(FlexGrammar.ASSIGNMENT_EXPR)) {
      for (Token t : assignmentExpr.getTokens()) {

        String tokenValue = t.getValue();
        if (FlexPunctuator.LPARENTHESIS.getValue().equals(tokenValue) || FlexPunctuator.DOT.getValue().equals(tokenValue)) {
          addIssue("Calculate the stop condition value outside the loop and set it to a variable.", assignmentExpr);
          break;
        }
      }
    }
  }

  /**
   * Returns for statement stop condition, null if there is no stop condition.
   */
  @Nullable
  private static AstNode getStopCondition(AstNode forStatement) {
    AstNode semicolonNode = forStatement.getFirstChild(FlexPunctuator.SEMICOLON);

    if (semicolonNode != null) {
      AstNode stopConditionExpr = semicolonNode.getNextAstNode();
      return stopConditionExpr.is(FlexGrammar.LIST_EXPRESSION) ? stopConditionExpr : null;
    }
    return null;
  }

  private void checkIfModifyingCounter(AstNode expression) {
    AstNode varNode = null;
    if (expression.is(FlexGrammar.ASSIGNMENT_EXPR) && expression.hasDirectChildren(FlexGrammar.ASSIGNMENT_OPERATOR)) {
      varNode = expression.getFirstChild();
    } else if (expression.is(FlexPunctuator.DOUBLE_PLUS, FlexPunctuator.DOUBLE_MINUS)) {
      AstNode exprParent = expression.getParent();
      varNode = exprParent.is(FlexGrammar.UNARY_EXPR) ? exprParent.getLastChild() : exprParent.getFirstChild();
    }

    if (varNode != null) {
      String varName = Expression.exprToString(varNode);
      if (counters.contains(varName)) {
        addIssue(MessageFormat.format("Do not update the loop counter \"{0}\" within the loop body.", varName), varNode);
      }
    }
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.FOR_STATEMENT)) {
      counters.removeAll(getLoopsCounters(astNode));
    }
  }

  private static Set<String> getLoopsCounters(AstNode forStatement) {
    Set<String> loopCounters = new HashSet<>();
    AstNode initialiser = forStatement.getFirstChild(FlexGrammar.FOR_INITIALISER);

    if (initialiser != null) {
      AstNode initialiserExpr = initialiser.getFirstChild();

      if (initialiserExpr.is(FlexGrammar.VARIABLE_DEF_NO_IN)) {
        getCountersFromVariableDef(loopCounters, initialiserExpr);

      } else {
        getCountersFromListExpression(loopCounters, initialiserExpr);
      }
    }
    return loopCounters;
  }

  private static void getCountersFromListExpression(Set<String> counters, AstNode initialiserExpr) {
    for (AstNode assignmentExpr : initialiserExpr.getChildren(FlexGrammar.ASSIGNMENT_EXPR_NO_IN)) {
      AstNode exprFirstChild = assignmentExpr.getFirstChild();

      if (assignmentExpr.hasDirectChildren(FlexGrammar.ASSIGNMENT_OPERATOR)) {
        counters.add(Expression.exprToString(exprFirstChild));
      } else if (exprFirstChild.is(FlexGrammar.UNARY_EXPR)) {
        counters.add(Expression.exprToString(exprFirstChild.getLastChild()));
      } else if (exprFirstChild.is(FlexGrammar.POSTFIX_EXPR)) {
        counters.add(Expression.exprToString(exprFirstChild.getFirstChild()));
      }
    }
  }

  private static void getCountersFromVariableDef(Set<String> counters, AstNode initialiserExpr) {
    for (AstNode variableBinding : initialiserExpr.getFirstChild(FlexGrammar.VARIABLE_BINDING_LIST_NO_IN).getChildren(FlexGrammar.VARIABLE_BINDING_NO_IN)) {
      counters.add(Expression.exprToString(variableBinding.getFirstChild(FlexGrammar.TYPED_IDENTIFIER_NO_IN).getFirstChild(FlexGrammar.IDENTIFIER)));
    }
  }

}
