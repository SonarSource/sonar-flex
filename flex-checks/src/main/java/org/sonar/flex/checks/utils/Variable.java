/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource Sàrl.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonar.flex.checks.utils;

import com.sonar.sslr.api.AstNode;
import java.util.ArrayList;
import java.util.List;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;

public final class Variable {

  private Variable() {
  }

  public static String getName(AstNode varDeclStatement) {
    Preconditions.checkState(varDeclStatement.is(FlexGrammar.VARIABLE_DECLARATION_STATEMENT));
    return varDeclStatement
      .getFirstChild(FlexGrammar.VARIABLE_DEF)
      .getFirstChild(FlexGrammar.VARIABLE_BINDING_LIST)
      .getFirstChild(FlexGrammar.VARIABLE_BINDING)
      .getFirstChild(FlexGrammar.TYPED_IDENTIFIER)
      .getFirstChild(FlexGrammar.IDENTIFIER).getTokenValue();
  }

  public static boolean isVariable(AstNode directive) {
    Preconditions.checkState(directive.is(FlexGrammar.DIRECTIVE));
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
    Preconditions.checkState(directive.is(FlexGrammar.DIRECTIVE));
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
    Preconditions.checkState(varDeclStatement.is(FlexGrammar.VARIABLE_DECLARATION_STATEMENT));
    List<AstNode> identifiers = new ArrayList<>();
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
