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
  key = "S1445",
  priority = Priority.MINOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MINOR)
public class ConstructorWithVoidReturnTypeCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    AstNode constructorDef = getConstructor(astNode);
    if (constructorDef != null) {
      AstNode resultTypeNode = constructorDef.getFirstChild(FlexGrammar.FUNCTION_COMMON)
        .getFirstChild(FlexGrammar.FUNCTION_SIGNATURE)
        .getFirstChild(FlexGrammar.RESULT_TYPE);

      if (resultTypeNode != null && resultTypeNode.getFirstChild(FlexKeyword.VOID) != null) {
        getContext().createLineViolation(this, "Remove the \"void\" return type from this \"{0}\" constructor", constructorDef,
          constructorDef.getFirstChild(FlexGrammar.FUNCTION_NAME).getFirstChild().getTokenValue());
      }
    }
  }

  private static AstNode getConstructor(AstNode classDefNode) {
    final String className = classDefNode.getFirstChild(FlexGrammar.CLASS_NAME)
      .getFirstChild(FlexGrammar.CLASS_IDENTIFIERS)
      .getLastChild()
      .getTokenValue();

    for (AstNode directive : classDefNode.getFirstChild(FlexGrammar.BLOCK).getFirstChild(FlexGrammar.DIRECTIVES).getChildren()) {

      if (directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE) != null
          && directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE).getFirstChild().is(FlexGrammar.FUNCTION_DEF)) {
          AstNode functionDef = directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE).getFirstChild(FlexGrammar.FUNCTION_DEF);

          if (functionDef.getFirstChild(FlexGrammar.FUNCTION_NAME).getNumberOfChildren() == 1
            && functionDef.getFirstChild(FlexGrammar.FUNCTION_NAME).getFirstChild().getTokenValue().equals(className)) {
              return functionDef;
          }
        }
    }
    return null;
  }
}