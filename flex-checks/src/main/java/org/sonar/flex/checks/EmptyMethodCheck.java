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
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexPunctuator;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S1186",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class EmptyMethodCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    for (AstNode function : Clazz.getFunctions(astNode)) {
      AstNode block = function.getFirstChild(FlexGrammar.FUNCTION_COMMON).getFirstChild(FlexGrammar.BLOCK);

      if (block != null && isEmptyBlock(block)) {
        getContext().createLineViolation(this, "Add a nested comment explaining why this method is empty, throw an NotSupportedException or complete the implementation.", function);
      }
    }
  }

  private static boolean isEmptyBlock(AstNode block) {
    AstNode rightCurlyBrace = block.getFirstChild(FlexPunctuator.RCURLYBRACE);
    return !block.getFirstChild(FlexGrammar.DIRECTIVES).hasChildren() && !hasComment(rightCurlyBrace);
  }

  private static boolean hasComment(AstNode node) {
    return node.getToken().hasTrivia();
  }
}
