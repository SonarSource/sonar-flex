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
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S1066",
  name = "Collapsible \"if\" statements should be merged",
  priority = Priority.MAJOR,
  tags = Tags.CLUMSY)
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("5min")
public class CollapsibleIfStatementCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.IF_STATEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (!hasElseClause(astNode)) {

      AstNode childtSatementNode = astNode.getFirstChild(FlexGrammar.SUB_STATEMENT).getFirstChild(FlexGrammar.STATEMENT);
      if (childtSatementNode != null) {

        AstNode nestedCollapsibleIf = getNestedIfCollapsible(childtSatementNode.getFirstChild());
        if (nestedCollapsibleIf != null) {
          getContext().createLineViolation(this, "Merge this if statement with the enclosing one.", nestedCollapsibleIf);
        }

      }
    }
  }

  private static AstNode getNestedIfCollapsible(AstNode statementNode) {
    if (statementNode.is(FlexGrammar.IF_STATEMENT)) {
      return statementNode;
    }

    if (statementNode.is(FlexGrammar.BLOCK) && statementNode.getFirstChild(FlexGrammar.DIRECTIVES).getChildren().size() == 1) {
      AstNode singleStatementChild = statementNode.getFirstChild(FlexGrammar.DIRECTIVES).getFirstChild(FlexGrammar.DIRECTIVE).getFirstChild();

      if (singleStatementChild.is(FlexGrammar.STATEMENT) && singleStatementChild.getFirstChild().is(FlexGrammar.IF_STATEMENT)) {
        AstNode ifNode = singleStatementChild.getFirstChild();
        return !hasElseClause(ifNode) ? ifNode : null;
      }
    }
    return null;
  }

  private static boolean hasElseClause(AstNode astNode) {
    return astNode.hasDirectChildren(FlexKeyword.ELSE);
  }
}
