/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2020 SonarSource SA
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
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Function;

@Rule(key = "S1470")
public class OverrideEventCloneFunctionCheck extends FlexCheck {

  private static final String EVENT_TYPE_NAME = "Event";

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (!isExtendingEvent(astNode)) {
      return;
    }
    List<AstNode> classDirectives = astNode
      .getFirstChild(FlexGrammar.BLOCK)
      .getFirstChild(FlexGrammar.DIRECTIVES)
      .getChildren(FlexGrammar.DIRECTIVE);

    for (AstNode directive : classDirectives) {
      if (isOverridingFunction(directive) && isCloneFunction(directive)) {
        return;
      }
    }

    String className = astNode.getFirstChild(FlexGrammar.CLASS_NAME).getFirstChild(FlexGrammar.CLASS_IDENTIFIERS).getLastChild().getTokenValue();
    addIssue(MessageFormat.format("Make this class \"{0}\" override \"Event.clone()\" function.", className), astNode);
  }

  private static boolean isCloneFunction(AstNode directive) {
    AstNode functionDef = directive
      .getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE)
      .getFirstChild(FlexGrammar.FUNCTION_DEF);

    String functionName = Function.getName(functionDef);

    return "clone".equals(functionName) && EVENT_TYPE_NAME.equals(getResultType(functionDef));
  }

  private static String getResultType(AstNode functionDef) {
    AstNode resultType = functionDef
      .getFirstChild(FlexGrammar.FUNCTION_COMMON)
      .getFirstChild(FlexGrammar.FUNCTION_SIGNATURE)
      .getFirstChild(FlexGrammar.RESULT_TYPE);

    if (resultType != null && resultType.getFirstChild(FlexGrammar.TYPE_EXPR) != null) {
      return resultType.getFirstChild(FlexGrammar.TYPE_EXPR).getTokenValue();
    }
    return null;
  }


  private static boolean isExtendingEvent(AstNode classDef) {
    AstNode inheritenceNode = classDef.getFirstChild(FlexGrammar.INHERITENCE);

    if (inheritenceNode != null && inheritenceNode.getFirstChild(FlexKeyword.EXTENDS) != null) {
      AstNode qualifiedId = inheritenceNode.getFirstChild(FlexGrammar.TYPE_EXPR).getLastChild();
      if (qualifiedId.is(FlexGrammar.QUALIFIED_IDENTIFIER) && EVENT_TYPE_NAME.equals(qualifiedId.getTokenValue())) {
        return true;
      }
    }
    return false;
  }

  private static boolean isOverridingFunction(AstNode directive) {
    return isFunctionWithAttributes(directive) && isOverriding(directive);
  }

  private static boolean isFunctionWithAttributes(AstNode directive) {
    return directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE) != null
      && directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE).getFirstChild().is(FlexGrammar.FUNCTION_DEF)
      && directive.getFirstChild(FlexGrammar.ATTRIBUTES) != null;
  }

  private static boolean isOverriding(AstNode directive) {
    for (AstNode attribute : directive.getFirstChild(FlexGrammar.ATTRIBUTES).getChildren()) {

      if (attribute.getFirstChild().is(FlexGrammar.ATTRIBUTE_EXPR)
        && attribute.getFirstChild().getNumberOfChildren() == 1
        && attribute.getFirstChild().getFirstChild(FlexGrammar.IDENTIFIER).getTokenValue().equals(FlexKeyword.OVERRIDE.getValue())) {
        return true;
      }
    }
    return false;
  }
}
