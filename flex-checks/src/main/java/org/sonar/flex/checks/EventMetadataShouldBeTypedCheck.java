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
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S1463",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class EventMetadataShouldBeTypedCheck extends SquidCheck<LexerlessGrammar> {

  private static class EventProperty {
    String name = null;
    boolean isTypeDefined = false;
  }

  @Override
  public void init() {
    subscribeTo(FlexGrammar.METADATA_STATEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    AstNode eventNode = getEventTag(astNode.getFirstChild(FlexGrammar.ARRAY_INITIALISER).getFirstChild(FlexGrammar.ELEMENT_LIST));

    if (eventNode != null) {
      EventProperty eventProperty = getEventProperty(eventNode.getFirstChild(FlexGrammar.ARGUMENTS));

      if (eventProperty != null && !eventProperty.isTypeDefined) {
        getContext().createLineViolation(this, "The {0} event type is missing in this metadata tag", astNode, eventProperty.name);
      }
    }

  }

  private static AstNode getEventTag(AstNode elementList) {
    if (elementList != null) {

      for (AstNode literalElement : elementList.getChildren(FlexGrammar.LITERAL_ELEMENT)) {
        AstNode postfixExpr = literalElement.getFirstChild().getFirstChild(FlexGrammar.POSTFIX_EXPR);

        if (isTagEvent(postfixExpr)) {
          return postfixExpr;
        }
      }
    }
    return null;
  }

  private static boolean isTagEvent(AstNode postfixExpr) {
    return postfixExpr != null && "Event".equals(postfixExpr.getTokenValue());
  }

  private static EventProperty getEventProperty(AstNode arguments) {
    if (arguments == null || arguments.getFirstChild(FlexGrammar.LIST_EXPRESSION) == null) {
      return null;
    }

    EventProperty eventProperty = new EventProperty();

    for (AstNode assignmentExpr : arguments.getFirstChild(FlexGrammar.LIST_EXPRESSION).getChildren(FlexGrammar.ASSIGNMENT_EXPR)) {
      AstNode postfixExpr = assignmentExpr.getFirstChild(FlexGrammar.POSTFIX_EXPR);

      if (isProperty(postfixExpr, "type")) {
        eventProperty.isTypeDefined = true;
      } else if (isProperty(postfixExpr, "name")) {
        eventProperty.name = assignmentExpr.getLastChild().getTokenValue();
      }
    }
    return eventProperty;
  }

  private static boolean isProperty(AstNode postfixExpr, String name) {
    return postfixExpr != null && name.equals(postfixExpr.getTokenValue());
  }

}
