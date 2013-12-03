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
package org.sonar.flex.checks.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sonar.sslr.api.AstNode;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;

import java.util.List;

public class Variable {

  private Variable() {
  }

  public static String getName(AstNode varDeclStatement) {
    Preconditions.checkArgument(varDeclStatement.is(FlexGrammar.VARIABLE_DECLARATION_STATEMENT));
    return varDeclStatement
      .getFirstChild(FlexGrammar.VARIABLE_DEF)
      .getFirstChild(FlexGrammar.VARIABLE_BINDING_LIST)
      .getFirstChild(FlexGrammar.VARIABLE_BINDING)
      .getFirstChild(FlexGrammar.TYPED_IDENTIFIER)
      .getFirstChild(FlexGrammar.IDENTIFIER).getTokenValue();
  }

  public static boolean isVariable(AstNode directive) {
    Preconditions.checkArgument(directive.is(FlexGrammar.DIRECTIVE));
    if (directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE) != null) {
      AstNode variableDecStmt = directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE).getFirstChild(FlexGrammar.VARIABLE_DECLARATION_STATEMENT);

      if (variableDecStmt != null) {
        return variableDecStmt
          .getFirstChild(FlexGrammar.VARIABLE_DEF)
          .getFirstChild(FlexGrammar.VARIABLE_DEF_KIND)
          .getFirstChild().is(FlexKeyword.VAR);
      }
    }
    return false;
  }

  public static boolean isConst(AstNode directive) {
    Preconditions.checkArgument(directive.is(FlexGrammar.DIRECTIVE));
    if (directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE) != null) {
      AstNode variableDecStmt = directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE).getFirstChild(FlexGrammar.VARIABLE_DECLARATION_STATEMENT);

      if (variableDecStmt != null) {
        return variableDecStmt
          .getFirstChild(FlexGrammar.VARIABLE_DEF)
          .getFirstChild(FlexGrammar.VARIABLE_DEF_KIND)
          .getFirstChild().is(FlexKeyword.CONST);
      }
    }
    return false;
  }

  public static List<AstNode> getDeclaredIdentifiers(AstNode varDeclStatement) {
    Preconditions.checkArgument(varDeclStatement.is(FlexGrammar.VARIABLE_DECLARATION_STATEMENT));
    List<AstNode> identifiers = Lists.newArrayList();
    if (varDeclStatement.is(FlexGrammar.VARIABLE_DECLARATION_STATEMENT)) {
      AstNode varBindingList = varDeclStatement
        .getFirstChild(FlexGrammar.VARIABLE_DEF)
        .getFirstChild(FlexGrammar.VARIABLE_BINDING_LIST);

      for (AstNode varBinding : varBindingList.getChildren(FlexGrammar.VARIABLE_BINDING)) {
        identifiers.add(varBinding.getFirstChild(FlexGrammar.TYPED_IDENTIFIER).getFirstChild(FlexGrammar.IDENTIFIER));
      }
    }
    return identifiers;
  }

}
