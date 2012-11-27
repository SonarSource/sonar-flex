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
package org.sonar.flex.parser.grammar.xml;

import com.google.common.base.Charsets;
import com.sonar.sslr.impl.Parser;
import org.junit.Before;
import org.junit.Test;
import org.sonar.flex.FlexConfiguration;
import org.sonar.flex.api.FlexGrammar;
import org.sonar.flex.parser.FlexParser;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class XmlLiteralTest {

  Parser<FlexGrammar> p = FlexParser.create(new FlexConfiguration(Charsets.UTF_8));
  FlexGrammar g = p.getGrammar();

  @Before
  public void init() {
    p.setRootRule(g.xmlLiteral);
  }

  @Test
  public void realLife() {
    assertThat(p)
        .matches("<node></node>")
        .matches("<node/>")

        .matches("<node attribute='value'></node>")
        .matches("<node attribute='value'/>")

        .matches("<node>text</node>")
        .matches("<node><!-- this is a comment --></node>")
        .matches("<node><?test this is a pi ?></node>")
        .matches("<node><subnode></subnode><subnode></subnode></node>")

        .matches("<![CDATA[data]]>")

        .matches("<{binding} attribute='value'/>")
        .matches("<node {binding}='value'/>")
        .matches("<node attribute={binding}/>");
  }

}
