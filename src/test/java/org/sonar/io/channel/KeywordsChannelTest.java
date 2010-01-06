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

import java.util.Set;
import java.util.ArrayList;

import com.google.common.collect.Sets;

public class KeywordsChannelTest {
  @Test
  public void testKeywordsChannel() {
    Set<String> keywords = Sets.newHashSet("wordone", "wordtwo", "word-three");

    ArrayList<String> actual = new ArrayList<String>();
    KeywordsChannel channel = new KeywordsChannel(actual , keywords);
    TokenChannelTester.check("wordone", channel, "wordone", actual, true);

    actual = new ArrayList<String>();
    channel = new KeywordsChannel(actual , keywords);
    TokenChannelTester.check("wordtwo:", channel, "wordtwo", actual, true);

    actual = new ArrayList<String>();
    channel = new KeywordsChannel(actual , keywords);
    TokenChannelTester.check("word-three ", channel, "word-three", actual, true);

    actual = new ArrayList<String>();
    channel = new KeywordsChannel(actual , keywords);
    TokenChannelTester.check(" wordone:", channel, "wordone", actual, false);

    actual = new ArrayList<String>();
    channel = new KeywordsChannel(actual , keywords);
    TokenChannelTester.check("wordone wordtwo", channel, "wordone", actual, true);

    actual = new ArrayList<String>();
    channel = new KeywordsChannel(actual , keywords);
    TokenChannelTester.check("wordone-wordtwo", channel, "wordone", actual, false);
  }
}
