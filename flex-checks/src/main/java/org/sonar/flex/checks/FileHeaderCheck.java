/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.Files;
import com.sonar.sslr.api.AstNode;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.api.CharsetAwareVisitor;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Rule(
  key = "S1451",
  name = "Copyright and license headers should be defined",
  priority = Priority.BLOCKER)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.SECURITY_FEATURES)
@SqaleConstantRemediation("5min")
public class FileHeaderCheck extends SquidCheck<LexerlessGrammar> implements CharsetAwareVisitor {

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

  private Charset charset;
  private String[] expectedLines;

  private Pattern regularExpression = null;

  @Override
  public void setCharset(Charset charset) {
    this.charset = charset;
  }

  @Override
  public void init() {
    if (isRegularExpression) {
      if (headerFormat.endsWith("\n") || headerFormat.endsWith("\r")) {
        regularExpression = Pattern.compile(headerFormat);
      } else {
        regularExpression = Pattern.compile(headerFormat + "(\r|\r\n|\n)");
      }
    } else {
      expectedLines = headerFormat.split("(?:\r)?\n|\r");
    }
  }

  @Override
  public void visitFile(AstNode astNode) {
    File file = getContext().getFile();
    boolean hasHeader = isRegularExpression ? matchesRegularExpression(file) : matchesPlainTextHeader(file);
    if (!hasHeader) {
      getContext().createFileViolation(this, "Add or update the header of this file.");
    }
  }

  @VisibleForTesting
  protected boolean matchesRegularExpression(File file) {
    String content;
    try {
      content = Files.toString(file, charset);
    } catch (IOException e) {
      throw new IllegalStateException(exceptionMessage(file), e);
    }
    Matcher matcher = regularExpression.matcher(content);
    if (matcher.find()) {
      return matcher.start() == 0;
    }
    return false;
  }

  @VisibleForTesting
  protected boolean matchesPlainTextHeader(File file) {
    List<String> lines;

    try {
      lines = Files.readLines(file, charset);
    } catch (IOException e) {
      throw new IllegalStateException(exceptionMessage(file), e);
    }

    return matches(expectedLines, lines);
  }

  private static boolean matches(String[] expectedLines, List<String> lines) {
    boolean result;

    if (expectedLines.length <= lines.size()) {
      result = true;

      Iterator<String> it = lines.iterator();
      for (int i = 0; i < expectedLines.length; i++) {
        String line = it.next();
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

  private static String exceptionMessage(File file) {
    return String.format("Unable to execute rule \"S1451\" for file %s", file.getName());
  }

}
