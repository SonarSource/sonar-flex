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
package org.sonar.flex.grammar.lexical;

import org.junit.jupiter.api.Test;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class SpacingNoLineBreakTest {

  private final LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void empty() {
    assertThat(g.rule(FlexGrammar.SPACING_NO_LB))
      .matches("");
  }

  @Test
  public void whitespace() {
    assertThat(g.rule(FlexGrammar.SPACING_NO_LB))
      .matches(" ")
      .notMatches("\n")
      .notMatches("\r")
      .notMatches("\r\n");
  }

  @Test
  public void single_line_comment() {
    assertThat(g.rule(FlexGrammar.SPACING_NO_LB))
      .matchesPrefix(" // comment", "\n");
  }

  @Test
  public void multi_line_comment() {
    assertThat(g.rule(FlexGrammar.SPACING_NO_LB))
      .matches(" /* comment */ /* comment */ ")
      .notMatches("/* comment \n */");
  }

}
