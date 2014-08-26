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
import  org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexPunctuator;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S108",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class EmptyNestedBlockCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.BLOCK);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (!astNode.getFirstChild(FlexGrammar.DIRECTIVES).hasChildren() && isNested(astNode) && !hasComment(astNode)) {
      getContext().createLineViolation(this, "Either remove or fill this block of code.", astNode);
    }
  }

  private static boolean isNested(AstNode blockNode) {
    return !blockNode.getParent().is(
      FlexGrammar.CLASS_DEF,
      FlexGrammar.INTERFACE_DEF,
      FlexGrammar.PACKAGE_DEF,
      FlexGrammar.FUNCTION_COMMON);
  }

  private static boolean hasComment(AstNode blockNode) {
    return blockNode.getFirstChild(FlexPunctuator.RCURLYBRACE).getToken().hasTrivia();
  }

}
