/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
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
import java.util.Collections;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.metrics.ComplexityVisitor;

@Rule(key = "ClassComplexity")
@Deprecated
public class ClassComplexityCheck extends FlexCheck {

  private static final int DEFAULT_MAXIMUM_CLASS_COMPLEXITY_THRESHOLD = 80;

  @RuleProperty(
    key = "maximumClassComplexityThreshold",
    description = "Maximum complexity allowed.",
    defaultValue = "" + DEFAULT_MAXIMUM_CLASS_COMPLEXITY_THRESHOLD)
  private int maximumClassComplexityThreshold = DEFAULT_MAXIMUM_CLASS_COMPLEXITY_THRESHOLD;

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode node) {
    int complexity = ComplexityVisitor.complexity(node);
    if (complexity > maximumClassComplexityThreshold) {
      String message = String.format("Class has a complexity of %s which is greater than %s authorized.", complexity, maximumClassComplexityThreshold);
      addIssueWithCost(message, node, (double)complexity - maximumClassComplexityThreshold);
    }
  }

  public void setMaximumClassComplexityThreshold(int threshold) {
    this.maximumClassComplexityThreshold = threshold;
  }

}
