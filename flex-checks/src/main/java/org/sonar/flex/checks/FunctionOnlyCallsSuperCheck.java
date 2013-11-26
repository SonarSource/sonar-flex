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

import com.google.common.base.Joiner;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.squid.checks.SquidCheck;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.ArrayList;
import java.util.List;

@Rule(
  key = "S1185",
  priority = Priority.MINOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MINOR)
public class FunctionOnlyCallsSuperCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.FUNCTION_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (!isOverridingParentFunction(astNode.getPreviousAstNode())) {
      return;
    }
    AstNode singleDirectiveNode = getSingleStatementBlock(astNode);

    if (singleDirectiveNode != null && isSuperOrReturnOfSuperReference(singleDirectiveNode.getFirstChild())) {
      List<String> parameters = getParameters(astNode);
      String methodName = astNode
        .getFirstChild(FlexGrammar.FUNCTION_NAME)
        .getFirstChild(FlexGrammar.IDENTIFIER)
        .getTokenValue();

      if (isUselessCallToSuper(singleDirectiveNode.getFirstChild(FlexGrammar.STATEMENT), methodName, parameters)
        && !hasMetadataTag(astNode.getParent().getParent().getPreviousAstNode())) {
        getContext().createLineViolation(this, "Remove this method \"{0}\" to simply inherit it.", astNode, methodName);
      }
    }
  }

  private static boolean hasMetadataTag(AstNode directive) {
    return directive.getFirstChild().is(FlexGrammar.STATEMENT)
      && directive.getFirstChild().getFirstChild().is(FlexGrammar.METADATA_STATEMENT);
  }

  private static AstNode getSingleStatementBlock(AstNode functionDef) {
    AstNode functionBlock = functionDef.getFirstChild(FlexGrammar.FUNCTION_COMMON).getFirstChild(FlexGrammar.BLOCK);

    if (functionBlock != null && functionBlock.getFirstChild(FlexGrammar.DIRECTIVES).getNumberOfChildren() == 1) {
      return functionBlock.getFirstChild(FlexGrammar.DIRECTIVES).getFirstChild();
    }
    return null;
  }

  private static boolean isSuperReference(AstNode statement) {
    return statement.getFirstChild(FlexGrammar.EXPRESSION_STATEMENT) != null
      && isSuperExpression(statement.getFirstChild().getFirstChild());
  }

  private static boolean isSuperExpression(AstNode listExpression) {
    AstNode postfixExpr = listExpression.getFirstChild(FlexGrammar.ASSIGNMENT_EXPR).getFirstChild(FlexGrammar.POSTFIX_EXPR);

    return postfixExpr != null && postfixExpr.getFirstChild(FlexGrammar.SUPER_EXPR) != null;
  }

  private static boolean isReturnOfSuperReference(AstNode statement) {
    return statement.getFirstChild().is(FlexGrammar.RETURN_STATEMENT)
      && statement.getFirstChild().getFirstChild(FlexGrammar.LIST_EXPRESSION) != null
      && isSuperExpression(statement.getFirstChild().getFirstChild(FlexGrammar.LIST_EXPRESSION));
  }

  private static boolean isSuperOrReturnOfSuperReference(AstNode directiveChild) {
    return directiveChild.is(FlexGrammar.STATEMENT) && (isSuperReference(directiveChild) || isReturnOfSuperReference(directiveChild));


  }

  private static boolean isOverridingParentFunction(AstNode attributesNode) {
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

  private static List<String> getParameters(AstNode functionDef) {
    List<String> parameters = new ArrayList<String>();
    AstNode parametersNode = functionDef
      .getFirstChild(FlexGrammar.FUNCTION_COMMON)
      .getFirstChild(FlexGrammar.FUNCTION_SIGNATURE)
      .getFirstChild(FlexGrammar.PARAMETERS);

    if (parametersNode != null) {

      for (AstNode parameter : parametersNode.getChildren(FlexGrammar.PARAMETER, FlexGrammar.REST_PARAMETERS)) {
        if (parameter.getFirstChild(FlexGrammar.TYPED_IDENTIFIER) != null) {
          parameters.add(parameter
            .getFirstChild(FlexGrammar.TYPED_IDENTIFIER)
            .getFirstChild(FlexGrammar.IDENTIFIER)
            .getTokenValue());
        }
      }
    }
    return parameters;
  }

  private static boolean isUselessCallToSuper(AstNode singleStatement, String methodName, List<String> paramaters) {
    StringBuilder sb = new StringBuilder();

    for (Token token : singleStatement.getFirstChild().getTokens()) {
      sb.append(token.getValue());
    }

    String actual = sb.toString();
    String expected = FlexKeyword.SUPER.getValue() + "." + methodName + "(" + Joiner.on(",").join(paramaters) + ")";

    return actual.equals(expected) || actual.equals(FlexKeyword.RETURN.getValue() + expected + ";");
  }
}
