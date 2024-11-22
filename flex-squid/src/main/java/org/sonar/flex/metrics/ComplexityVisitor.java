/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2024 SonarSource SA
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
package org.sonar.flex.metrics;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.FlexPunctuator;
import org.sonar.flex.FlexVisitor;

public class ComplexityVisitor extends FlexVisitor {

  private int complexity;

  public int getComplexity() {
    return complexity;
  }

  @Override
  public List<AstNodeType> subscribedTo() {
    return Arrays.asList(
      // Entry points
      FlexGrammar.FUNCTION_DEF,
      FlexGrammar.FUNCTION_EXPR,

      // Branching nodes
      FlexGrammar.IF_STATEMENT,
      FlexGrammar.FOR_STATEMENT,
      FlexGrammar.WHILE_STATEMENT,
      FlexGrammar.DO_STATEMENT,
      FlexKeyword.CASE,

      // Expressions
      FlexPunctuator.QUERY,
      FlexGrammar.LOGICAL_AND_OPERATOR,
      FlexGrammar.LOGICAL_OR_OPERATOR);
  }

  @Override
  public void visitFile(@Nullable AstNode node) {
    complexity = 0;
  }

  @Override
  public void visitNode(AstNode astNode) {
    complexity++;
  }

  public static int complexity(AstNode root) {
    ComplexityVisitor visitor = new ComplexityVisitor();
    visitor.scanNode(root);
    return visitor.complexity;
  }

  public static int functionComplexity(AstNode functionDef) {
    ComplexityVisitor visitor = new FunctionComplexityVisitor(functionDef);
    visitor.scanNode(functionDef);
    return visitor.complexity;
  }

  private static class FunctionComplexityVisitor extends ComplexityVisitor {

    private final AstNode functionDef;
    private int nestingLevel = 0;

    public FunctionComplexityVisitor(AstNode functionDef) {
      this.functionDef = functionDef;
    }

    @Override
    public void visitNode(AstNode astNode) {
      if (isNestedFunction(astNode)) {
        nestingLevel++;
      }
      if (nestingLevel == 0) {
        super.visitNode(astNode);
      }
    }

    @Override
    public void leaveNode(AstNode node) {
      if (isNestedFunction(node)) {
        nestingLevel--;
      }
    }

    private boolean isNestedFunction(AstNode astNode) {
      return astNode.is(FlexGrammar.FUNCTION_DEF, FlexGrammar.FUNCTION_EXPR) && astNode != functionDef;
    }
  }
}
