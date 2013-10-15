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

public class GlobalDefinitionsTest {

  private LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void variableDefinition() {
    Assertions.assertThat(g.rule(FlexGrammar.VARIABLE_DEF))
      .matches("var student : Object = {FirstName:\"John\", LastName:\"Smith\" }")
      .matches("const int= 3");
  }

  @Test
  public void functionDefinition() {
    Assertions.assertThat(g.rule(FlexGrammar.FUNCTION_DEF))
      .matches("function f() : Number {}")
      .matches("function get f() {}");
  }

  @Test
  public void interfaceDefinition() {
    Assertions.assertThat(g.rule(FlexGrammar.INTERFACE_DEF))
      .matches("interface T  {}")
      .matches("interface T extends Tye0bject{}")
      .notMatches("interfaceT  {}");
  }

  @Test
  public void packageDefinition() {
    Assertions.assertThat(g.rule(FlexGrammar.PACKAGE_DEF))
      .matches("package p {}")
      .matches("package   parent.child {   } ")
      .matches("package p{}");
  }

  @Test
  public void namespaceDefinition() {
    Assertions.assertThat(g.rule(FlexGrammar.NAMESPACE_DEF))
      .matches("namespace NS1")
      .matches("namespace NS2= NS1")
      .matches("namespace NS3 = \"http://www.macromedia.com/flash/2005\"");
  }

  @Test
  public void regexp() {
    Assertions.assertThat(g.rule(FlexGrammar.REGULAR_EXPRESSION))
      .matches("/test-\\d/i")
      .matches("/<p>.*?<\\/p>/s")
      .matches("/\\d{3}-\\d{3}-\\d{4}|\\(\\d{3}\\)\\s?\\d{3}-\\d{4}/");

  }
}
