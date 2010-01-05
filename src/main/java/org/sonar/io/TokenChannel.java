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

package org.sonar.io;

import java.util.List;
import java.io.StringWriter;

public class TokenChannel implements Channel {
  List<String> list;

  public TokenChannel(List<String> list) {
    this.list = list;
  }

  public boolean read(CodeReader code) {
    int nextChar = code.peek();
    if (nextChar != '-') {
      StringWriter sw = new StringWriter();
      while (nextChar != '-' && nextChar != -1) {
        code.pop(sw);
        nextChar = code.peek();
      }
      list.add(sw.toString());
      return true;
    }
    return false;
  }
}
