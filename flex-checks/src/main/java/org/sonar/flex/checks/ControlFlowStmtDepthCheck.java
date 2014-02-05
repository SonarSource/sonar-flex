/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
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
import com.sonar.sslr.squid.checks.SquidCheck;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;

/**
 * Note that implementation differs from AbstractNestedIfCheck - see SONARPLUGINS-1855 and SONARPLUGINS-2178
 */
@Rule(
  key = "S134",
  priority = Priority.MINOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MINOR)
public class ControlFlowStmtDepthCheck extends SquidCheck<LexerlessGrammar> {

  private int nestingLevel;

  private static final int DEFAULT_MAX = 3;

  @RuleProperty(
    key = "max",
    defaultValue = "" + DEFAULT_MAX)
  public int max = DEFAULT_MAX;

  public int getMax() {
    return max;
  }

  @Override
  public void init() {
    subscribeTo(
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
        getContext().createLineViolation(this, "Refactor this code to not nest more than {0} if/for/while/switch statements.", astNode, getMax());
      }
    }
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (!isElseIf(astNode)) {
      nestingLevel--;
    }
  }

  private boolean isElseIf(AstNode astNode) {
    return astNode.getParent().getParent().getPreviousSibling() != null
      && astNode.getParent().getParent().getPreviousSibling().is(FlexKeyword.ELSE);
  }

}
