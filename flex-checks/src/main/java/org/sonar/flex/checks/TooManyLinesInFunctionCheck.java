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
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexPunctuator;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S138",
  name = "Functions should not have too many lines",
  priority = Priority.MAJOR,
  tags = Tags.BRAIN_OVERLOAD)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.UNDERSTANDABILITY)
@SqaleConstantRemediation("20min")
public class TooManyLinesInFunctionCheck extends SquidCheck<LexerlessGrammar> {

  private static final int DEFAULT = 100;

  @RuleProperty(
    key = "max",
    description = "Maximum authorized lines in a function",
    defaultValue = "" + DEFAULT)
  int max = DEFAULT;

  @Override
  public void init() {
    subscribeTo(
      FlexGrammar.FUNCTION_DEF,
      FlexGrammar.FUNCTION_EXPR);
  }

  @Override
  public void visitNode(AstNode astNode) {
    int nbLines = getNumberOfLine(astNode);

    if (nbLines > max) {
      getContext().createLineViolation(this, "This function has {0} lines, which is greater than the {1} lines authorized. Split it into smaller functions.",
        astNode, nbLines, max);
    }
  }

  public static int getNumberOfLine(AstNode functionNode) {
    AstNode blockNode = functionNode.getFirstChild(FlexGrammar.FUNCTION_COMMON).getFirstChild(FlexGrammar.BLOCK);

    // Is not an abstract method
    if (blockNode != null) {
      int firstLine = blockNode.getFirstChild(FlexPunctuator.LCURLYBRACE).getTokenLine();
      int lastLine = blockNode.getFirstChild(FlexPunctuator.RCURLYBRACE).getTokenLine();
      return lastLine - firstLine + 1;
    }

    return 0;
  }
}
