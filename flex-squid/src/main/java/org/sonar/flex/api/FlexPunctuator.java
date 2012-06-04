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
package org.sonar.flex.api;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

public enum FlexPunctuator implements TokenType {
  QUESTION("?"),
  LPAREN("("),
  RPAREN(")"),
  LBRACK("["),
  RBRACK("]"),
  LCURLY("{"),
  RCURLY("}"),
  COLON(":"),
  DBL_COLON("::"),
  COMMA(","),
  ASSIGN("="),
  EQUAL("=="),
  STRICT_EQUAL("==="),
  LNOT("!"),
  BNOT("~"),
  NOT_EQUAL("!="),
  STRICT_NOT_EQUAL("!=="),
  DIV("/"),
  DIV_ASSIGN("/="),
  PLUS("+"),
  PLUS_ASSIGN("+="),
  INC("++"),
  MINUS("-"),
  MINUS_ASSIGN("-="),
  DEC("--"),
  STAR("*"),
  STAR_ASSIGN("*="),
  MOD("%"),
  MOD_ASSIGN("%="),
  SR(">>"),
  SR_ASSIGN(">>="),
  BSR(">>>"),
  BSR_ASSIGN(">>>="),
  GE(">="),
  GT(">"),
  SL("<<"),
  SL_ASSIGN("<<="),
  LE("<="),
  LT("<"),
  BXOR("^"),
  BXOR_ASSIGN("^="),
  BOR("|"),
  BOR_ASSIGN("|="),
  LOR("||"),
  BAND("&"),
  BAND_ASSIGN("&="),
  LAND("&&"),
  LAND_ASSIGN("&&="),
  LOR_ASSIGN("||="),
  E4X_ATTRI("@"),
  SEMI(";"),
  DOT("."),
  REST("...");

  private final String value;

  private FlexPunctuator(String value) {
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

}
