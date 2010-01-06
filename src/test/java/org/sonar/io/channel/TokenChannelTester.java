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

import com.google.common.collect.Lists;

import java.util.List;
import java.util.ArrayList;
import java.io.StringReader;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import org.sonar.io.CodeReader;

public class TokenChannelTester {
  public static void check(String input, AbstractTokenChannel channel, String expectedOutput, List<String> actual, boolean assertion) {
    List<String> expected = Lists.newArrayList(expectedOutput);

    if (assertion) {
      assertTrue(channel.read(getCodeReader(input)));
      assertThat(actual, is(expected));
    }
    else {
      assertFalse(channel.read(getCodeReader(input)));
    }
  }

  public static void check(String input, AbstractTokenChannel channel, List<String> actual, boolean assertion) {
    check(input, channel, null, actual, assertion);
  }


  private static CodeReader getCodeReader(String s) {
    return new CodeReader(new StringReader(s));
  }

}
