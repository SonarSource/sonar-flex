/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2024 SonarSource SA
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
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Modifiers;

@Rule(key = "S1312")
public class PrivateStaticConstLoggerCheck extends FlexCheck {

  private static final String DEFAULT = "LOG(?:GER)?";
  private Pattern pattern = null;

  @RuleProperty(
    key = "format",
    description = "Regular expression used to check the logger names against.",
    defaultValue = DEFAULT)
  String format = DEFAULT;

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    if (pattern == null) {
      pattern = Pattern.compile(format);
    }
  }

  @Override
  public void visitNode(AstNode astNode) {
    for (AstNode directive : Clazz.getDirectives(astNode)) {

      if (isVariableDeclaration(directive)) {
        AstNode variableDef = directive
          .getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE)
          .getFirstChild(FlexGrammar.VARIABLE_DECLARATION_STATEMENT)
          .getFirstChild(FlexGrammar.VARIABLE_DEF);

        visitVariableDefinition(directive, variableDef);
      }
    }
  }

  private void visitVariableDefinition(AstNode directive, AstNode variableDef) {
    for (AstNode variableBindingNode : variableDef.getFirstChild(FlexGrammar.VARIABLE_BINDING_LIST).getChildren(FlexGrammar.VARIABLE_BINDING)) {
      if (isILogger(variableBindingNode)) {
        AstNode identifierNode = variableBindingNode
          .getFirstChild(FlexGrammar.TYPED_IDENTIFIER)
          .getFirstChild(FlexGrammar.IDENTIFIER);
        Set<AstNodeType> modifiers = Modifiers.getModifiers(directive.getFirstChild(FlexGrammar.ATTRIBUTES));
        boolean isPrivateStaticConst = modifiers.contains(FlexKeyword.PRIVATE) && modifiers.contains(FlexKeyword.STATIC) && isConst(variableDef);

        reportIssue(isPrivateStaticConst, pattern.matcher(identifierNode.getTokenValue()).matches(), variableBindingNode);
      }
    }
  }

  private void reportIssue(boolean isPrivateStaticConst, boolean matchesFormat, AstNode identifierNode) {
    String identifier = identifierNode.getTokenValue();

    if (!isPrivateStaticConst && !matchesFormat) {
      addIssue(MessageFormat.format("Make the logger \"{0}\" private static const and rename it to comply with the format \"{1}\".", identifier, format), identifierNode);
    } else if (!isPrivateStaticConst) {
      addIssue(MessageFormat.format("Make the logger \"{0}\" private static const.", identifier), identifierNode);
    } else if (!matchesFormat) {
      addIssue(MessageFormat.format("Rename the \"{0}\" logger to comply with the format \"{1}\".", identifier, format), identifierNode);
    }
  }

  private static boolean isILogger(AstNode variableBinding) {
    AstNode typeExpr = variableBinding
      .getFirstChild(FlexGrammar.TYPED_IDENTIFIER)
      .getFirstChild(FlexGrammar.TYPE_EXPR);

    return typeExpr != null && "ILogger".equals(typeExpr.getTokenValue());
  }

  private static boolean isConst(AstNode variableDef) {
    return variableDef.getFirstChild(FlexGrammar.VARIABLE_DEF_KIND).getFirstChild().is(FlexKeyword.CONST);
  }

  private static boolean isVariableDeclaration(AstNode directive) {
    return directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE) != null &&
      directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE).getFirstChild().is(FlexGrammar.VARIABLE_DECLARATION_STATEMENT);
  }
}
