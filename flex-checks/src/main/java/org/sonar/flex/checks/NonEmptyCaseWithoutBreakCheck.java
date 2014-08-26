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

import com.google.common.collect.Iterables;
import com.sonar.sslr.api.AstNode;
import  org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "NonEmptyCaseWithoutBreak",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class NonEmptyCaseWithoutBreakCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.CASE_ELEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    AstNode lastAstNode = astNode.getLastChild();
    if (lastAstNode.getFirstChild().is(FlexGrammar.STATEMENT)
      && lastAstNode.getFirstChild().getFirstChild().isNot(FlexGrammar.BREAK_STATEMENT, FlexGrammar.RETURN_STATEMENT, FlexGrammar.THROW_STATEMENT)) {
      getContext().createLineViolation(this, "Last statement in this switch-clause should be an unconditional break.",
        Iterables.getLast(astNode.getChildren(FlexGrammar.CASE_LABEL)));
    }
  }

}
