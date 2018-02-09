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

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexCheck;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "S1451",
  name = "Copyright and license headers should be defined",
  priority = Priority.BLOCKER)
@SqaleConstantRemediation("5min")
public class FileHeaderCheck extends FlexCheck {

  private static final String DEFAULT_HEADER_FORMAT = "";

  @RuleProperty(
    key = "headerFormat",
    description = "Expected copyright and license header",
    defaultValue = DEFAULT_HEADER_FORMAT,
    type = "TEXT")
  public String headerFormat = DEFAULT_HEADER_FORMAT;

  @RuleProperty(
    key = "isRegularExpression",
    description = "Whether the headerFormat is a regular expression",
    defaultValue = "false")
  public boolean isRegularExpression = false;

  private Predicate<String> headerMatcher;

  public Predicate<String> headerMatcher() {
    if (headerMatcher == null) {
      if (isRegularExpression) {
        String regexp = headerFormat;
        if (!headerFormat.endsWith("\n") && !headerFormat.endsWith("\r")) {
          regexp += "(\r|\r\n|\n)";
        }
        Pattern pattern = Pattern.compile(regexp);
        headerMatcher = s -> matchesRegularExpression(pattern, s);
      } else {
        String[] expectedLines = headerFormat.split("(?:\r)?\n|\r");
        headerMatcher = s -> matchesPlainTextHeader(expectedLines, s);
      }
    }
    return headerMatcher;
  }

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.emptyList();
  }

  @Override
  public void visitFile(AstNode astNode) {
    String fileContent = getContext().fileContent();
    boolean hasHeader = headerMatcher().test(fileContent);
    if (!hasHeader) {
      addFileIssue("Add or update the header of this file.");
    }
  }


  private static boolean matchesRegularExpression(Pattern regularExpression, String content) {
    Matcher matcher = regularExpression.matcher(content);
    if (matcher.find()) {
      return matcher.start() == 0;
    }
    return false;
  }

  private static boolean matchesPlainTextHeader(String[] expectedLines, String content) {
    String[] lines = content.split("\\r?\\n");

    boolean result;
    if (expectedLines.length <= lines.length) {
      result = true;

      for (int i = 0; i < expectedLines.length; i++) {
        String line = lines[i];
        if (!line.equals(expectedLines[i])) {
          result = false;
          break;
        }
      }
    } else {
      result = false;
    }

    return result;
  }

}
