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
import java.io.StringWriter;

public class LitteralChannel extends AbstractTokenChannel {
  private int[] token;

  public LitteralChannel(List<String> list, int... token) {
    super(list);
    if (token.length == 0) {
      throw new IllegalArgumentException();
    }
    this.token = token;
  }

  public boolean read(CodeReader code) {
    int nextChar = code.peek();
    StringWriter sw = new StringWriter();
    if (isToken(nextChar)) {
      ChannelUtils.readTo(code, sw, nextChar);
      addToken(sw);
      return true;
    }
    return false;
  }

  private boolean isToken(int c) {
    boolean isToken = false;

    for (int i = 0; i < token.length; i++) {
      if (c == token[i]) {
        isToken = true;
        break;
      }
    }
    return isToken;
  }
}
