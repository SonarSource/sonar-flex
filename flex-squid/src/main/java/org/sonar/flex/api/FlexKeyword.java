/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * sonarqube@googlegroups.com
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
package org.sonar.flex.api;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

/**
 * Below is a list of ActionScript 3 syntactic keywords, that can be used as identifiers, but that have special meaning in certain contexts:
 * each
 * get
 * set
 * namespace
 * include
 * dynamic
 * final
 * native
 * override
 * static
 */
public enum FlexKeyword implements TokenType {
  // "as" is not a keyword in ActionScript 2, so we treat it as syntactic keyword to permit usage as identifier - see SONARPLUGINS-2072
  // AS("as"),
  BREAK("break"),
  CASE("case"),
  CATCH("catch"),
  CLASS("class"),
  CONST("const"),
  CONTINUE("continue"),
  DEFAULT("default"),
  DELETE("delete"),
  DO("do"),
  ELSE("else"),
  EXTENDS("extends"),
  FALSE("false"),
  FINALLY("finally"),
  FOR("for"),
  FUNCTION("function"),
  IF("if"),
  IMPLEMENTS("implements"),
  IMPORT("import"),
  IN("in"),
  INSTANCEOF("instanceof"),
  INTERFACE("interface"),
  INTERNAL("internal"),
  IS("is"),
  NEW("new"),
  NULL("null"),
  PACKAGE("package"),
  PRIVATE("private"),
  PROTECTED("protected"),
  PUBLIC("public"),
  RETURN("return"),
  SWITCH("switch"),
  THROW("throw"),
  TRUE("true"),
  TRY("try"),
  TYPEOF("typeof"),
  USE("use"),
  VAR("var"),
  VOID("void"),
  WHILE("while"),
  WITH("with");

  private final String value;

  private FlexKeyword(String value) {
    this.value = value;
  }

  public String getName() {
    return name();
  }

  public String getValue() {
    return value;
  }

  public boolean hasToBeSkippedFromAst(AstNode node) {
    return false;
  }

  public static String[] keywordValues() {
    FlexKeyword[] keywordsEnum = FlexKeyword.values();
    String[] keywords = new String[keywordsEnum.length];
    for (int i = 0; i < keywords.length; i++) {
      keywords[i] = keywordsEnum[i].getValue();
    }
    return keywords;
  }

}
