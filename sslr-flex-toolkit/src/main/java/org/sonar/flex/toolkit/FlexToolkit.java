/*
 * Sonar Flex Plugin
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
package org.sonar.flex.toolkit;

import com.google.common.collect.ImmutableList;
import com.sonar.sslr.devkit.SsdkGui;
import com.sonar.sslr.impl.Parser;
import org.sonar.colorizer.*;
import org.sonar.flex.api.FlexGrammar;
import org.sonar.flex.api.FlexKeyword;
import org.sonar.flex.parser.FlexParser;

import java.util.List;

public final class FlexToolkit {

  private FlexToolkit() {
  }

  public static void main(String[] args) {
    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "SSDK");
    Parser<FlexGrammar> parser = FlexParser.create();
    SsdkGui toolkit = new SsdkGui(parser, getTokenizers());
    toolkit.setVisible(true);
    toolkit.setSize(1000, 800);
    toolkit.setTitle("SSLR Flex Toolkit");
  }

  public static List<Tokenizer> getTokenizers() {
    return ImmutableList.of(
        new StringTokenizer("<span class=\"s\">", "</span>"),
        new CDocTokenizer("<span class=\"cd\">", "</span>"),
        new JavadocTokenizer("<span class=\"cppd\">", "</span>"),
        new CppDocTokenizer("<span class=\"cppd\">", "</span>"),
        new KeywordsTokenizer("<span class=\"k\">", "</span>", FlexKeyword.keywordValues()));
  }

}
