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
  // was replaced by grammar rule
  // STAR_ASSIGN("*="),
  MOD("%"),
  MOD_ASSIGN("%="),
  SR(">>"),
  SR_ASSIGN(">>="),
  BSR(">>>"),
  BSR_ASSIGN(">>>="),
  // was replaced by grammar rule
  // GE(">="),
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

  FlexPunctuator(String value) {
    this.value = value;
  }

  @Override
  public String getName() {
    return name();
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public boolean hasToBeSkippedFromAst(AstNode node) {
    return false;
  }

}
