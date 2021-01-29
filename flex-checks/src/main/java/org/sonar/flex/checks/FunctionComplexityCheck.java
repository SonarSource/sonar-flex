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
import java.util.Arrays;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.metrics.ComplexityVisitor;

@Rule(key = "FunctionComplexity")
public class FunctionComplexityCheck extends FlexCheck {

  private static final int DEFAULT_MAXIMUM_FUNCTION_COMPLEXITY_THRESHOLD = 10;

  @RuleProperty(
    key = "maximumFunctionComplexityThreshold",
    description = "The maximum authorized complexity.",
    defaultValue = "" + DEFAULT_MAXIMUM_FUNCTION_COMPLEXITY_THRESHOLD)
  private int maximumFunctionComplexityThreshold = DEFAULT_MAXIMUM_FUNCTION_COMPLEXITY_THRESHOLD;

  @Override
  public List<AstNodeType> subscribedTo() {
    return Arrays.asList(FlexGrammar.FUNCTION_DEF, FlexGrammar.FUNCTION_EXPR);
  }

  @Override
  public void visitNode(AstNode node) {
    int functionComplexity = ComplexityVisitor.functionComplexity(node);
    if (functionComplexity > maximumFunctionComplexityThreshold) {
      String message = String.format("Function has a complexity of %s which is greater than %s authorized.", functionComplexity, maximumFunctionComplexityThreshold);
      addIssueWithCost(message, node, (double)functionComplexity - maximumFunctionComplexityThreshold);
    }
  }

  public void setMaximumFunctionComplexityThreshold(int threshold) {
    this.maximumFunctionComplexityThreshold = threshold;
  }

}
