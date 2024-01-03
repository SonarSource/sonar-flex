/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2024 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
