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

public class EndOfStatementNoLineBreakTest {

  private final LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void semicolon() {
    assertThat(g.rule(FlexGrammar.EOS_NO_LB))
      .matchesPrefix(";", "another-statement")
      .matchesPrefix("/* comment */ ;", "another-statement")
    .notMatches("\n ;")
    .notMatches("/* comment \n */ ;");
  }

  @Test
  public void line_terminator_sequence() {
    assertThat(g.rule(FlexGrammar.EOS_NO_LB))
      .matchesPrefix("\n", "another-statement")
      .matchesPrefix("\r\n", "another-statement")
      .matchesPrefix("\r", "another-statement")
      .matchesPrefix("// comment \n", "another-statement")
      .matchesPrefix("/* comment */ \n", "another-statement")
      .notMatches("\n\n")
      .notMatches("/* comment \n */ \n");
  }

  @Test
  public void right_curly_bracket() {
    assertThat(g.rule(FlexGrammar.EOS_NO_LB))
      .matchesPrefix("", "}")
      .matchesPrefix(" ", "}")
      .matchesPrefix("/* comment */ ", "}")
      .notMatches("/* comment \n */ }");
  }

  @Test
  public void end_of_input() {
    assertThat(g.rule(FlexGrammar.EOS_NO_LB))
      .matches("")
      .matches(" ")
      .matches("/* comment */")
      .notMatches("/* comment \n */");
  }

}
