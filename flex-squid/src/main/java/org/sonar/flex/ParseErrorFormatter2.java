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
package org.sonar.flex;

import com.google.common.base.Preconditions;
import org.sonar.sslr.internal.matchers.InputBuffer;
import org.sonar.sslr.internal.matchers.TextUtils;
import org.sonar.sslr.parser.ParseError;

/**
 * Duplicates corresponding class from SSLR in order to remove path to failed rules.
 */
public class ParseErrorFormatter2 {

  /**
   * Number of lines in snippet before and after line with error.
   */
  private static final int SNIPPET_SIZE = 10;

  public String format(ParseError parseError) {
    Preconditions.checkNotNull(parseError);

    InputBuffer inputBuffer = parseError.getInputBuffer();
    InputBuffer.Position position = inputBuffer.getPosition(parseError.getErrorIndex());
    StringBuilder sb = new StringBuilder();
    sb.append("Parse error at line ").append(position.getLine())
      .append(" column ").append(position.getColumn())
      .append("\n\n");
    appendSnipped(sb, inputBuffer, position);
    return sb.toString();
  }

  private static void appendSnipped(StringBuilder sb, InputBuffer inputBuffer, InputBuffer.Position position) {
    int startLine = Math.max(position.getLine() - SNIPPET_SIZE, 1);
    int endLine = Math.min(position.getLine() + SNIPPET_SIZE, inputBuffer.getLineCount());
    int padding = Integer.toString(endLine).length();
    String lineNumberFormat = "%1$" + padding + "d: ";
    for (int line = startLine; line <= endLine; line++) {
      sb.append(String.format(lineNumberFormat, line));
      sb.append(TextUtils.trimTrailingLineSeparatorFrom(inputBuffer.extractLine(line))).append('\n');
      if (line == position.getLine()) {
        for (int i = 1; i < position.getColumn() + padding + 2; i++) {
          sb.append(' ');
        }
        sb.append("^\n");
      }
    }
  }

}
