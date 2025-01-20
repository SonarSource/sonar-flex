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

public class PackageDefinitionTest {

  private final LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void emptyPackage() {
    Assertions.assertThat(g.rule(FlexGrammar.PACKAGE_DEF))
      .matches("package p {}")
      .matches("package   parent.child {   }")
      .matches("package p{}");
  }

  @Test
  public void packageWithBody() {
    Assertions.assertThat(g.rule(FlexGrammar.PACKAGE_DEF))
      .matches("package samples\n"
      + "{\n"
      + "    public class SampleCode\n"
      + "    {\n"
      + "        private var sampleGreeting:String;\n"
      + "        public function sampleFunction()\n"
      + "        {\n"
      + "            trace(sampleGreeting + \" from sampleFunction()\");\n"
      + "        }\n"
      + "    }\n"
      + "}");
  }

  @Test
  public void childPackages() {
    Assertions.assertThat(g.rule(FlexGrammar.PACKAGE_DEF))
      .matches("package flash.xml\n"
      + "{\n"
      + "    class XMLDocument {}\n"
      + "    class XMLNode {}\n"
      + "    class XMLSocket {}\n"
      + "}");
  }
}
