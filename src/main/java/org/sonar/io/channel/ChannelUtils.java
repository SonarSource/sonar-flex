/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.io.channel;

import org.sonar.io.CodeReader;
import java.io.Writer;

public class ChannelUtils {

  private ChannelUtils(){
  }

  public static void readTo(CodeReader code, Writer writer, int delimiter) {
    int nextChar;
    int length = 0;
    do {
      length++;
      code.pop(writer);
      nextChar = code.peek();
    }
    while ((nextChar != delimiter || !evenNumberOfBackSlashPriorDelimiter(code, delimiter, length)) && nextChar != -1);
    code.pop(writer);
  }

  private static boolean evenNumberOfBackSlashPriorDelimiter(CodeReader code, int token, int length) {
    int numberOfBackSlashChar = 0;
    int i = 0;
    while (i < length) {
      if (code.lastChar(i) == '\\') {
        numberOfBackSlashChar++;
        i++;
      }
      else {
        break;
      }
    }
    return numberOfBackSlashChar % 2 == 0;
  }
}
