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

import java.util.List;

public class CobolTokenizerState {

  private List<String> lines;
  private int currentLineIndex;
  private int currentColumnIndex;
  private String fileName;
  private String currentLine;

  CobolTokenizerState(SourceCode tokens) {
    lines = tokens.getCode();
    fileName = tokens.getFileName();

    currentLineIndex = 0;
    currentColumnIndex = -1;
  }

  int getTotalLines() {
    return lines.size();
  }

  boolean hasNextLine() {
    if (currentLineIndex < getTotalLines()) {
      return true;
    }
    return false;
  }

  String getLine() {
    return currentLine;
  }

  String nextLine() {
    currentColumnIndex = -1;
    currentLineIndex++;
    currentLine = lines.get(currentLineIndex - 1);
    return getLine();
  }

  char nextCharInLine() {
    currentColumnIndex++;
    return getChar();
  }

  char getChar() {
    return getLine().charAt(currentColumnIndex);
  }

  int getLineIndex() {
    return currentLineIndex;
  }

  String getFileName() {
    return fileName;
  }

  int getCharIndex() {
    return currentColumnIndex;
  }

  boolean hasNextChar() {
    return currentColumnIndex < getLine().length() - 1;
  }

  public void nextCharInLine(int length) {
    for (int i = 0; i < length; i++) {
      nextCharInLine();
    }
  }

  public char previousCharInLine() {
    currentColumnIndex--;
    return getChar();
  }
}
