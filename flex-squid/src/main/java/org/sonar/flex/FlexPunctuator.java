/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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

  private FlexPunctuator(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
