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

public class IfStatementTest {

  private LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void simpleIf() {
    Assertions.assertThat(g.rule(FlexGrammar.IF_STATEMENT))
      .matches("if (x >1);")
      .matches("if (x >1) {  q=b;}")
      .matches("if (x >1) {  q=b; \n var student : Object = {FirstName:\"John\", LastName:\"Smith\"};}");
  }

  @Test
  public void ifElse() {
    Assertions.assertThat(g.rule(FlexGrammar.IF_STATEMENT))
      .matches("if (x > 20)\n"
            + "{\n"
            + "    trace(\"x is > 20\");\n"
            + "}\n"
            + "else\n"
            + "{\n"
            + "    trace(\"x is <= 20\");\n"
            + "}");
  }

  @Test
  public void if2Else() {
    Assertions.assertThat(g.rule(FlexGrammar.IF_STATEMENT))
      .matches("if (x > 0)\n" +
              "    trace(\"x is positive\");\n" +
              "else if (x < 0) \n" +
              "    trace(\"x is negative\");\n" +
              "else\n" +
              "    trace(\"x is 0\");");
    
  }  
}
