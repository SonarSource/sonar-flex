/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2024 SonarSource SA
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

import org.sonar.sslr.grammar.GrammarRuleKey;

public enum FlexPunctuator implements GrammarRuleKey {

  AT_SIGN("@"),
  COMMA(","),
  COLON(":"),
  DOUBLE_COLON("::"),
  DOT("."),
  DOUBLE_DOT(".."),
  LCURLYBRACE("{"),
  RCURLYBRACE("}"),
  LBRAKET("["),
  RBRAKET("]"),
  LPARENTHESIS("("),
  RPARENTHESIS(")"),
  STAR("*"),
  PLUS("+"),
  MINUS("-"),
  DOUBLE_PLUS("++"),
  DOUBLE_MINUS("--"),
  DIV("/"),
  MOD("%"),
  NOT("!"),
  EQUAL1("="),
  EQUAL2("=="),
  EQUAL3("==="),
  NOTEQUAL1("!="),
  NOTEQUAL2("!=="),
  SL("<<"),
  SR(">>"),
  SR2(">>>"),
  STAR_EQU("*="),
  DIV_EQU("/="),
  MOD_EQU("%="),
  PLUS_EQU("+="),
  MINUS_EQU("-="),
  SL_EQU("<<="),
  SR_EQU(">>="),
  SR_EQU2(">>>="),
  AND_EQU("&="),
  XOR_EQU("^="),
  OR_EQU("|="),
  ANDAND_EQU("&&="),
  XORXOR_EQU("^^="),
  OROR_EQU("||="),
  LT("<"),
  GT(">"),
  LE("<="),
  GE(">="),
  AND("&"),
  ANDAND("&&"),
  XOR("^"),
  OR("|"),
  OROR("||"),
  QUERY("?"),
  TILD("~"),
  TRIPLE_DOTS("..."),
  SEMICOLON(";");

  private final String value;

  FlexPunctuator(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
