/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource Sàrl.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Token;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Function;

@Rule(key = "S1185")
public class FunctionOnlyCallsSuperCheck extends FlexCheck {

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.FUNCTION_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (!isOverridingParentFunction(astNode.getPreviousAstNode())) {
      return;
    }
    AstNode singleDirectiveNode = getSingleStatementBlock(astNode);

    if (singleDirectiveNode != null && isSuperOrReturnOfSuperReference(singleDirectiveNode.getFirstChild())) {
      List<String> parameters = getParametersName(astNode);
      String methodName = Function.getName(astNode);

      if (isUselessCallToSuper(singleDirectiveNode.getFirstChild(FlexGrammar.STATEMENT), methodName, parameters)
        && !hasMetadataTag(astNode.getParent().getParent().getPreviousAstNode())) {
        addIssue(MessageFormat.format("Remove this method \"{0}\" to simply inherit it.", methodName), astNode);
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
      && isSuperExpression(statement.getFirstChild().getFirstChild().getFirstChild());
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

  private static List<String> getParametersName(AstNode functionDef) {
    List<String> parametersNames = new ArrayList<>();
    for (AstNode identifier : Function.getParametersIdentifiers(functionDef)) {
      parametersNames.add(identifier.getTokenValue());
    }
    return parametersNames;
  }

  private static boolean isOverridingParentFunction(@Nullable AstNode attributesNode) {
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

  private static boolean isUselessCallToSuper(AstNode singleStatement, String methodName, List<String> parameters) {
    StringBuilder sb = new StringBuilder();

    for (Token token : singleStatement.getFirstChild().getTokens()) {
      sb.append(token.getValue());
    }

    String actual = sb.toString();
    String expected = FlexKeyword.SUPER.getValue() + "." + methodName + "(" + String.join(",", parameters) + ");";

    return actual.equals(expected) || actual.equals(FlexKeyword.RETURN.getValue() + expected);
  }
}
