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

public class PackageDefinitionTest {

  private LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void emptyPackage() {
    Assertions.assertThat(g.rule(FlexGrammar.PACKAGE_DEF))
      .matches("package p {}")
      .matches("package   parent.child {   } ")
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
