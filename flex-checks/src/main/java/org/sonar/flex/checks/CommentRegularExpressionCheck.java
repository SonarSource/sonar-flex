/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2021 SonarSource SA
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

import com.google.common.base.Strings;
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
      if (!Strings.isNullOrEmpty(regularExpression)) {
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
