/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonar.flex.toolkit;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.sonar.colorizer.CDocTokenizer;
import org.sonar.colorizer.CppDocTokenizer;
import org.sonar.colorizer.JavadocTokenizer;
import org.sonar.colorizer.KeywordsTokenizer;
import org.sonar.colorizer.StringTokenizer;
import org.sonar.colorizer.Tokenizer;
import org.sonar.flex.api.FlexKeyword;
import org.sonar.flex.parser.FlexParser;
import org.sonar.sslr.toolkit.Toolkit;

public final class FlexToolkit {

  public static final String SPAN_END_TAG = "</span>";

  private FlexToolkit() {
  }

  public static void main(String[] args) {
    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "SSDK");
    new Toolkit(FlexParser.create(Charset.defaultCharset()), getTokenizers(), "SSLR Flex Toolkit").run();
  }

  // Visible for testing
  static List<Tokenizer> getTokenizers() {
    return Collections.unmodifiableList(Arrays.asList(
      new StringTokenizer("<span class=\"s\">", SPAN_END_TAG),
      new CDocTokenizer("<span class=\"cd\">", SPAN_END_TAG),
      new JavadocTokenizer("<span class=\"cppd\">", SPAN_END_TAG),
      new CppDocTokenizer("<span class=\"cppd\">", SPAN_END_TAG),
      new KeywordsTokenizer("<span class=\"k\">", SPAN_END_TAG, FlexKeyword.keywordValues())));
  }

}
