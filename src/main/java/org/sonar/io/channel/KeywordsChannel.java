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

import java.util.List;
import java.util.Set;
import java.io.StringWriter;

public class KeywordsChannel extends AbstractTokenChannel {
  Set<String> keywords;

  public KeywordsChannel(List<String> list, Set<String> keywords) {
    super(list);
    if (keywords == null || keywords.isEmpty()) {
      throw new IllegalArgumentException();
    }
    this.keywords = keywords;
  }

  public boolean read(CodeReader code) {
    if (isValidForKeyword(code.peek())) {
      int i=1;
      while (isValidForKeyword(code.peek(i)[i-1])) {
        i++;
      }
      String s = new String(code.peek(i-1));
      if (keywords.contains(s)) {
        addToken(new StringWriter().append(s));
        return true;
      }
    }
    return false;
  }

  public static boolean isValidForKeyword(int c) {
    boolean valid = false;
    if (Character.isLetter(c) || c == '-') {
      valid = true;
    }
    return valid;
  }



}
