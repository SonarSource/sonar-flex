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
package org.sonar.flex.grammar.definitions;

import org.junit.Test;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.tests.Assertions;

public class VariableDefinitionTest {

  private LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void simpleVariableDefintion() {
    Assertions.assertThat(g.rule(FlexGrammar.VARIABLE_DEF))
      .matches("var i")
      .matches("var i:int")
      .matches("var i:int = 20");
  }

  
  @Test
  public void multiVariableDefinition() {
    Assertions.assertThat(g.rule(FlexGrammar.VARIABLE_DEF))
      .matches("var a:int, b:int, c:int")
      .matches("var a:int = 10, b:int = 20, c:int = 30");
  }
  
  @Test
  public void arrayVariableDefinition() {
    Assertions.assertThat(g.rule(FlexGrammar.VARIABLE_DEF))
      .matches("var numArray:Array = [\"zero\", \"one\", \"two\"]");
  }

  @Test
  public void objectVariableDefinition() {
    Assertions.assertThat(g.rule(FlexGrammar.VARIABLE_DEF))
      .matches("var customItem:CustomClass = new CustomClass()");
  }
}
