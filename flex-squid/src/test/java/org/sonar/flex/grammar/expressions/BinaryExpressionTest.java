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
package org.sonar.flex.grammar.expressions;

import org.junit.jupiter.api.Test;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.tests.Assertions;

public class BinaryExpressionTest {

  private final LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void multiplicative() {
    Assertions.assertThat(g.rule(FlexGrammar.MULTIPLICATIVE_EXPR))
      .matches("a * b")
      .matches("a / b")
      .matches("a % b");
  }

  @Test
  public void additive() {
    Assertions.assertThat(g.rule(FlexGrammar.ADDITIVE_EXPR))
      .matches("a + b")
      .matches("a - b")
      // ActionScript 2:
      .matches("a add b");
  }

  @Test
  public void shift() {
    Assertions.assertThat(g.rule(FlexGrammar.SHIFT_EXPR))
      .matches("a << b")
      .matches("a >> b")
      .matches("a >>> b");
  }

  @Test
  public void relational() {
    Assertions.assertThat(g.rule(FlexGrammar.RELATIONAL_EXPR))
      .matches("a <= b")
      .matches("a >= b")
      .matches("a < b")
      .matches("a > b")
      .matches("a in b")
      .matches("a instanceof b")
      .matches("a is b")
      .matches("a as b")
      // ActionScript 2:
      .matches("a lt b")
      .matches("a gt b")
      .matches("a le b")
      .matches("a ge b");
  }

  @Test
  public void equality() {
    Assertions.assertThat(g.rule(FlexGrammar.EQUALITY_EXPR))
      .matches("a !== b")
      .matches("a === b")
      .matches("a == b")
      .matches("a != b")
      // ActionScript 2:
      .matches("a <> b")
      .matches("a eq b")
      .matches("a ne b");
  }

  @Test
  public void bitewise() {
    Assertions.assertThat(g.rule(FlexGrammar.BITEWISE_AND_EXPR))
      .matches("a & b");

    Assertions.assertThat(g.rule(FlexGrammar.BITEWISE_XOR_EXPR))
      .matches("a ^ b");

    Assertions.assertThat(g.rule(FlexGrammar.BITEWISE_OR_EXPR))
      .matches("a | b");
  }

  @Test
  public void logical() {
    Assertions.assertThat(g.rule(FlexGrammar.LOGICAL_AND_EXPR))
      .matches("a && b")
      .matches("a and b");

    Assertions.assertThat(g.rule(FlexGrammar.LOGICAL_OR_EXPR))
      .matches("a || b")
      .matches("a or b");
  }

}
