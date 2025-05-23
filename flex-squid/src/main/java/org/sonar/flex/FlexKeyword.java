/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource SA
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
package org.sonar.flex;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.sonar.sslr.grammar.GrammarRuleKey;

public enum FlexKeyword implements GrammarRuleKey {

  /**
   * "as" is not keyword in ActionScript 2, so we treat it as syntactic keyword
   */
  AS(true),
  BREAK,
  CASE,
  CATCH,
  CLASS,
  CONST,
  CONTINUE,
  DEFAULT,
  DELETE,
  DO,
  ELSE,
  EXTENDS,
  FALSE,
  FINALLY,
  FOR,
  FUNCTION,
  IF,
  IMPLEMENTS,
  IMPORT,
  IN,
  INSTANCEOF,
  INTERFACE,
  INTERNAL,
  IS,
  NEW,
  NULL,
  PACKAGE,
  PRIVATE,
  PROTECTED,
  PUBLIC,
  RETURN,
  SUPER,
  SWITCH,
  THIS,
  THROW,
  TRUE,
  TRY,
  TYPEOF,
  USE,
  VAR,
  VOID,
  WHILE,
  WITH,

  EACH(true),
  GET(true),
  SET(true),
  NAMESPACE(true),
  INCLUDE(true),
  DYNAMIC(true),
  FINAL(true),
  // "native" strangely appears in both the "keywords" and "syntactic keywords" lists of the language spec
  // It seems that "native" is accepted as a valid identifier by the compiler and should be considered as a "syntactic keyword"
  NATIVE(true),
  OVERRIDE(true),
  STATIC(true),
  XML(true);

  private final boolean syntactic;

  FlexKeyword() {
    this(false);
  }

  FlexKeyword(boolean syntactic) {
    this.syntactic = syntactic;
  }

  public static String[] keywordValues() {
    String[] keywordsValue = new String[FlexKeyword.values().length];
    int i = 0;
    for (FlexKeyword keyword : FlexKeyword.values()) {
      keywordsValue[i] = keyword.getValue();
      i++;
    }
    return keywordsValue;
  }

  public static List<FlexKeyword> keywords() {
    return Collections.unmodifiableList(Arrays.stream(values())
      .filter(flexKeyword -> !flexKeyword.syntactic)
      .collect(Collectors.toList()));
  }

  public String getValue() {
    return toString().toLowerCase(Locale.ENGLISH);
  }

}
