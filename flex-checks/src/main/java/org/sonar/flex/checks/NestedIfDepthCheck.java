/*
 * Sonar Flex Plugin
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

/**
 * Note that implementation differs from AbstractNestedIfCheck - see SONARPLUGINS-1855 and SONARPLUGINS-2178
 */
@Rule(
  key = "NestedIfDepth",
  priority = Priority.MINOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MINOR)
public class NestedIfDepthCheck extends SquidCheck<LexerlessGrammar> {

  private int nestingLevel;

  private static final int DEFAULT_MAXIMUM_NESTING_LEVEL = 3;

  @RuleProperty(
    key = "maximumNestingLevel",
    defaultValue = "" + DEFAULT_MAXIMUM_NESTING_LEVEL)
  public int maximumNestingLevel = DEFAULT_MAXIMUM_NESTING_LEVEL;

  public int getMaximumNestingLevel() {
    return maximumNestingLevel;
  }

  @Override
  public void init() {
    subscribeTo(FlexGrammar.IF_STATEMENT);
  }

  @Override
  public void visitFile(AstNode astNode) {
    nestingLevel = 0;
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (!isElseIf(astNode)) {
      nestingLevel++;
      if (nestingLevel == getMaximumNestingLevel() + 1) {
        getContext().createLineViolation(this, "This if has a nesting level of {0}, which is higher than the maximum allowed {1}.", astNode,
          nestingLevel,
          getMaximumNestingLevel());
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
