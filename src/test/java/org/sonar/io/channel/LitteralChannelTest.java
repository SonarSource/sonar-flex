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

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LitteralChannelTest {

  @Test
  public void isReadingLitteral() {
    List<String> actual = new ArrayList<String>();
    LitteralChannel channel = new LitteralChannel(actual, '\'');
    TokenChannelTester.check("\'string\'", channel, "\'string\'", actual, true);
    TokenChannelTester.check("this is not a string", channel, actual, false);

    channel = new LitteralChannel(actual = new ArrayList<String>(), '\"');
    TokenChannelTester.check("\'string\'", channel, actual, false);
    TokenChannelTester.check("this is not a string", channel, actual, false);
    TokenChannelTester.check("\"toto\";", channel, "\"toto\"", actual, true);
  }

  @Test
  public void escapedChar() {
    List<String> actual = new ArrayList<String>();
    LitteralChannel channel = new LitteralChannel(actual, '\"');
    TokenChannelTester.check("\"\";", channel, "\"\"", actual, true);

    actual = new ArrayList<String>();
    channel = new LitteralChannel(actual, '\"');
    TokenChannelTester.check("\"(\\\"\").replace", channel, "\"(\\\"\"", actual, true);

    actual = new ArrayList<String>();
    channel = new LitteralChannel(actual, '\"');
    TokenChannelTester.check("\"\\\\\";", channel, "\"\\\\\"", actual, true);
  }
}
