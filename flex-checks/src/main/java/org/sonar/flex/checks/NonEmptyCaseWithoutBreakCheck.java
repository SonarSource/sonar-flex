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

import com.google.common.collect.Iterables;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "NonEmptyCaseWithoutBreak",
  name = "Switch cases should end with an unconditional \"break\" statement",
  tags = {Tags.CWE, Tags.MISRA, Tags.CERT, Tags.PITFALL},
  priority = Priority.CRITICAL)
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.LOGIC_RELIABILITY)
@SqaleConstantRemediation("10min")
public class NonEmptyCaseWithoutBreakCheck extends SquidCheck<LexerlessGrammar> {

  private static final AstNodeType[] JUMP_NODES = {FlexGrammar.BREAK_STATEMENT, FlexGrammar.RETURN_STATEMENT, FlexGrammar.THROW_STATEMENT, FlexGrammar.CONTINUE_STATEMENT};

  @Override
  public void init() {
    subscribeTo(FlexGrammar.CASE_ELEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (isLastCaseElement(astNode)) {
      return;
    }

    int jumpStmtNumber = astNode.getDescendants(JUMP_NODES).size();

    if (jumpStmtNumber < 2) {
      AstNode directive = astNode.getLastChild();
      visitLastDirective(astNode, directive);
    }
  }

  private static boolean isLastCaseElement(AstNode astNode) {
    return astNode.getNextSibling().isNot(FlexGrammar.CASE_ELEMENT);
  }

  private void visitLastDirective(AstNode astNode, AstNode directive) {
    if (isBlock(directive)) {
      visitLastDirective(astNode, directive.getFirstChild().getFirstChild().getLastChild());
      return;
    }
    if (directive.getFirstChild().is(FlexGrammar.STATEMENT)
      && directive.getFirstChild().getFirstChild().isNot(JUMP_NODES)) {
      getContext().createLineViolation(this, "Last statement in this switch-clause should be an unconditional break.",
        Iterables.getLast(astNode.getChildren(FlexGrammar.CASE_LABEL)));
    }
  }

  private static boolean isBlock(AstNode directive) {
    return directive.getNumberOfChildren() == 1
        && directive.getFirstChild().is(FlexGrammar.STATEMENT)
        && directive.getFirstChild().getNumberOfChildren() == 1
        && directive.getFirstChild().getFirstChild().is(FlexGrammar.BLOCK);
  }

}
