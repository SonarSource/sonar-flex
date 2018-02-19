/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2018 SonarSource SA
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

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Token;
import java.text.MessageFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Function;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "S1172",
  name = "Unused function parameters should be removed",
  priority = Priority.MAJOR,
  tags = {Tags.MISRA, Tags.UNUSED})
@ActivatedByDefault
@SqaleConstantRemediation("5min")
public class UnusedFunctionParametersCheck extends FlexCheck {

  private Deque<Boolean> classes = new ArrayDeque<>();

  private static class Scope {
    private final Scope outerScope;
    private final AstNode functionDec;
    private final Map<String, Integer> arguments;

    public Scope(Scope outerScope, AstNode functionDec) {
      this.outerScope = outerScope;
      this.functionDec = functionDec;
      this.arguments = Maps.newLinkedHashMap();
    }

    private void declare(AstNode astNode) {
      Preconditions.checkState(astNode.is(FlexGrammar.IDENTIFIER));
      String identifier = astNode.getTokenValue();
      arguments.put(identifier, 0);
    }

    private void use(String value) {
      Scope scope = this;
      while (scope != null) {
        Integer usage = scope.arguments.get(value);
        if (usage != null) {
          usage++;
          scope.arguments.put(value, usage);
          return;
        }
        scope = scope.outerScope;
      }
    }
  }

  private static final AstNodeType[] FUNCTION_NODES = {FlexGrammar.FUNCTION_DEF, FlexGrammar.FUNCTION_EXPR};
  private Scope currentScope;

  @Override
  public List<AstNodeType> subscribedTo() {
    List<AstNodeType> types = new ArrayList<>();
    types.add(FlexGrammar.POSTFIX_EXPR);
    types.add(FlexGrammar.PARAMETERS);
    types.add(FlexGrammar.CLASS_DEF);
    Collections.addAll(types, FUNCTION_NODES);
    return types;
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    currentScope = null;
    classes.clear();
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.CLASS_DEF)) {
      classes.push(implementsAnInterface(astNode));
    } else if (astNode.is(FUNCTION_NODES)) {
      // enter new scope
      currentScope = new Scope(currentScope, astNode);

    } else if (currentScope != null && astNode.is(FlexGrammar.PARAMETERS) && astNode.getParent().is(FlexGrammar.FUNCTION_SIGNATURE)) {
      declareInCurrentScope(Function.getParametersIdentifiers(currentScope.functionDec));

    } else if (currentScope != null && astNode.is(FlexGrammar.POSTFIX_EXPR)) {
      AstNode postfixExprChild = astNode.getFirstChild();
      // check if it is not a call to function with same name than the parameter
      if (postfixExprChild.is(FlexGrammar.PRIMARY_EXPR) && postfixExprChild.getNextAstNode().isNot(FlexGrammar.ARGUMENTS)) {
        currentScope.use(getPrimaryExpressionStringValue(postfixExprChild));
      }
    }
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.is(FUNCTION_NODES) && isNotAbstract(astNode)) {
      // leave scope
      if (!isExcluded(astNode)) {
        reportUnusedArgument();
      }
      currentScope = currentScope.outerScope;
    } else if (astNode.is(FlexGrammar.CLASS_DEF)) {
      classes.pop();
    }
  }

  private void reportUnusedArgument() {
    int nbUnusedArgs = 0;
    StringBuilder builder = new StringBuilder();

    for (Map.Entry<String, Integer> entry : currentScope.arguments.entrySet()) {
      if (entry.getValue() == 0) {
        builder.append(entry.getKey() + " ");
        nbUnusedArgs++;
      }
    }

    if (nbUnusedArgs > 0) {
      addIssue(
        MessageFormat.format(
          "Remove the unused function {0} \"{1}\".", nbUnusedArgs > 1 ? "parameters" : "parameter",
          StringUtils.join(builder.toString().trim().split(" "), ", ")),
        currentScope.functionDec);
    }
  }

  private boolean isExcluded(AstNode functionDec) {
    AstNode directives = functionDec
      .getFirstChild(FlexGrammar.FUNCTION_COMMON)
      .getFirstChild(FlexGrammar.BLOCK)
      .getFirstChild(FlexGrammar.DIRECTIVES);

    return isExcludedFunctionDeclaration(functionDec) || isEmpty(directives)
      || containsOnlyThrowStmt(directives) || isInClassImplementingInterface();
  }

  private static Boolean implementsAnInterface(AstNode classDef) {
    AstNode inheritenceNode = classDef.getFirstChild(FlexGrammar.INHERITENCE);
    return inheritenceNode != null && inheritenceNode.getFirstChild().is(FlexKeyword.IMPLEMENTS);
  }

  private boolean isInClassImplementingInterface() {
    return !classes.isEmpty() && classes.peek();
  }

  private static boolean containsOnlyThrowStmt(AstNode directives) {
    List<AstNode> directiveList = directives.getChildren();

    if (directiveList.size() == 1) {
      AstNode directiveKind = directiveList.get(0).getFirstChild().getFirstChild();
      return directiveKind.is(FlexGrammar.THROW_STATEMENT);
    }
    return false;
  }

  private static boolean isEmpty(AstNode directives) {
    return directives.getNumberOfChildren() == 0;
  }

  private void declareInCurrentScope(List<AstNode> identifiers) {
    for (AstNode identifier : identifiers) {
      currentScope.declare(identifier);
    }
  }

  private static boolean isExcludedFunctionDeclaration(AstNode functionDec) {
    return functionDec.is(FlexGrammar.FUNCTION_DEF) && (Function.isOverriding(functionDec) || isEventHandler(functionDec));
  }

  private static boolean isEventHandler(AstNode functionDec) {
    String functionName = functionDec.getFirstChild(FlexGrammar.FUNCTION_NAME).getTokenValue();

    if (functionName.toLowerCase(Locale.ENGLISH).contains("handle") || startsWithOnPreposition(functionName)) {
      AstNode parameters = functionDec
        .getFirstChild(FlexGrammar.FUNCTION_COMMON)
        .getFirstChild(FlexGrammar.FUNCTION_SIGNATURE)
        .getFirstChild(FlexGrammar.PARAMETERS);

      if (parameters != null) {
        AstNode firstParameter = parameters.getFirstChild(FlexGrammar.PARAMETER);

        if (firstParameter != null && firstParameter.getFirstChild(FlexGrammar.TYPED_IDENTIFIER) != null) {
          AstNode firstParameterType = firstParameter
            .getFirstChild(FlexGrammar.TYPED_IDENTIFIER)
            .getFirstChild(FlexGrammar.TYPE_EXPR);
          return firstParameterType != null && firstParameterType.getLastToken().getValue().endsWith("Event");
        }
      }
    }
    return false;
  }

  private static boolean startsWithOnPreposition(String name) {
    return name.startsWith("on") && (name.length() == 2 || name.substring(2, 3).matches("[A-Z]"));
  }

  private static boolean isNotAbstract(AstNode functionDef) {
    return functionDef.getFirstChild(FlexGrammar.FUNCTION_COMMON).getLastChild().is(FlexGrammar.BLOCK);
  }

  private static String getPrimaryExpressionStringValue(AstNode postfixExpr) {
    StringBuilder builder = new StringBuilder();
    for (Token t : postfixExpr.getTokens()) {
      builder.append(t.getValue());
    }
    return builder.toString();
  }

}
