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
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.squidbridge.checks.AbstractOneStatementPerLineCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "OneStatementPerLine",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class OneStatementPerLineCheck extends AbstractOneStatementPerLineCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.STATEMENT, FlexGrammar.VARIABLE_DECLARATION_STATEMENT);
  }

  @Override
  public AstNodeType getStatementRule() {
    return null;
  }

  @Override
  public boolean isExcluded(AstNode astNode) {
    AstNode statementNode = astNode.getFirstChild();
    return statementNode.is(FlexGrammar.BLOCK)
      || statementNode.is(FlexGrammar.EMPTY_STATEMENT)
      || statementNode.is(FlexGrammar.LABELED_STATEMENT);
  }

}
