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
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexPunctuator;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;


@Rule(
  key = "S1454",
  name = "Multiple \"++\" or \"--\" unary operators should not be used in a single arithmetic expression",
  priority = Priority.MINOR)
@ActivatedByDefault
@SqaleConstantRemediation("2min")
public class TooManyUnaryOperatorCheck extends SquidCheck<LexerlessGrammar> {

  private boolean assignmentExpression;
  private int counter;

  @Override
  public void init() {
    subscribeTo(
      FlexGrammar.ASSIGNMENT_EXPR,
      FlexPunctuator.DOUBLE_MINUS,
      FlexPunctuator.DOUBLE_PLUS);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    assignmentExpression = false;
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.ASSIGNMENT_EXPR)) {
      assignmentExpression = true;
    } else if (assignmentExpression && (astNode.is(FlexPunctuator.DOUBLE_MINUS) || astNode.is(FlexPunctuator.DOUBLE_PLUS))) {
      counter++;
    }
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.ASSIGNMENT_EXPR)) {
      if (counter > 1) {
        getContext().createLineViolation(this, "Split this expression into multiple expressions so that each one contains no more than a single \"++\" or \"--\" unary operator",
          astNode);
      }
      assignmentExpression = false;
      counter = 0;
    }
  }
}
