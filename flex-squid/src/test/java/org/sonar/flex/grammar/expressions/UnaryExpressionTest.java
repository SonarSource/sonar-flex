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
package org.sonar.flex.grammar.expressions;

import org.junit.jupiter.api.Test;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.tests.Assertions;

public class UnaryExpressionTest {

  private final LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void unary() {
    Assertions.assertThat(g.rule(FlexGrammar.UNARY_EXPR))
      .matches("a")
      .matches("delete a")
      .matches("++ a")
      .matches("-- a")

      .matches("void a")
      .matches("typeof a")
      .matches("+ a")
      .matches("- a")
      .matches("! a")
      .matches("~ a")

      .matches("void ++ a")

      // ActionScript 2:
//      .matches("not a")
    ;
  }

}
