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
package org.sonar.flex;

import com.google.common.base.Preconditions;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.RecognitionException;
import org.apache.commons.io.IOUtils;
import org.sonar.sslr.internal.matchers.AstCreator;
import org.sonar.sslr.internal.matchers.InputBuffer;
import org.sonar.sslr.internal.text.AbstractText;
import org.sonar.sslr.internal.text.LocatedText;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.parser.ParseError;
import org.sonar.sslr.parser.ParseRunner;
import org.sonar.sslr.parser.ParsingResult;
import org.sonar.sslr.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Duplicates corresponding class from SSLR in order to change formatting of error report.
 */
public class ParserAdapter2 extends org.sonar.sslr.parser.ParserAdapter<LexerlessGrammar> {

  private final Charset charset;
  private final ParseRunner parseRunner;

  public ParserAdapter2(Charset charset, LexerlessGrammar grammar) {
    super(charset, grammar);
    this.charset = Preconditions.checkNotNull(charset, "charset");
    this.parseRunner = new ParseRunner(grammar.getRootRule());
  }

  @Override
  public AstNode parse(String source) {
    Text text = new LocatedText(null, source.toCharArray());
    return parse(text);
  }

  @Override
  public AstNode parse(File file) {
    Text text = new LocatedText(file, fileToCharArray(file, charset));
    return parse(text);
  }

  private static char[] fileToCharArray(File file, Charset charset) {
    FileInputStream is = null;
    try {
      is = new FileInputStream(file);
      return IOUtils.toCharArray(is, charset.name());
    } catch (IOException e) {
      throw new RecognitionException(0, e.getMessage(), e);
    } finally {
      IOUtils.closeQuietly(is);
    }
  }

  private AstNode parse(Text input) {
    char[] chars = ((AbstractText) input).toChars();
    ParsingResult result = parseRunner.parse(chars);
    if (result.isMatched()) {
      return AstCreator.create(result, input);
    } else {
      ParseError parseError = result.getParseError();
      InputBuffer inputBuffer = parseError.getInputBuffer();
      int line = inputBuffer.getPosition(parseError.getErrorIndex()).getLine();
      String message = new ParseErrorFormatter2().format(parseError);
      throw new RecognitionException(line, message);
    }
  }

}
