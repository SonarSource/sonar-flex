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

import java.util.List;
@Rule(
  key = "S1170",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class PublicConstNotStaticCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    List<AstNode> classDirectives = astNode
      .getFirstChild(FlexGrammar.BLOCK)
      .getFirstChild(FlexGrammar.DIRECTIVES)
      .getChildren(FlexGrammar.DIRECTIVE);

    for (AstNode directive : classDirectives) {
      if (isConstDefinition(directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE)) && isPublicAndNotStatic(directive.getFirstChild(FlexGrammar.ATTRIBUTES))) {
        getContext().createLineViolation(this, "Make this const field \"{0}\" static too", directive,
          getConstName(directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE)));
      }
    }
  }

  private static String getConstName(AstNode annotableDir) {
    return annotableDir.getFirstChild()
      .getFirstChild(FlexGrammar.VARIABLE_DEF)
      .getFirstChild(FlexGrammar.VARIABLE_BINDING_LIST)
      .getFirstChild(FlexGrammar.VARIABLE_BINDING)
      .getFirstChild(FlexGrammar.TYPED_IDENTIFIER)
      .getFirstChild(FlexGrammar.IDENTIFIER).getTokenValue();
  }

  private static boolean isConstDefinition(AstNode annotableDir) {
    return annotableDir != null
      && annotableDir.getFirstChild().is(FlexGrammar.VARIABLE_DECLARATION_STATEMENT)
      && annotableDir.getFirstChild().getFirstChild(FlexGrammar.VARIABLE_DEF).getFirstChild(FlexGrammar.VARIABLE_DEF_KIND).getFirstChild().is(FlexKeyword.CONST);
  }

  private static boolean isPublicAndNotStatic(AstNode attributesNode) {
    if (attributesNode == null) {
      return false;
    }
    boolean isPublic = false, isStatic = false;

    for (AstNode attribute : attributesNode.getChildren(FlexGrammar.ATTRIBUTE)) {
      if (attribute.getFirstChild(FlexGrammar.RESERVED_NAMESPACE) != null
        && attribute.getFirstChild(FlexGrammar.RESERVED_NAMESPACE).getFirstChild().is(FlexKeyword.PUBLIC)) {
        isPublic = true;
      }

      if (attribute.getFirstChild(FlexGrammar.ATTRIBUTE_EXPR) != null
        && attribute.getFirstChild(FlexGrammar.ATTRIBUTE_EXPR).getFirstChild(FlexGrammar.IDENTIFIER).getFirstChild().is(FlexKeyword.STATIC)) {
        isStatic = true;
      }

    }
    return isPublic && !isStatic;
  }
}
