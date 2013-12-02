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

import com.google.common.collect.Lists;
import com.sonar.sslr.api.AstNode;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;

import java.util.List;

public class Function {

  private Function() {
  }

  public static boolean isAccessor(AstNode functionDef) {
    return functionDef.getFirstChild(FlexGrammar.FUNCTION_NAME).getFirstChild(FlexKeyword.GET, FlexKeyword.SET) != null;
  }


  public static boolean isEmptyConstructor(AstNode functionDef, String className) {
    AstNode functionBlock = functionDef.getFirstChild(FlexGrammar.FUNCTION_COMMON).getFirstChild(FlexGrammar.BLOCK);
    return isConstructor(functionDef, className)
      && (functionBlock == null || functionBlock.getFirstChild(FlexGrammar.DIRECTIVES).getChildren().isEmpty());
  }

  public static boolean isConstructor(AstNode functionDef, String className) {
    return functionDef.getFirstChild(FlexGrammar.FUNCTION_NAME).getNumberOfChildren() == 1
      && functionDef.getFirstChild(FlexGrammar.FUNCTION_NAME).getFirstChild().getTokenValue().equals(className);
  }


  public static List<String> getParametersNames(AstNode functionDef) {
    List<String> parametersNames = Lists.newArrayList();
    AstNode parameters = functionDef
      .getFirstChild(FlexGrammar.FUNCTION_COMMON)
      .getFirstChild(FlexGrammar.FUNCTION_SIGNATURE)
      .getFirstChild(FlexGrammar.PARAMETERS);

    if (parameters != null) {
      for (AstNode parameter : parameters.getChildren(FlexGrammar.PARAMETER)) {
        parametersNames.add(parameter.getFirstChild(FlexGrammar.TYPED_IDENTIFIER).getFirstChild(FlexGrammar.IDENTIFIER).getTokenValue());
      }
    }
    return parametersNames;
  }
}
