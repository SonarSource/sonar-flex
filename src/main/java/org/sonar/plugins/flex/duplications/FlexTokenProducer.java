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
package org.sonar.plugins.flex.duplications;

import org.sonar.plugins.flex.duplications.internal.TokenChunker;

/**
 * Reference for this AS3 lexer: http://livedocs.adobe.com/flex/3/html/help.html?content=03_Language_and_Syntax_01.html
 * 
 * Some approximations are done using the Java syntax.
 */
public final class FlexTokenProducer {

  private FlexTokenProducer() {
  }

  private static final String NORMALIZED_CHARACTER_LITERAL = "$CHARS";
  private static final String NORMALIZED_NUMERIC_LITERAL = "$NUMBER";

  private static final String EXP = "([Ee][+-]?+[0-9_]++)";

  /**
   * Creates a {@link TokenChunker} for the ActionScript language.
   * 
   * @return a TokenChunker
   */
  public static TokenChunker build() {
    return TokenChunker.builder()
        // White Space
        .ignore("\\s")
        // Comments
        .ignore("//[^\\n\\r]*+")
        .ignore("/\\*[\\s\\S]*?\\*/")
        // String Literals
        .token("\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\"", NORMALIZED_CHARACTER_LITERAL)
        // Identifiers, Keywords, Boolean Literals, The Null Literal
        .token("\\p{javaJavaIdentifierStart}++\\p{javaJavaIdentifierPart}*+")
        // Numbers
        .token("[0-9_]++\\.([0-9_]++)?+" + EXP + "?+", NORMALIZED_NUMERIC_LITERAL)
        .token("\\.[0-9_]++" + EXP + "?+", NORMALIZED_NUMERIC_LITERAL)
        .token("[0-9_]++" + EXP, NORMALIZED_NUMERIC_LITERAL)
        // Any other character
        .token(".")
        .build();
  }

}
