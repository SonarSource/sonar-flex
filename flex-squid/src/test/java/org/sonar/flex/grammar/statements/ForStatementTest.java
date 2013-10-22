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
package org.sonar.flex.grammar.statements;

import org.junit.Test;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.tests.Assertions;

public class ForStatementTest {

  private final LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void test() {
    Assertions.assertThat(g.rule(FlexGrammar.FOR_INITIALISER))
      // list expression
      .matches("a = 1, b = 2")
      // variable definition
      .matches("var a = 1, b = 2");

    Assertions.assertThat(g.rule(FlexGrammar.FOR_IN_BINDING))
      // variable binding
      .matches("var i")
      // postfix expression
      .matches("i");

    Assertions.assertThat(g.rule(FlexGrammar.FOR_STATEMENT))
      // for
      .matches("for ( ; ; ) { }")
      .matches("for (var i = 0; i < 5; i++) { }")
      // for in
      .matches("for (var item:String in items) { }")
      // for each
      .matches("for each (var item:String in items) { }")
      .notMatches("for \n each (var item:String in items) { }");
  }

}
