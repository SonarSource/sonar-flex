/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sonar.flex.FlexGrammar;

public final class MetadataTag {

  private MetadataTag() {
  }

  public static boolean isTag(AstNode metadata, String tagName) {
    Preconditions.checkState(metadata.is(FlexGrammar.METADATA_STATEMENT));
    if (isNotEmpty(metadata)) {
      AstNode postfixExpr = metadata
        .getFirstChild(FlexGrammar.ASSIGNMENT_EXPR)
        .getFirstChild(FlexGrammar.POSTFIX_EXPR);

      return postfixExpr != null && tagName.equals(postfixExpr.getTokenValue());
    }
    return false;
  }

  // [Metadata("property, in, one, string")] --> [property, in, one, string]
  // or
  // [Metadata(tag="property, in, one, string")] --> [property, in, one, string]
  public static List<String> getSinglePropertyAsList(AstNode metadata) {
    Preconditions.checkState(metadata.is(FlexGrammar.METADATA_STATEMENT));
    List<String> propertyList = new ArrayList<>();

    if (isNotEmpty(metadata) && hasProperty(metadata)) {
      AstNode properties = metadata
        .getFirstChild(FlexGrammar.ASSIGNMENT_EXPR)
        .getFirstChild(FlexGrammar.POSTFIX_EXPR)
        .getFirstChild(FlexGrammar.ARGUMENTS)
        .getFirstChild(FlexGrammar.LIST_EXPRESSION);

      if (properties.getNumberOfChildren() == 1) {
        AstNode assignmentExpr = properties.getFirstChild(FlexGrammar.ASSIGNMENT_EXPR);
        if (assignmentExpr.getNumberOfChildren() > 1) {
          // Case where assignment expr contains an assignment operation
          assignmentExpr = assignmentExpr.getFirstChild(FlexGrammar.ASSIGNMENT_EXPR);
        }
        String singleProperty = assignmentExpr.getTokenValue();
        for (String property : singleProperty.substring(1, singleProperty.length() - 1).split(",")) {
          propertyList.add(property.trim());
        }
      }
    }
    return propertyList;
  }

  public static Map<String, String> getTagPropertiesMap(AstNode metadata) {
    Preconditions.checkState(metadata.is(FlexGrammar.METADATA_STATEMENT));
    if (isNotEmpty(metadata) && hasProperty(metadata)) {

      Map<String, String> properties = new HashMap<>();
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
    Preconditions.checkState(metadata.is(FlexGrammar.METADATA_STATEMENT));
    AstNode arguments = metadata
      .getFirstChild(FlexGrammar.ASSIGNMENT_EXPR)
      .getFirstChild(FlexGrammar.POSTFIX_EXPR)
      .getFirstChild(FlexGrammar.ARGUMENTS);
    return arguments != null && arguments.getFirstChild(FlexGrammar.LIST_EXPRESSION) != null;
  }

  public static boolean isMetadataTag(AstNode directive) {
    return directive.getFirstChild().is(FlexGrammar.STATEMENT)
      && directive.getFirstChild().getFirstChild().is(FlexGrammar.METADATA_STATEMENT);
  }

}
