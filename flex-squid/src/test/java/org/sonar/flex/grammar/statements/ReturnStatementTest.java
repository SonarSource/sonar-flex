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
package org.sonar.flex.grammar.statements;

import org.junit.jupiter.api.Test;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.tests.Assertions;

public class ReturnStatementTest {

  private final LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void eos_is_line_terminator() {
    Assertions.assertThat(g.rule(FlexGrammar.RETURN_STATEMENT))
      .matchesPrefix("return \n", "another-statement ;")
      .matchesPrefix("return expression \n", "another-statement ;")
      .matchesPrefix("return \n", ";");
  }

  @Test
  public void eos_is_semicolon() {
    Assertions.assertThat(g.rule(FlexGrammar.RETURN_STATEMENT))
      .matchesPrefix("return ;", "another-statement")
      .matchesPrefix("return expression ;", "another-statement");
  }

  @Test
  public void eos_before_right_curly_bracket() {
    Assertions.assertThat(g.rule(FlexGrammar.RETURN_STATEMENT))
      .matchesPrefix("return ", "}")
      .matchesPrefix("return expression ", "}");
  }

  @Test
  public void eos_is_end_of_input() {
    Assertions.assertThat(g.rule(FlexGrammar.RETURN_STATEMENT))
      .matches("return ")
      .matches("return expression ");
  }

}
