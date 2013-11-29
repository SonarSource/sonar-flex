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

import com.sonar.sslr.api.AstNode;
import org.sonar.flex.FlexGrammar;

public class Clazz {

  private Clazz () {
  }

  public static AstNode getConstructor(AstNode classDefNode) {
    final String className = classDefNode.getFirstChild(FlexGrammar.CLASS_NAME)
      .getFirstChild(FlexGrammar.CLASS_IDENTIFIERS)
      .getLastChild()
      .getTokenValue();

    for (AstNode directive : classDefNode.getFirstChild(FlexGrammar.BLOCK).getFirstChild(FlexGrammar.DIRECTIVES).getChildren()) {
      AstNode functionDef = getFunctionDefinition(directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE));

      if (functionDef != null && isConstructor(functionDef, className)) {
        return functionDef;
      }
    }

    return null;
  }

  public static boolean isConstructor(AstNode functionDef, String className) {
    return functionDef.getFirstChild(FlexGrammar.FUNCTION_NAME).getNumberOfChildren() == 1
      && functionDef.getFirstChild(FlexGrammar.FUNCTION_NAME).getFirstChild().getTokenValue().equals(className);
  }

  private static AstNode getFunctionDefinition(AstNode annotableDir) {
    return annotableDir != null && annotableDir.getFirstChild().is(FlexGrammar.FUNCTION_DEF) ? annotableDir.getFirstChild() : null;
  }
}
