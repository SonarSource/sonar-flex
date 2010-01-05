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
package org.sonar.io;

import java.io.*;

import org.apache.commons.io.IOUtils;

public class CodeReader {

  public static final int LAST_CHARS_MAX_SIZE = 20;
  public static final int PEEK_TO_WINDOW_SIZE = 20;

  private final Reader reader;
  private int[] lastChars = new int[LAST_CHARS_MAX_SIZE];

  public CodeReader(Reader reader) {
    this.reader = new BufferedReader(reader);
    for (int i = 0; i < lastChars.length; i++) {
      lastChars[i] = -1;
    }
  }

  private int read() throws IOException {
    if (!reader.ready()) {
      return -1;
    }
    return reader.read();
  }

  public final void mark(int readAheadLimit) throws IOException {
    if (reader.markSupported()) {
      throw new CodeReaderException("Mark are not supported on provided Reader.");
    }
    reader.mark(readAheadLimit);
  }

  public final int lastChar() {
    return lastChars[0];
  }

  public final int lastChar(int index) {
    return lastChars[index];
  }

  public final int peek() {
    try {
      reader.mark(1);
      int nextChar = read();
      reader.reset();
      return nextChar;
    }
    catch (IOException e) {
      throw new CodeReaderException("Unable to read on input stream.", e);
    }
  }

  public final void pop(Writer writer) {
    try {
      setLastChar(read());
      writer.write(lastChar());
    }
    catch (IOException e) {
      throw new CodeReaderException("Unable to read on input stream.", e);
    }
  }

  public final void pop() {
    try {
      setLastChar(read());
    }
    catch (IOException e) {
      throw new CodeReaderException("Unable to read on input stream.", e);
    }
  }

  private void setLastChar(int read) {
    for (int i = lastChars.length - 1; i > 0; i--) {
      lastChars[i] = lastChars[i - 1];
    }
    lastChars[0] = read;

  }

 /** public void popTo(EndMatcher matcher, Writer writer) {
    pop(writer);
    while (peek() != -1 && !matcher.read(peek()) && ) {
      pop(writer);
    }
  } **/

  public void close() {
    IOUtils.closeQuietly(reader);
  }

  public char[] peek(int index) {
    try {
      char[] result = new char[index];
      reader.mark(index);
      reader.read(result, 0, index);
      reader.reset();
      return result;
    }
    catch (IOException e) {
      throw new CodeReaderException("Unable to read on input stream.", e);
    }
  }
}