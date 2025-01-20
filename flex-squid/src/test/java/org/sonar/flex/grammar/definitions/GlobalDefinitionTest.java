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
package org.sonar.flex.grammar.definitions;

import org.junit.jupiter.api.Test;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.tests.Assertions;

public class GlobalDefinitionTest {

  private final LexerlessGrammar g = FlexGrammar.createGrammar();

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
      .matches("package   parent.child {   }")
      .matches("package p{}")
      .matches("package samples\n" +
              "{\n" +
              "    public class SampleCode\n" +
              "    {\n" +
              "        public var sampleGreeting:String;\n" +
              "        public function sampleFunction()\n" +
              "        {\n" +
              "            trace(sampleGreeting + \" from sampleFunction()\");\n" +
              "        }\n" +
              "    }\n" +
            "}")
    .matches("package flash.xml\n" +
            "{\n" +
            "    class XMLDocument {}\n" +
            "    class XMLNode {}\n" +
            "    class XMLSocket {}\n" +
            "}");
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
      .matches("/\\d{3}-\\d{3}-\\d{4}|\\(\\d{3}\\)\\s?\\d{3}-\\d{4}/")
      .matches("/([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}/")
      .notMatches("/<p>.*?<\\/p>s");
  }
}
