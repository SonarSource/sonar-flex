/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2024 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.flex.checks.utils;

import com.sonar.sslr.api.AstNode;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.sonar.flex.FlexGrammar;

public final class Clazz {

  private Clazz() {
  }

  public static List<AstNode> getDirectives(AstNode classDefNode) {
    Preconditions.checkState(classDefNode.is(FlexGrammar.CLASS_DEF));
    return classDefNode
      .getFirstChild(FlexGrammar.BLOCK)
      .getFirstChild(FlexGrammar.DIRECTIVES)
      .getChildren(FlexGrammar.DIRECTIVE);
  }

  public static List<AstNode> getFields(AstNode classDefNode) {
    Preconditions.checkState(classDefNode.is(FlexGrammar.CLASS_DEF));
    List<AstNode> fields = new ArrayList<>();

    for (AstNode directive : classDefNode.getFirstChild(FlexGrammar.BLOCK).getFirstChild(FlexGrammar.DIRECTIVES).getChildren()) {
      AstNode fieldDef = getFieldDefinition(directive);
      if (fieldDef != null) {
        fields.add(fieldDef);
      }
    }
    return fields;
  }

  public static String getName(AstNode classDefNode) {
    Preconditions.checkState(classDefNode.is(FlexGrammar.CLASS_DEF));
    return classDefNode.getFirstChild(FlexGrammar.CLASS_NAME)
      .getFirstChild(FlexGrammar.CLASS_IDENTIFIERS)
      .getLastChild()
      .getTokenValue();
  }

  public static AstNode getConstructor(AstNode classDefNode) {
    Preconditions.checkState(classDefNode.is(FlexGrammar.CLASS_DEF));
    final String className = Clazz.getName(classDefNode);

    for (AstNode directive : classDefNode.getFirstChild(FlexGrammar.BLOCK).getFirstChild(FlexGrammar.DIRECTIVES).getChildren()) {
      AstNode functionDef = getFunctionDefinition(directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE));

      if (functionDef != null && Function.isConstructor(functionDef, className)) {
        return functionDef;
      }
    }

    return null;
  }

  private static AstNode getFieldDefinition(AstNode directive) {
    Preconditions.checkState(directive.is(FlexGrammar.DIRECTIVE));
    AstNode annotableDir = directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE);
    return annotableDir == null ? null : annotableDir.getFirstChild(FlexGrammar.VARIABLE_DECLARATION_STATEMENT);
  }

  private static AstNode getFunctionDefinition(@Nullable AstNode annotableDir) {
    return annotableDir != null
      && annotableDir.is(FlexGrammar.ANNOTABLE_DIRECTIVE)
      && annotableDir.getFirstChild().is(FlexGrammar.FUNCTION_DEF) ? annotableDir.getFirstChild() : null;
  }

  public static List<AstNode> getFunctions(AstNode classDefNode) {
    Preconditions.checkState(classDefNode.is(FlexGrammar.CLASS_DEF));
    List<AstNode> functions = new ArrayList<>();

    for (AstNode directive : classDefNode.getFirstChild(FlexGrammar.BLOCK).getFirstChild(FlexGrammar.DIRECTIVES).getChildren()) {
      AstNode functionDef = getFunctionDefinition(directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE));

      if (functionDef != null) {
        functions.add(functionDef);
      }
    }
    return functions;
  }

}
