/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * sonarqube@googlegroups.com
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

import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.impl.Lexer;
import net.sourceforge.pmd.cpd.SourceCode;
import net.sourceforge.pmd.cpd.TokenEntry;
import net.sourceforge.pmd.cpd.Tokenizer;
import net.sourceforge.pmd.cpd.Tokens;
import org.sonar.flex.FlexConfiguration;
import org.sonar.flex.api.FlexKeyword;
import org.sonar.flex.api.FlexPunctuator;
import org.sonar.flex.api.FlexTokenType;
import org.sonar.flex.lexer.FlexLexer;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;

public class FlexTokenizer implements Tokenizer {

  private static final String NORMALIZED_CHARACTER_LITERAL = "$CHARS";
  private static final String NORMALIZED_NUMERIC_LITERAL = "$NUMBER";
  private final Charset charset;

  public FlexTokenizer(Charset charset) {
    this.charset = charset;
  }

  /**
   * Cuts the given source into a list of tokens.
   */
  @Override
  public final void tokenize(SourceCode source, Tokens cpdTokens) {
    String fileName = source.getFileName();
    if (fileName.endsWith(".mxml")) {
      cpdTokens.add(TokenEntry.getEOF());
      return;
    }
    Lexer lexer = FlexLexer.create(new FlexConfiguration(charset));
    Iterator<Token> iterator = lexer.lex(new File(fileName)).iterator();
    // we currently use this hack to remove "import" directives
    boolean importDirective = false;
    while (iterator.hasNext()) {
      Token token = iterator.next();
      if (token.getType() == FlexKeyword.IMPORT) {
        importDirective = true;
      } else if (importDirective) {
        // We do nothing as we want to ignore "import" directives
        if (token.getType() == FlexPunctuator.SEMI) {
          importDirective = false;
        }
      } else if (token.getType() != GenericTokenType.EOF) {
        cpdTokens.add(new TokenEntry(getTokenImage(token), fileName, token.getLine()));
      }
    }
    cpdTokens.add(TokenEntry.getEOF());
  }

  private static String getTokenImage(Token token) {
    if (token.getType() == GenericTokenType.LITERAL) {
      return NORMALIZED_CHARACTER_LITERAL;
    } else if (token.getType() == FlexTokenType.NUMERIC_LITERAL) {
      return NORMALIZED_NUMERIC_LITERAL;
    }
    return token.getValue();
  }

}
