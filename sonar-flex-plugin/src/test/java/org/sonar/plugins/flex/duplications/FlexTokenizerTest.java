/*
 * SonarQube Flex Plugin
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
package org.sonar.plugins.flex.duplications;

import com.google.common.base.Charsets;
import net.sourceforge.pmd.cpd.SourceCode;
import net.sourceforge.pmd.cpd.TokenEntry;
import net.sourceforge.pmd.cpd.Tokenizer;
import net.sourceforge.pmd.cpd.Tokens;
import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlexTokenizerTest {

  private Tokenizer tokenizer;

  @Before
  public void setup() throws Exception {
    tokenizer = new FlexTokenizer(Charsets.UTF_8);
  }

  @Test
  public void testTokenize() throws Exception {
    SourceCode source = new SourceCode(new SourceCode.FileCodeLoader(TestUtils.getResource("org/sonar/plugins/flex/duplications/SmallFile.as"), Charsets.UTF_8.name()));
    Tokens tokens = new Tokens();
    tokenizer.tokenize(source, tokens);

    assertThat(tokens.size()).isEqualTo(29);
  }

  @Test
  public void should_ignore_mxml() throws Exception {
    SourceCode source = mock(SourceCode.class);
    when(source.getFileName()).thenReturn("foo.mxml");

    Tokens tokens = new Tokens();
    tokenizer.tokenize(source, tokens);

    assertThat(tokens.size()).isEqualTo(1);
    assertThat(tokens.getTokens().get(0)).isEqualTo(TokenEntry.getEOF());
  }

}
