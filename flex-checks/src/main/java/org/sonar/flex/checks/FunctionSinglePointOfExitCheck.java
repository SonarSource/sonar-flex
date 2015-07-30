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
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "FunctionSinglePointOfExit",
  name = "Functions shall have a single point of exit at the end of the function",
  priority = Priority.MINOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.UNDERSTANDABILITY)
@SqaleConstantRemediation("20min")
public class FunctionSinglePointOfExitCheck extends SquidCheck<LexerlessGrammar> {

  private int returnStatements;

  @Override
  public void init() {
    subscribeTo(FlexGrammar.FUNCTION_DEF, FlexGrammar.RETURN_STATEMENT);
  }

  @Override
  public void visitNode(AstNode node) {
    if (node.is(FlexGrammar.FUNCTION_DEF)) {
      returnStatements = 0;
    } else if (node.is(FlexGrammar.RETURN_STATEMENT)) {
      returnStatements++;
    }
  }

  @Override
  public void leaveNode(AstNode node) {
    if (node.is(FlexGrammar.FUNCTION_DEF) && (returnStatements != 0) && (returnStatements > 1 || !hasReturnAtEnd(node))) {
      getContext().createLineViolation(this, "A function shall have a single point of exit at the end of the function.", node);
    }
  }

  private static boolean hasReturnAtEnd(AstNode functionDefinitionNode) {
    AstNode lastDirectiveNode = functionDefinitionNode
      .getFirstChild(FlexGrammar.FUNCTION_COMMON)
      .getFirstChild(FlexGrammar.BLOCK)
      .getFirstChild(FlexGrammar.DIRECTIVES)
      .getLastChild();
    if (lastDirectiveNode != null) {
      AstNode statementNode = lastDirectiveNode.getFirstChild(FlexGrammar.STATEMENT);
      if (statementNode != null && statementNode.getFirstChild().is(FlexGrammar.RETURN_STATEMENT)) {
        return true;
      }
    }
    return false;
  }

}
