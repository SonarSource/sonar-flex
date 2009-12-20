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

import java.util.ArrayList;
import java.util.List;

public class CobolTokenizerConfiguration {

  public List<String> stringTokens;
  public List<String> ignorableCharacters;
  public List<String> ignorableTokens;
  public List<String> ignorableFromTokensToStmtEnd;
  public String stmtEndDelimiter = ";";
  public List<String> oneLineCommentDelimiters;
  public List<String> multiLinesCommentDelimiters;
  public boolean mustIgnoreString = false;
  public boolean mustIgnoreComment = false;

  public CobolTokenizerConfiguration() {
    // if you want to ignore String token
    mustIgnoreString = true;
    // if you want to ignore Comments
    mustIgnoreComment = true;

    // setting markers for "string" in Flex
    stringTokens = new ArrayList<String>();
    stringTokens.add("\'");

    // setting delimiters for one line comment in Flex
    oneLineCommentDelimiters = new ArrayList<String>();
    oneLineCommentDelimiters.add("      *");

    // setting delimiters for multi-lines comment in plsql
    multiLinesCommentDelimiters = new ArrayList<String>();
    multiLinesCommentDelimiters.add("/*");
    multiLinesCommentDelimiters.add("*/");


    // setting markers for 'ignorable character' in plsql
    ignorableCharacters = new ArrayList<String>();
    ignorableCharacters.add(";");
    ignorableCharacters.add("(");
    ignorableCharacters.add(")");
    ignorableCharacters.add(",");
    ignorableCharacters.add("=");

    // setting markers for 'ignorable token' in plsql
    ignorableTokens = new ArrayList<String>();
    ignorableTokens.add("declare");
    ignorableTokens.add("begin");
    ignorableTokens.add("end");
    ignorableTokens.add("");

    // setting markers for 'ignorable statement' in plsql
    ignorableFromTokensToStmtEnd = new ArrayList<String>();
    ignorableFromTokensToStmtEnd.add("cursor");


    // setting statements delimiter in Flex
    stmtEndDelimiter = ".";
  }
}
