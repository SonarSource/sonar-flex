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
package org.sonar.flex.grammar.lexical;

import org.junit.jupiter.api.Test;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class SpacingTest {

  private final LexerlessGrammar g = FlexGrammar.createGrammar();

  /**
   * Must allow empty matches, otherwise "optional(SPACING)" will be used everywhere in grammar,
   * which leads to dramatic degradation of performance.
   */
  @Test
  public void empty() {
    assertThat(g.rule(FlexGrammar.SPACING))
      .matches("");
  }

  @Test
  public void whitespace() {
    assertThat(g.rule(FlexGrammar.SPACING))
      .matches(" ")
      .matches("\n")
      .matches("\r")
      .matches("\r\n");
  }

  @Test
  public void single_line_comment() {
    assertThat(g.rule(FlexGrammar.SPACING))
      .matches(" // comment")
      .matches(" // comment \n");
  }

  @Test
  public void multi_line_comment() {
    assertThat(g.rule(FlexGrammar.SPACING))
      .matches(" /* comment */ /* comment */ ")
      .matches("/* comment \n */");
  }

}
