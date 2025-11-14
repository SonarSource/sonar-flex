/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource Sàrl.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonar.flex.lexer;

import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.BlackHoleChannel;
import com.sonar.sslr.impl.channel.IdentifierAndKeywordChannel;
import com.sonar.sslr.impl.channel.PunctuatorChannel;
import com.sonar.sslr.impl.channel.UnknownCharacterChannel;
import java.nio.charset.Charset;
import org.sonar.flex.api.FlexKeyword;
import org.sonar.flex.api.FlexPunctuator;
import org.sonar.flex.api.FlexTokenType;
import org.sonar.sslr.channel.Channel;
import org.sonar.sslr.channel.CodeReader;

import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.commentRegexp;
import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.regexp;

public final class FlexLexer {

  private static final String EXP = "([Ee][+-]?+[0-9_]++)";

  private FlexLexer() {
  }

  public static Lexer create(Charset charset) {
    return Lexer.builder()
      .withCharset(charset)

      .withFailIfNoChannelToConsumeOneCharacter(true)

      .withChannel(new BomCharacterChannel())
      .withChannel(new BlackHoleChannel("\\s++"))

      // Comments
      .withChannel(commentRegexp("//[^\\n\\r]*+"))
      .withChannel(commentRegexp("/\\*[\\s\\S]*?\\*/"))

      // String Literals
      .withChannel(regexp(GenericTokenType.LITERAL, "\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\""))
      .withChannel(regexp(GenericTokenType.LITERAL, "\'([^\'\\\\]*+(\\\\[\\s\\S])?+)*+\'"))

      // Regular Expression Literal
      .withChannel(new FlexRegularExpressionLiteralChannel())

      // Numbers
      .withChannel(regexp(FlexTokenType.NUMERIC_LITERAL, "0[xX][0-9a-fA-F]++"))
      .withChannel(regexp(FlexTokenType.NUMERIC_LITERAL, "[0-9]++\\.([0-9]++)?+" + EXP + "?+"))
      .withChannel(regexp(FlexTokenType.NUMERIC_LITERAL, "\\.[0-9]++" + EXP + "?+"))
      .withChannel(regexp(FlexTokenType.NUMERIC_LITERAL, "[0-9]++" + EXP + "?+"))

      .withChannel(new IdentifierAndKeywordChannel("\\p{javaJavaIdentifierStart}++\\p{javaJavaIdentifierPart}*+", true, FlexKeyword.values()))
      .withChannel(new PunctuatorChannel(FlexPunctuator.values()))

      .withChannel(new UnknownCharacterChannel())

      .build();
  }

  private static class BomCharacterChannel extends Channel<Lexer> {

    @Override
    public boolean consume(CodeReader code, Lexer lexer) {
      if (code.peek() == '\ufeff') {
        code.pop();
        code.setColumnPosition(code.getColumnPosition() - 1);
        return true;
      } else {
        return false;
      }
    }
  }

}
