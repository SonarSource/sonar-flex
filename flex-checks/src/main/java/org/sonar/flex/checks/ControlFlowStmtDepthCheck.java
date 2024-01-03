/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2024 SonarSource SA
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
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;

/**
 * Note that implementation differs from AbstractNestedIfCheck - see SONARPLUGINS-1855 and SONARPLUGINS-2178
 */
@Rule(key = "S134")
public class ControlFlowStmtDepthCheck extends FlexCheck {

  private int nestingLevel;

  private static final int DEFAULT_MAX = 3;

  @RuleProperty(
    key = "max",
    description = "Maximum allowed control flow statement nesting depth.",
    defaultValue = "" + DEFAULT_MAX)
  public int max = DEFAULT_MAX;

  public int getMax() {
    return max;
  }

  @Override
  public List<AstNodeType> subscribedTo() {
    return Arrays.asList(
      FlexGrammar.IF_STATEMENT,
      FlexGrammar.DO_STATEMENT,
      FlexGrammar.WHILE_STATEMENT,
      FlexGrammar.FOR_STATEMENT,
      FlexGrammar.SWITCH_STATEMENT);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    nestingLevel = 0;
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (!isElseIf(astNode)) {
      nestingLevel++;
      if (nestingLevel == getMax() + 1) {
        addIssue(MessageFormat.format("Refactor this code to not nest more than {0} if/for/while/switch statements.", getMax()), astNode);
      }
    }
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (!isElseIf(astNode)) {
      nestingLevel--;
    }
  }

  private static boolean isElseIf(AstNode astNode) {
    return astNode.getParent().getParent().getPreviousSibling() != null
      && astNode.getParent().getParent().getPreviousSibling().is(FlexKeyword.ELSE);
  }

}
