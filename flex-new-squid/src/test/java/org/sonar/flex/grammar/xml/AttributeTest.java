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
package org.sonar.flex.grammar.xml;

import org.sonar.flex.*;
import org.junit.Test;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.tests.Assertions;

public class GlobalXmlTest {

  private final LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void comment() {
    Assertions.assertThat(g.rule(FlexGrammar.XML_COMMENT))
      .matches("<!-- comment -->")
      .matchesPrefix("<!-- comment -->", "<!-- another commment-->")
      .notMatches("<!-- -- -->");
  }

  @Test
  public void cdata() {
    Assertions.assertThat(g.rule(FlexGrammar.XML_CDATA))
      .matches("<![CDATA[ cdata content <tag attr=\"with attribute\"> ]]>")
      .matches("<![CDATA[]]>")
      .notMatches("<![CDATA[ cdata content ]] ]]>");
  }

  @Test
  public void pi() {
    Assertions.assertThat(g.rule(FlexGrammar.XML_PI))
      .matches("<? Some content ... ?>")
      .notMatches("<? Some wrong ?>content ?>");
  }

  @Test
  public void text() {
    Assertions.assertThat(g.rule(FlexGrammar.XML_TEXT))
      .notMatches("")
      .matches("Some text fitting format :)")
      .notMatches("Some text not fitting the {format :(")
      .notMatches(" Another one not fitting...< :(");
  }

  @Test
  public void name() {
    Assertions.assertThat(g.rule(FlexGrammar.XML_NAME))
      .matches("NameStart.")
      .matches("NameStart_:namepart")
      .notMatches(">");
  }

  @Test
  public void attributeValue() {
    Assertions.assertThat(g.rule(FlexGrammar.XML_ATTRIBUTE_VALUE))
      .matches("\" Content ==\"")
      .matches("' Content ++'")
      .notMatches("\" Content\" == \"");
  }

  @Test
  public void initialiser() {
    Assertions.assertThat(g.rule(FlexGrammar.XML_INITIALISER))
      .matches("<order> \n"
             + "        <!--This is a comment. --> \n"
             + "        <?PROC_INSTR sample ?> \n"
             + "        <item id='1'> \n"
            + "            <menuName>burger</menuName> \n"
            + "            <price>3.95</price> \n"
              + "        </item> \n"
              + " </order>");
  }

  @Test
  public void element() {
    Assertions.assertThat(g.rule(FlexGrammar.XML_ELEMENT_CONTENT))
      .matches("<tagname attr=\"attrvalue\"/>");
  }
}
