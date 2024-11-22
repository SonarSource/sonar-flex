/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2024 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
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

public class Function {

  private Function() {
  }

  public static String getName(AstNode functionDef) {
    Preconditions.checkState(functionDef.is(FlexGrammar.FUNCTION_DEF));
    return functionDef.getFirstChild(FlexGrammar.FUNCTION_NAME).getFirstChild(FlexGrammar.IDENTIFIER).getTokenValue();
  }

  public static boolean isAccessor(AstNode functionDef) {
    Preconditions.checkState(functionDef.is(FlexGrammar.FUNCTION_DEF));
    return functionDef.getFirstChild(FlexGrammar.FUNCTION_NAME).getFirstChild(FlexKeyword.GET, FlexKeyword.SET) != null;
  }


  public static boolean isEmptyConstructor(AstNode functionDef, String className) {
    Preconditions.checkState(functionDef.is(FlexGrammar.FUNCTION_DEF));
    AstNode functionBlock = functionDef.getFirstChild(FlexGrammar.FUNCTION_COMMON).getFirstChild(FlexGrammar.BLOCK);

    return isConstructor(functionDef, className)
      && (functionBlock == null || functionBlock.getFirstChild(FlexGrammar.DIRECTIVES).getChildren().isEmpty());
  }

  public static boolean isConstructor(AstNode functionDef, String className) {
    Preconditions.checkState(functionDef.is(FlexGrammar.FUNCTION_DEF));
    return functionDef.getFirstChild(FlexGrammar.FUNCTION_NAME).getNumberOfChildren() == 1
      && functionDef.getFirstChild(FlexGrammar.FUNCTION_NAME).getFirstChild().getTokenValue().equals(className);
  }


  public static List<AstNode> getParametersIdentifiers(AstNode functionDef) {
    Preconditions.checkState(functionDef.is(FlexGrammar.FUNCTION_DEF, FlexGrammar.FUNCTION_EXPR));
    List<AstNode> paramIdentifier = new ArrayList<>();
    AstNode parameters = functionDef
      .getFirstChild(FlexGrammar.FUNCTION_COMMON)
      .getFirstChild(FlexGrammar.FUNCTION_SIGNATURE)
      .getFirstChild(FlexGrammar.PARAMETERS);

    if (parameters != null) {
      for (AstNode parameter : parameters.getChildren(FlexGrammar.PARAMETER, FlexGrammar.REST_PARAMETERS)) {
        if (parameter.getFirstChild(FlexGrammar.TYPED_IDENTIFIER) != null) {
          paramIdentifier.add(parameter.getFirstChild(FlexGrammar.TYPED_IDENTIFIER).getFirstChild(FlexGrammar.IDENTIFIER));
        }
      }
    }
    return paramIdentifier;
  }

  public static boolean isOverriding(AstNode functionDef) {
    Preconditions.checkState(functionDef.is(FlexGrammar.FUNCTION_DEF));
    AstNode attributesNode = functionDef.getPreviousAstNode();

    if (attributesNode != null && attributesNode.is(FlexGrammar.ATTRIBUTES)) {

      for (AstNode attribute : attributesNode.getChildren()) {
        if (attribute.getFirstChild().is(FlexGrammar.ATTRIBUTE_EXPR)
          && attribute.getFirstChild().getNumberOfChildren() == 1
          && attribute.getFirstChild().getFirstChild(FlexGrammar.IDENTIFIER).getTokenValue().equals(FlexKeyword.OVERRIDE.getValue())) {
          return true;
        }
      }
    }
    return false;
  }
}
