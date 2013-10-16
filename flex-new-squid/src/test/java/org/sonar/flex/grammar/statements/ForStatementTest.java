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

  private LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void forStatement() {

    Assertions.assertThat(g.rule(FlexGrammar.FOR_STATEMENT))
      .matches("for (i = 0; i < 5; i++) { trace(i);}");
  }

  @Test
  public void forInStatement() {
    Assertions.assertThat(g.rule(FlexGrammar.FOR_STATEMENT))
      .matches("for (var i:String in myObj) { trace(i + \": \" + myObj[i]);}")
      .matches("for (var n in e..name) { trace(i + \": \" + myObj[i]);}");

    // Check for "allowIn" vs "noIn":
    // This rule should not consume more than "var i:String "
    Assertions.assertThat(g.rule(FlexGrammar.FOR_IN_BINDING))
      .notMatches("var i:String in myObj");
  }

  @Test
  public void forEachInStatement() {
    Assertions.assertThat(g.rule(FlexGrammar.FOR_STATEMENT))
      .matches("for each (var num in myObj) {trace(num);}");
  }
}
