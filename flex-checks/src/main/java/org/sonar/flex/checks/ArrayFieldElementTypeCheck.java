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

@Rule(key = "S1469",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class ArrayFieldElementTypeCheck extends SquidCheck<LexerlessGrammar> {

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

      if (isVariableDefinition(directive)) {
        AstNode varBindingList = directive
          .getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE)
          .getFirstChild(FlexGrammar.VARIABLE_DECLARATION_STATEMENT)
          .getFirstChild(FlexGrammar.VARIABLE_DEF)
          .getFirstChild(FlexGrammar.VARIABLE_BINDING_LIST);

        for (AstNode varBinding : varBindingList.getChildren(FlexGrammar.VARIABLE_BINDING)) {

          if (!hasInitialisation(varBinding) && isArray(varBinding) && !hasMetadataWithType(directive)) {
            getContext().createLineViolation(this, "Define the element type for this ''{0}'' array", varBinding,
              varBinding.getFirstChild(FlexGrammar.TYPED_IDENTIFIER).getFirstChild(FlexGrammar.IDENTIFIER).getTokenValue());
          }
        }
      }

    }
  }

  private static boolean hasInitialisation(AstNode varBinding) {
    return varBinding.getFirstChild(FlexGrammar.VARIABLE_INITIALISATION) != null;
  }

  private static boolean isArray(AstNode varBinding) {
    AstNode typeExpr = varBinding.getFirstChild(FlexGrammar.TYPED_IDENTIFIER).getFirstChild(FlexGrammar.TYPE_EXPR);

    return typeExpr != null
      && typeExpr.getNumberOfChildren() == 1
      && "Array".equals(typeExpr.getFirstChild().getTokenValue());
  }

  private static boolean hasMetadataWithType(AstNode directive) {
    AstNode previousDirective = directive.getPreviousAstNode();

    if (previousDirective != null && isMetadataTag(previousDirective)) {
      AstNode elementList = previousDirective.getFirstChild()
        .getFirstChild(FlexGrammar.METADATA_STATEMENT)
        .getFirstChild(FlexGrammar.ARRAY_INITIALISER)
        .getFirstChild(FlexGrammar.ELEMENT_LIST);

      for (AstNode literalElement : elementList.getChildren(FlexGrammar.LITERAL_ELEMENT)) {

        if ("ArrayElementType".equals(literalElement.getTokenValue())) {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean isMetadataTag(AstNode directive) {
    return directive.getFirstChild().is(FlexGrammar.STATEMENT)
      && directive.getFirstChild().getFirstChild().is(FlexGrammar.METADATA_STATEMENT);
  }

  private static boolean isVariableDefinition(AstNode directive) {
    if (directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE) != null) {
      AstNode annotableDir = directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE).getFirstChild();

      return annotableDir.is(FlexGrammar.VARIABLE_DECLARATION_STATEMENT)
        && annotableDir.getFirstChild(FlexGrammar.VARIABLE_DEF).getFirstChild(FlexGrammar.VARIABLE_DEF_KIND).getFirstChild().is(FlexKeyword.VAR);
    }
    return false;
  }

}
