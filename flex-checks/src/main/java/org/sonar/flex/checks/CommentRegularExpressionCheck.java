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

import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.Trivia;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexCheck;

@Rule(key = "CommentRegularExpression")
public class CommentRegularExpressionCheck extends FlexCheck {

  private static final String DEFAULT_REGULAR_EXPRESSION = "";
  private static final String DEFAULT_MESSAGE = "The regular expression matches this comment.";

  @RuleProperty(
    key = "regularExpression",
    description = "The regular expression",
    defaultValue = "" + DEFAULT_REGULAR_EXPRESSION)
  public String regularExpression = DEFAULT_REGULAR_EXPRESSION;

  @RuleProperty(
    key = "message",
    description = "The issue message",
    defaultValue = "" + DEFAULT_MESSAGE)
  public String message = DEFAULT_MESSAGE;

  private Pattern pattern = null;

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.emptyList();
  }

  public Pattern pattern() {
    if (pattern == null) {
      Objects.requireNonNull(regularExpression, "getRegularExpression() should not return null");
      if (!regularExpression.isEmpty()) {
        try {
          pattern = Pattern.compile(regularExpression, Pattern.DOTALL);
        } catch (RuntimeException e) {
          throw new IllegalStateException("Unable to compile regular expression: " + regularExpression, e);
        }
      }
    }
    return pattern;
  }

  @Override
  public void visitToken(Token token) {
    Pattern regexp = pattern();
    if (regexp != null) {
      for (Trivia trivia : token.getTrivia()) {
        if (trivia.isComment() && regexp.matcher(trivia.getToken().getOriginalValue()).matches()) {
          addIssue(message, trivia.getToken());
        }
      }
    }
  }
}
