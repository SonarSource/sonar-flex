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
package org.sonar.flex.lexer;

import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.BlackHoleChannel;
import com.sonar.sslr.impl.channel.IdentifierAndKeywordChannel;
import com.sonar.sslr.impl.channel.PunctuatorChannel;
import org.sonar.flex.FlexConfiguration;
import org.sonar.flex.api.FlexKeyword;
import org.sonar.flex.api.FlexPunctuator;
import org.sonar.flex.api.FlexTokenType;

import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.commentRegexp;
import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.regexp;

public final class FlexLexer {

  private static final String EXP = "([Ee][+-]?+[0-9_]++)";

  private FlexLexer() {
  }

  public static Lexer create() {
    return create(new FlexConfiguration());
  }

  public static Lexer create(FlexConfiguration conf) {
    return Lexer.builder()
        .withCharset(conf.getCharset())

        .withFailIfNoChannelToConsumeOneCharacter(true)

        // Comments
        .withChannel(commentRegexp("//[^\\n\\r]*+"))
        .withChannel(commentRegexp("/\\*[\\s\\S]*?\\*/"))

        // String Literals
        .withChannel(regexp(GenericTokenType.LITERAL, "\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\""))

        // Numbers
        .withChannel(regexp(FlexTokenType.NUMERIC_LITERAL, "[0-9]++\\.([0-9]++)?+" + EXP + "?+"))
        .withChannel(regexp(FlexTokenType.NUMERIC_LITERAL, "\\.[0-9]++" + EXP + "?+"))
        .withChannel(regexp(FlexTokenType.NUMERIC_LITERAL, "[0-9]++" + EXP + "?+"))

        .withChannel(new IdentifierAndKeywordChannel("\\p{javaJavaIdentifierStart}++\\p{javaJavaIdentifierPart}*+", true, FlexKeyword.values()))
        .withChannel(new PunctuatorChannel(FlexPunctuator.values()))

        .withChannel(new BlackHoleChannel("[\\s]"))

        // TODO temporary
        .withChannel(regexp(FlexTokenType.UNKNOWN, "."))

        .build();
  }

}
