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
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S1446",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class DynamicClassCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.getPreviousAstNode() != null && astNode.getPreviousAstNode().is(FlexGrammar.ATTRIBUTES) && isDynamic(astNode.getPreviousAstNode())) {
      String className = astNode
        .getFirstChild(FlexGrammar.CLASS_NAME)
        .getFirstChild(FlexGrammar.CLASS_IDENTIFIERS)
        .getLastChild().getTokenValue();
      getContext().createLineViolation(this, "Make this \"{0}\" class non-dynamic", astNode, className);
    }
  }

  private static boolean isDynamic(AstNode attributesNode) {
    for (AstNode attribute : attributesNode.getChildren(FlexGrammar.ATTRIBUTE)) {

      if (attribute.getFirstChild().is(FlexGrammar.ATTRIBUTE_EXPR)
        && attribute.getFirstChild().getNumberOfChildren() == 1
        && attribute.getFirstChild().getFirstChild(FlexGrammar.IDENTIFIER).getFirstChild().is(FlexKeyword.DYNAMIC)) {
        return true;
      }
    }
    return false;
  }

}
