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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;

@Rule(key = "ActionScript2")
public class ActionScript2Check extends FlexCheck {

  private final Set<String> deprecatedOperators = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("or", "and", "ne", "eq", "ge", "gt", "le", "lt", "add", "<>")));

  @Override
  public List<AstNodeType> subscribedTo() {
    return Arrays.asList(
      FlexGrammar.UNARY_EXPR,
      FlexGrammar.LOGICAL_OR_OPERATOR,
      FlexGrammar.LOGICAL_AND_OPERATOR,
      FlexGrammar.EQUALITY_OPERATOR,
      FlexGrammar.RELATIONAL_OPERATOR,
      FlexGrammar.RELATIONAL_OPERATOR_NO_IN,
      FlexGrammar.ADDITIVE_OPERATOR
    );
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.UNARY_EXPR) && "not".equals(astNode.getFirstChild().getTokenValue())) {
      addIssue("Operator 'not' not available in ActionScript 3.0", astNode.getFirstChild());
    } else {
      String operator = getValue(astNode);
      if (deprecatedOperators.contains(operator)) {
        addIssue("Operator '" + operator + "' not available in ActionScript 3.0", astNode);
      }
    }
  }

  private static String getValue(AstNode astNode) {
    StringBuilder sb = new StringBuilder();
    for (AstNode child : astNode.getChildren()) {
      sb.append(child.getTokenValue());
    }
    return sb.toString();
  }

}
