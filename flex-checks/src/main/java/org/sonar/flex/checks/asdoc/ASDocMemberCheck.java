/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2017 SonarSource SA
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
package org.sonar.flex.checks.asdoc;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Trivia;
import org.apache.commons.lang.StringUtils;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.ASDocCheck;
import org.sonar.flex.checks.utils.Function;
import org.sonar.flex.checks.utils.Modifiers;

import java.util.List;
import java.util.Set;

public class ASDocMemberCheck {

  public static class MethodASDoc {
    Set<String> parameters = Sets.newHashSet();
    boolean hasReturn = false;

    public boolean isParameterDocumented(String paramName) {
      for (String param : parameters) {
        if (param.equalsIgnoreCase(paramName)) {
          return true;
        }
      }
      return false;
    }
  }

  public void visitNode(ASDocCheck check, List<AstNode> classDirectives) {
    checkMember(check, classDirectives);
  }

  private static void checkMember(ASDocCheck check, List<AstNode> classDirectives) {
    for (AstNode directive : classDirectives) {
      AstNode annotableDirective = directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE);

      if (annotableDirective != null && Modifiers.isPublic(annotableDirective.getPreviousAstNode())) {
        AstNode annotableDirChild = annotableDirective.getFirstChild();

        // Fields
        if (check.properties && annotableDirChild.is(FlexGrammar.VARIABLE_DECLARATION_STATEMENT)) {
          checkField(check, getTrivia(directive), annotableDirChild);

          // Methods
        } else if (check.methods && annotableDirChild.is(FlexGrammar.FUNCTION_DEF)) {
          checkMethod(check, getTrivia(directive), annotableDirChild);
        }
      }
    }
  }


  /**
   * Returns class member trivia.
   * Handles case when there is metadata tag before method or field.
   */
  private static List<Trivia> getTrivia(AstNode directive) {
    // If there is no metatdata right before the method or field declaration
    if (directive.getToken().hasTrivia()) {
      return directive.getToken().getTrivia();
    }

    AstNode current = directive;
    AstNode previousNode = directive.getPreviousAstNode();
    while (isMetadata(previousNode)) {
      current = previousNode;
      previousNode = previousNode.getPreviousAstNode();
    }
    return current.getToken().getTrivia();
  }

  private static boolean isMetadata(AstNode directive) {
    AstNode statementKind = directive.getFirstChild().getFirstChild();
    return statementKind != null && statementKind.is(FlexGrammar.METADATA_STATEMENT);
  }

  /**
   * Verifies that an ASDoc is present above the field declaration.
   */
  private static void checkField(ASDocCheck check, List<Trivia> trivia, AstNode variableDec) {
    if (!check.hasASDoc(trivia) && !check.containsOnOfTags(trivia, ASDocCheck.PRIVATE_TAG, ASDocCheck.INHERIT_TAG)) {
      check.getContext().createLineViolation(check, "Add the missing ASDoc for this field declaration.", variableDec);
    }
  }

  /**
   * <ul> Verifies:
   * <li> presence of ASDoc above the method declaration
   * <li> presence of @return tag in ASDoc
   * <li> presence of @param tag in ASDoc
   * </ul>
   */
  private static void checkMethod(ASDocCheck check, List<Trivia> trivia, AstNode functionDef) {
    if (check.containsOnOfTags(trivia, ASDocCheck.PRIVATE_TAG, ASDocCheck.INHERIT_TAG)) {
      // skip all documentation check if has @private tag
      return;
    }
    // General missing ASDoc for the method
    if (!check.hasASDoc(trivia)) {
      check.getContext().createLineViolation(check, "Add the missing ASDoc for this method.", functionDef);
    } else {
      MethodASDoc methodASDoc = parseASDoc(trivia);

      if (check.methodReturn) {
        checkForReturnASDoc(check, methodASDoc, functionDef);
      }
      if (check.methodParams) {
        checkForParametersASDoc(check, methodASDoc, functionDef);
      }
    }
  }

  /**
   * Report an issue if the method as a non-void return type and "@return" tag is not present in the ASDoc
   */
  private static void checkForReturnASDoc(ASDocCheck check, MethodASDoc methodASDoc, AstNode functionDef) {
    if (!returnsVoid(functionDef) && !methodASDoc.hasReturn) {
      check.getContext().createLineViolation(check, "Add the missing \"@return\" ASDoc for the return value of this method.", functionDef);
    }
  }

  /**
   * Verifies that for every method's parameter a "@param" tag followed by the parameter's name
   * is present in the ASDoc.
   */
  private static void checkForParametersASDoc(ASDocCheck check, MethodASDoc methodASDoc, AstNode functionDef) {
    StringBuilder builder = new StringBuilder();

    for (AstNode parameter : Function.getParametersIdentifiers(functionDef)) {
      String paramValue = parameter.getTokenValue();

      if (!methodASDoc.isParameterDocumented(paramValue)) {
        builder.append(paramValue).append(", ");
      }
    }

    if (builder.length() > 0) {
      check.getContext().createLineViolation(check, "Add the missing \"@param\" ASDoc for: {0}.", functionDef, StringUtils.chop(builder.toString().trim()));
    }
  }

  private static MethodASDoc parseASDoc(List<Trivia> trivia) {
    MethodASDoc methodASDoc = new MethodASDoc();

    for (Trivia comment : trivia) {
      for (String line : comment.getToken().getValue().trim().split("(?:\r)?\n|\r")) {
        parseLine(line.trim().split(" "), methodASDoc);
      }
    }
    return methodASDoc;
  }

  private static void parseLine(String[] line, MethodASDoc methodASDoc) {
    int lineLength = line.length;
    for (int i = 0, next = 1; i < lineLength; i++, next++) {

      if ("@param".equals(line[i]) && next < lineLength) {
        methodASDoc.parameters.add(getParamName(line[next]));

      } else if ("@return".equals(line[i])) {
        methodASDoc.hasReturn = true;
      }
    }
  }


  private static boolean returnsVoid(AstNode functionDef) {
    AstNode returnType = functionDef
      .getFirstChild(FlexGrammar.FUNCTION_COMMON)
      .getFirstChild(FlexGrammar.FUNCTION_SIGNATURE)
      .getFirstChild(FlexGrammar.RESULT_TYPE);

    if (returnType == null) {
      return true;
    }
    return returnType.getLastChild().is(FlexKeyword.VOID) ? true : false;
  }

  private static String getParamName(String paramDoc) {
    if (!paramDoc.isEmpty()) {
      return paramDoc.split(":")[0];
    }
    return paramDoc;
  }

}
