/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2023 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.flex.lexer;

import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.BomCharacterChannel;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sonar.flex.api.FlexTokenType;

import static com.sonar.sslr.test.lexer.LexerMatchers.hasComment;
import static com.sonar.sslr.test.lexer.LexerMatchers.hasToken;
import static com.sonar.sslr.test.lexer.LexerMatchers.hasTokens;
import static org.hamcrest.MatcherAssert.assertThat;

public class FlexLexerTest {

  private static Lexer lexer;

  @BeforeAll
  public static void init() {
    lexer = FlexLexer.create(StandardCharsets.UTF_8);
  }

  @Test
  public void regular_expression_literal() throws Exception {
    assertThat("simple", lexer.lex("/a/"), hasToken("/a/", FlexTokenType.REGULAR_EXPRESSION_LITERAL));
    assertThat("flags", lexer.lex("/a/g"), hasToken("/a/g", FlexTokenType.REGULAR_EXPRESSION_LITERAL));
    assertThat("escaped slash", lexer.lex("/\\/a/"), hasToken("/\\/a/", FlexTokenType.REGULAR_EXPRESSION_LITERAL));
    assertThat("ambiguation", lexer.lex("1 / a == 1 / b"), hasTokens("1", "/", "a", "==", "1", "/", "b", "EOF"));
  }

  @Test
  public void multiline_comment() {
    assertThat(lexer.lex("/* My Comment \n*/"), hasComment("/* My Comment \n*/"));
    assertThat(lexer.lex("/**/"), hasComment("/**/"));
  }

  @Test
  public void inline_comment() {
    assertThat(lexer.lex("// My Comment \n new line"), hasComment("// My Comment "));
    assertThat(lexer.lex("//"), hasComment("//"));
  }

  @Test
  public void decimal_literal() {
    assertThat(lexer.lex("0"), hasToken("0", FlexTokenType.NUMERIC_LITERAL));
    assertThat(lexer.lex("1239"), hasToken("1239", FlexTokenType.NUMERIC_LITERAL));
  }

  @Test
  public void hex_literal() {
    assertThat(lexer.lex("0xFF"), hasToken("0xFF", FlexTokenType.NUMERIC_LITERAL));
  }

  @Test
  public void float_literal() {
    assertThat(lexer.lex("12.9E-1"), hasToken("12.9E-1", FlexTokenType.NUMERIC_LITERAL));
    assertThat(lexer.lex(".129e+1"), hasToken(".129e+1", FlexTokenType.NUMERIC_LITERAL));
  }

  @Test
  public void string_literal() {
    assertThat("empty", lexer.lex("''"), hasToken("''", GenericTokenType.LITERAL));
    assertThat("empty", lexer.lex("\"\""), hasToken("\"\"", GenericTokenType.LITERAL));

    assertThat(lexer.lex("'hello world'"), hasToken("'hello world'", GenericTokenType.LITERAL));
    assertThat(lexer.lex("\"hello world\""), hasToken("\"hello world\"", GenericTokenType.LITERAL));

    assertThat("escaped single quote", lexer.lex("'\\''"), hasToken("'\\''", GenericTokenType.LITERAL));
    assertThat("escaped double quote", lexer.lex("\"\\\"\""), hasToken("\"\\\"\"", GenericTokenType.LITERAL));
  }

  @Test
  public void identifier() {
    assertThat(lexer.lex("$"), hasToken("$", GenericTokenType.IDENTIFIER));
    assertThat(lexer.lex("_"), hasToken("_", GenericTokenType.IDENTIFIER));
    assertThat(lexer.lex("identifier"), hasToken("identifier", GenericTokenType.IDENTIFIER));
  }

  @Test
  public void bom() {
    assertThat(lexer.lex(Character.toString((char) BomCharacterChannel.BOM_CHAR)), hasTokens("EOF"));
  }

}
