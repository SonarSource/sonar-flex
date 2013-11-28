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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetadataTag {

  public static boolean isTag(AstNode metadata, String tagName) {
    if (isNotEmpty(metadata)) {
      AstNode postfixExpr = metadata
        .getFirstChild(FlexGrammar.ASSIGNMENT_EXPR)
        .getFirstChild(FlexGrammar.POSTFIX_EXPR);

      return postfixExpr != null && tagName.equals(postfixExpr.getTokenValue());
    }
    return false;
  }

  // [Metadata("property, in, one, string")] --> [property, in, one, string]
  public static List<String> getSinglePropertyAsList(AstNode metadata) {
    if (isNotEmpty(metadata) && hasProperty(metadata)) {
      AstNode properties = metadata
        .getFirstChild(FlexGrammar.ASSIGNMENT_EXPR)
        .getFirstChild(FlexGrammar.POSTFIX_EXPR)
        .getFirstChild(FlexGrammar.ARGUMENTS)
        .getFirstChild(FlexGrammar.LIST_EXPRESSION);

      if (properties.getNumberOfChildren() == 1) {
        String singleProperty = properties.getFirstChild(FlexGrammar.ASSIGNMENT_EXPR).getTokenValue();
        List<String> propertyList = new ArrayList<String>();

        for (String property : singleProperty.substring(1, singleProperty.length() - 1).split(",")) {
          propertyList.add(property.trim());
        }
        return propertyList;
      }
    }
    return null;
  }

  public static Map<String, String> getTagPropertiesMap(AstNode metadata) {
    if (isNotEmpty(metadata) && hasProperty(metadata)) {

      Map<String, String> properties = new HashMap<String, String>();
      AstNode listExpr = metadata
        .getFirstChild(FlexGrammar.ASSIGNMENT_EXPR)
        .getFirstChild(FlexGrammar.POSTFIX_EXPR)
        .getFirstChild(FlexGrammar.ARGUMENTS)
        .getFirstChild(FlexGrammar.LIST_EXPRESSION);

      for (AstNode assignmentExpr : listExpr.getChildren(FlexGrammar.ASSIGNMENT_EXPR)) {
        if (assignmentExpr.getFirstChild(FlexGrammar.ASSIGNMENT_OPERATOR) != null) {
          properties.put(assignmentExpr.getFirstChild(FlexGrammar.ASSIGNMENT_OPERATOR).getPreviousSibling().getTokenValue(),
            assignmentExpr.getFirstChild(FlexGrammar.ASSIGNMENT_OPERATOR).getNextSibling().getTokenValue());
        }
      }
      return properties;
    }
    return null;
  }

  public static boolean isNotEmpty(AstNode metadata) {
    return metadata.getFirstChild(FlexGrammar.ASSIGNMENT_EXPR) != null
      && metadata.getFirstChild(FlexGrammar.ASSIGNMENT_EXPR).getFirstChild(FlexGrammar.POSTFIX_EXPR) != null;
  }

  public static boolean hasProperty(AstNode metadata) {
    AstNode arguments = metadata
      .getFirstChild(FlexGrammar.ASSIGNMENT_EXPR)
      .getFirstChild(FlexGrammar.POSTFIX_EXPR)
      .getFirstChild(FlexGrammar.ARGUMENTS);
    return arguments != null && arguments.getFirstChild(FlexGrammar.LIST_EXPRESSION) != null;
  }
}
