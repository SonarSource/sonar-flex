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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;

public class CodeReaderTest {

  @Test
  public void pop() {
    CodeReader reader = new CodeReader(new StringReader("package org.sonar;"));

    StringWriter sw = new StringWriter();
    reader.pop(sw);
    assertEquals("p", sw.toString());
    reader.pop(sw);
    assertEquals("pa", sw.toString());
  }

  @Test
  public void peekAndPop() {
    CodeReader reader = new CodeReader(new StringReader("package org.sonar;"));
    StringWriter sw = new StringWriter();
    assertEquals('p', reader.peek());
    reader.pop(sw);
    assertEquals("p", sw.toString());
    assertEquals('p', reader.lastChar());
    assertEquals('a', reader.peek());
    assertEquals('p', reader.lastChar());
    reader.pop(sw);
    assertEquals("pa", sw.toString());
  }

  @Test
  public void peekLast() {
    CodeReader reader = new CodeReader(new StringReader("bar"));
    StringWriter sw = new StringWriter();
    reader.pop(sw);
    assertEquals("b", sw.toString());
    reader.pop(sw);
    assertEquals("ba", sw.toString());
    reader.pop(sw);
    assertEquals("bar", sw.toString());
    assertEquals(-1, reader.peek());
  }

  @Test
  public void popTo() {
    CodeReader reader = new CodeReader(new StringReader("package org.sonar;"));
    StringWriter sw = new StringWriter();
    reader.popTo(new EndMatcher() {

      public boolean read(int endFlag) {
        return 'r' == (char) endFlag;
      }
    }, sw);
    assertThat(sw.toString(), is("package o"));
    assertThat(reader.peek(), is((int) 'r'));
  }

  @Test
  public void peekToEnd() {
    CodeReader reader = new CodeReader(new StringReader("bar"));
    char[] chars = reader.peek(4);
    assertThat(chars.length, is(4));
    assertThat((int) chars[3], lessThan(1));
  }
}
