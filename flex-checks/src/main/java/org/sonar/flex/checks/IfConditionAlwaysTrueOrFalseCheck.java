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
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.util.Collections;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;

@Rule(key = "S1145")
public class IfConditionAlwaysTrueOrFalseCheck extends FlexCheck {


  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.IF_STATEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    AstNode conditionalExpr = astNode.getFirstChild(FlexGrammar.PARENTHESIZED_LIST_EXPR).getFirstChild(FlexGrammar.LIST_EXPRESSION);
    if (conditionalExpr.getChildren().size() == 1) {

      AstNode condition = conditionalExpr.getFirstChild().getFirstChild();
      if ((condition.is(FlexGrammar.POSTFIX_EXPR)
        && condition.getFirstChild().is(FlexGrammar.PRIMARY_EXPR)
        && condition.getFirstChild().getFirstChild().is(FlexKeyword.TRUE))
        || condition.getFirstChild().getFirstChild().is(FlexKeyword.FALSE)) {

        addIssue("Remove this if statement.", astNode);
      }


    }
  }

}
