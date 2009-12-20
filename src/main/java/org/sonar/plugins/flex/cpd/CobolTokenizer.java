/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.plugins.flex.cpd;

import net.sourceforge.pmd.cpd.SourceCode;
import net.sourceforge.pmd.cpd.TokenEntry;
import net.sourceforge.pmd.cpd.Tokenizer;
import net.sourceforge.pmd.cpd.Tokens;

public class CobolTokenizer implements Tokenizer {

  private final CobolTokenizerConfiguration configuration = new CobolTokenizerConfiguration();
  private CobolTokenizerState state;

  public final void tokenize(SourceCode tokens, Tokens tokenEntries) {
    state = new CobolTokenizerState(tokens);

    // analyze each line of the file
    while (state.hasNextLine()) {
      state.nextLine();

      // analyze each character of the line
      while (state.hasNextChar()) {

        String token = getNextTokenFromCurrentLine();

        if (isIgnorableStatements(token)) {
          goToEndOfStatement();
          continue;
        }
        if (isIgnorableToken(token)) {
          continue;
        }

        giveTokenToCPD(tokenEntries, token);

      }
    }
    tokenEntries.add(TokenEntry.getEOF());
  }

  private String getNextTokenFromCurrentLine() {
    StringBuilder token = new StringBuilder();
    while (state.hasNextChar()) {
      state.nextCharInLine();

      if (isTheEndOfToken(token)) {
        state.previousCharInLine();
        break;
      }
      if (isIgnorableChar()) {
        continue;
      }
      if (isMultiLinesCommentStart()) {
        return getMultiLinesCommentToken();
      } else if (isOneLineCommentStart()) {
        return getOneLineCommentToken();
      } else if (isStringDelimiter()) {
        return getStringToken();
      } else {
        token.append(state.getChar());
      }
    }
    return token.toString().toLowerCase();
  }

  private void giveTokenToCPD(Tokens tokenEntries, String token) {
    TokenEntry entry = new TokenEntry(token, state.getFileName(), state.getLineIndex());
    tokenEntries.add(entry);
  }

  private boolean isIgnorableStatements(String token) {
    return configuration.ignorableFromTokensToStmtEnd.contains(token);
  }

  private void goToEndOfStatement() {
    goToDelimiter(configuration.stmtEndDelimiter);
  }

  private boolean isTheEndOfToken(StringBuilder token) {
    return token.length() > 0
        && (isMultiLinesCommentStart() || isOneLineCommentStart() || isIgnorableChar() || isStringDelimiter());
  }

  private String getStringToken() {
    boolean escaped = false;
    StringBuilder token = new StringBuilder();
    while (state.hasNextChar()) {
      if (!isStringDelimiter()) {
        token.append(state.getChar());
      }
      state.nextCharInLine();
      if (escaped) {
        escaped = false;
      } else if ((isStringDelimiter() && (token.length() > 0))) {
        break;
      } else if (state.getChar() == '\\') {
        escaped = true;
      }
    }
    if (configuration.mustIgnoreString) {
      return "";
    } else {
      return token.toString();
    }
  }

  private boolean isIgnorableChar() {
    if (Character.isWhitespace(state.getChar())) {
      return true;
    }
    return this.configuration.ignorableCharacters.contains(String.valueOf(state.getChar()));
  }

  private boolean isStringDelimiter() {
    return configuration.stringTokens.contains("" + state.getChar());
  }

  private boolean isOneLineCommentStart() {
    for (String delimiter : configuration.oneLineCommentDelimiters) {
      if (state.getLine().startsWith(delimiter, state.getCharIndex())) {
        return true;
      }
    }
    return false;
  }

  private boolean isMultiLinesCommentStart() {
    if (state.getLine().startsWith(configuration.multiLinesCommentDelimiters.get(0), state.getCharIndex())) {
      return true;
    }
    return false;
  }

  private String getOneLineCommentToken() {
    String token = state.getLine().substring(state.getCharIndex());
    while (state.hasNextChar()) {
      state.nextCharInLine();
    }
    if (configuration.mustIgnoreComment) {
      return "";
    }
    return token;
  }

  private String getMultiLinesCommentToken() {
    String delimiter = configuration.multiLinesCommentDelimiters.get(1);
    String token = goToDelimiter(delimiter);
    if (configuration.mustIgnoreComment) {
      return "";
    }
    return token;
  }

  private String goToDelimiter(String delimiter) {
    StringBuilder token = new StringBuilder();
    while (!state.getLine().startsWith(delimiter, state.getCharIndex())) {
      if (state.hasNextChar()) {
        token.append(state.getChar());
        state.nextCharInLine();
      } else {
        state.nextLine();
        if (state.hasNextChar()) {
          state.nextCharInLine();
        }
      }
    }
    token.append(state.getLine().substring(state.getCharIndex(), delimiter.length() + state.getCharIndex()));
    state.nextCharInLine(delimiter.length() - 1);
    return token.toString();
  }

  private boolean isIgnorableToken(String token) {
    return this.configuration.ignorableTokens.contains(token);
  }
}
