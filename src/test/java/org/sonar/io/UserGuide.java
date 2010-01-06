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

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import org.sonar.io.channel.AbstractTokenChannel;
import org.sonar.io.channel.BlackHoleChannel;
import org.sonar.io.channel.KeywordsChannel;
import org.sonar.io.channel.LitteralChannel;
import org.apache.commons.io.IOUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class UserGuide {
  @Test
  public void runDispatcher() {
    List<String> actual = new ArrayList<String>();
    List<Channel> channels = new ArrayList<Channel>();
    channels.add(new ATokenChannel(actual));
    ChannelDispatcher dispatcher = new ChannelDispatcher(channels);
    CodeReader code = new CodeReader(new StringReader("a string-to-be read-"));
    dispatcher.read(code);
    List<String> expected = Lists.newArrayList("a string", "to", "be read");

    assertThat(expected, is(actual));
  }


  @Test
  public void testCobolGrammar() throws IOException {
    List<String> actual = new ArrayList<String>();
    Set<String> keywords = getKeywords();

    List<Channel> channels = new ArrayList<Channel>();
    channels.add(new BlackHoleChannel(' ', '\t', '\f', ';', '\r', '\n'));
    channels.add(new KeywordsChannel(actual, keywords));
    channels.add(new LitteralChannel(actual, '\''));

    ChannelDispatcher dispatcher = new ChannelDispatcher(channels);
    String s = IOUtils.toString(getClass().getResourceAsStream("/org/sonar/io/coucou.txt"));
    CodeReader code = new CodeReader(new StringReader(s));
    dispatcher.read(code);

    List<String> expected = getExpectedResult();

    //assertThat(expected, is(actual));

  }

  private List<String> getExpectedResult() {
    return Lists.newArrayList("IDENTIFICATION", "ON", "DIVISION", "ON", "PROGRAM-ID", "DATE-WRITTEN", "ENVIRONMENT",

      "DIVISION", "ON", "CONFIGURATION", "ON", "SECTION", "ON", "INPUT-OUTPUT", "OUTPUT", "SECTION", "ON", "FILE-CONTROL", "CONTROL", "SELECT",
      "ASSIGN", "SIGN", "TO", "ORGANIZATION", "ON", "SEQUENTIAL", "FILE", "STATUS", "SELECT", "ASSIGN", "SIGN", "TO", "ORGANIZATION",
      "ON", "SEQUENTIAL", "FILE", "STATUS", "DATA", "DIVISION", "ON", "FILE", "SECTION", "ON", "FD", "LABEL", "RECORDS", "STANDARD",
      "RD", "MODE", "DE", "BLOCK", "LOCK", "CONTAINS", "RECORDS", "DATA", "RECORD", "RD", "PIC", "DATA", "PIC", "FD", "LABEL", "RECORD",
      "RD", "STANDARD", "RD", "BLOCK", "LOCK", "CONTAINS", "RECORDS", "MODE", "DE", "PIC", "WORKING-STORAGE", "SECTION", "ON", "ON", "COPY", "ON",
      "COPY", "PIC", "VALUE", "SPACE", "PIC", "COPY", "FILLER", "PIC", "CODE", "DE", "PIC", "VALUE", "ZERO", "FILE", "STATUS", "PIC", "PIC", "PIC",
      "VALUE", "ZERO", "NO", "VALUE", "NO", "VALUE", "PIC", "VALUE", "VALUE", "IN", "IN", "PIC", "PIC", "PIC", "IN", "PIC", "PIC", "DATE", "IN",
      "PIC", "PIC", "PIC", "PIC", "ON", "ON", "PIC", "VALUE", "PIC", "VALUE", "PIC", "VALUE", "PIC", "VALUE", "PIC", "VALUE", "PIC", "VALUE",
      "PIC", "PIC", "VALUE", "PIC", "VALUE", "PIC", "VALUE", "PIC", "IN", "PROCEDURE", "DIVISION", "ON", "ACCEPT", "IN", "FROM", "IN", "IF",
      "SPACE", "DISPLAY", "DISPLAY", "DE", "ON", "DISPLAY", "TO", "CODE", "DE", "SET", "NO", "TO", "TRUE", "END-IF", "IF", "IF", "NO",
      "PERFORM", "VARYING", "FROM", "BY", "UNTIL", "IN", "NOT", "SPACE", "OR", "END-PERFORM", "PERFORM", "IN", "TO", "ADD", "TO", "IN",
      "TO", "PERFORM", "VARYING", "IN", "FROM", "BY", "UNTIL", "IN", "NOT", "SPACE", "OR", "IN", "END-PERFORM", "PERFORM", "END-IF", "IF",
      "IF", "NO", "PERFORM", "THRU", "END-IF", "IF", "IF", "NO", "PERFORM", "THRU", "END-IF", "IF", "IF", "NO", "PERFORM", "THRU", "UNTIL",
      "NO", "OR", "END-IF", "IF", "IF", "NO", "CALL", "ALL", "FD", "USING", "END-IF", "IF", "PERFORM", "THRU", "CODE", "DE", "TO", "CODE", "DE",
      "STOP", "TOP", "RUN", "OPEN", "INPUT", "IF", "NOT", "DISPLAY", "DISPLAY", "STATUS", "DISPLAY", "TO", "CODE", "DE", "SET", "NO", "TO",
      "TRUE", "END-IF", "IF", "IF", "NO", "OPEN", "OUTPUT", "IF", "NOT", "DISPLAY", "DISPLAY", "STATUS", "DISPLAY", "TO", "CODE", "DE", "SET",
      "NO", "TO", "TRUE", "END-IF", "IF", "END-IF", "IF", "EXIT", "IF", "NO", "ON", "DE", "ON", "SPACE", "TO", "TO", "TO", "TO", "TO", "TO",
      "TO", "TO", "AT", "IF", "SPACES", "TO", "TO", "ELSE", "TO", "END-IF", "IF", "IF", "TO", "ELSE", "END-IF", "IF", "TO", "STRING", "IN",
      "DELIMITED", "BY", "SIZE", "INTO", "TO", "TO", "STRING", "DELIMITED", "BY", "SIZE", "INTO", "TO", "TO", "PERFORM", "THRU", "ON", "DE",
      "SPACE", "TO", "DATA", "TO", "DETAIL", "TO", "PERFORM", "THRU", "ON", "DE", "DE", "DATA", "TO", "DETAIL", "TO", "PERFORM", "THRU", "ON",
      "DE", "DATA", "TO", "DETAIL", "TO", "PERFORM", "THRU", "ON", "DE", "DATA", "TO", "DETAIL", "TO", "PERFORM", "THRU", "ON", "DE", "DATA", "TO",
      "DETAIL", "TO", "PERFORM", "THRU", "ON", "DE", "TO", "DATA", "TO", "DETAIL", "TO", "PERFORM", "THRU", "END-IF", "IF", "PERFORM", "THRU",
      "EXIT", "IF", "NOT", "READ", "INTO", "TO", "AT", "END", "SET", "TO", "TRUE", "HIGH-VALUE", "VALUE", "TO", "HIGH-VALUE", "VALUE", "TO", "NOT",
      "AT", "END", "ADD", "TO", "END-READ", "READ", "END-IF", "IF", "EXIT", "WRITE", "ADD", "TO", "EXIT", "CLOSE", "EXIT", "IN", "IF", "CODE", "DE",
      "NOT", "ZERO", "CODE", "DE", "TO", "CODE", "DE", "DISPLAY", "CODE", "DE", "CODE", "DE", "END-IF", "IF", "TO", "CODE", "DE",
      "PERFORM", "THRU", "STOP", "TOP", "RUN", "TO", "CODE", "DE", "PERFORM", "THRU", "STOP", "TOP", "RUN", "END", "PROGRAM");
  }

  public class ATokenChannel extends AbstractTokenChannel {
    public ATokenChannel(List<String> list) {
      super(list);
    }

    public boolean read(CodeReader code) {
      int nextChar = code.peek();
      StringWriter sw = new StringWriter();
      if (nextChar != '-') {
        while (nextChar != '-' && nextChar != -1) {
          code.pop(sw);
          nextChar = code.peek();
        }
        addToken(sw);
        return true;
      }
      return false;
    }
  }

  public Set<String> getKeywords() {
    return Sets.newHashSet(
      "ACCEPT",
      "ACCESS",
      "ADD",
      "ADVANCING",
      "AFTER",
      "ALL",
      "ALPHABETIC",
      "ALSO",
      "ALTER",
      "ALTERNATE",
      "AND",
      "ARE",
      "AREA",
      "AREAS",
      "ASCENDING",
      "ASSIGN",
      "AT",
      "AUTHOR",
      "BEFORE",
      "BLANK",
      "BLOCK",
      "BOTTOM",
      "BY",
      "CALL",
      "CANCEL",
      "CD",
      "CF",
      "CH",
      "CHARACTER",
      "CHARACTERS",
      "CLOCK-UNITS",
      "CLOSE",
      "COBOL",
      "CODE",
      "CODE-SET",
      "COLLATING",
      "COLUMN",
      "COMMA",
      "COMMUNICATION",
      "COMP",
      "COMP-0",
      "COMPUTATIONAL-0",
      "CONFIGURATION",
      "CONTAINS",
      "CONTROL",
      "CONTROL-AREA",
      "COPY",
      "CORR",
      "CORRESPONDING",
      "COUNT",
      "CURRENCY",
      "DATA",
      "DATE",
      "DATE-COMPILED",
      "DATE-WRITTEN",
      "DAY",
      "DE",
      "DEBUG-CONTENTS",
      "DEBUG-ITEM",
      "DEBUG-LINE",
      "DEBUG-NAME",
      "DEBUG-SUB-1",
      "DEBUG-SUB-2",
      "DEBUG-SUB-3",
      "DEBUGGING",
      "DECIMAL-POINT",
      "DECLARATIVES",
      "DELETE",
      "DELIMITED",
      "DELIMITER",
      "DEPENDING",
      "DESCENDING",
      "DESTINATION",
      "DETAIL",
      "DISABLE",
      "DISPLAY",
      "DIVIDE",
      "DIVISION",
      "DOWN",
      "DUPLICATES",
      "DYNAMIC",
      "EGI",
      "ELSE",
      "EMI",
      "ENABLE",
      "END",
      "END-OF-PAGE",
      "ENTER",
      "ENVIRONMENT",
      "EOP",
      "EQUAL",
      "ERROR",
      "ESI",
      "EVERY",
      "	EXCEPTION",
      "EXIT",
      "EXTEND",
      "FD",
      "FILE",
      "FILE-CONTROL",
      "FILLER",
      "FINAL",
      "FIRST",
      "FOOTING",
      "FOR",
      "FROM",
      "GENERATE",
      "GIVING",
      "GO",
      "GREATER",
      "GROUP",
      "HEADING",
      "HIGH-VALUE",
      "HIGH-VALUES",
      "I-O",
      "I-O-CONTROL",
      "IDENTIFICATION",
      "IF",
      "IN",
      "INDEX",
      "INDEXED",
      "INDICATE",
      "INITIAL",
      "INITIATE",
      "INPUT",
      "INPUT-OUTPUT",
      "INSPECT",
      "INSTALLATION",
      "INTO",
      "INVALID",
      "JUST",
      "JUSTIFIED",
      "KEY",
      "LABEL",
      "LAST",
      "LEADING",
      "LEFT",
      "LENGTH",
      "LESS",
      "LIMIT",
      "LIMITS",
      "LINAGE",
      "LINAGE-COUNTER",
      "LINE",
      "LINE-COUNTER",
      "LINES",
      "LINKAGE",
      "LOCK",
      "LOW-VALUE",
      "LOW-VALUES",
      "MEMORY",
      "MERGE",
      "MESSAGE",
      "MODE",
      "MODULES",
      "MORE-LABELS",
      "MULTIPLE",
      "MULTIPLY",
      "NATIVE",
      "NEGATIVE",
      "NEXT",
      "NO",
      "NOT",
      "NUMBER",
      "NUMERIC",
      "OBJECT-COMPUTER",
      "OCCURS",
      "OF",
      "OFF",
      "OMITTED",
      "ON",
      "OPEN",
      "OPTIONAL",
      "OR",
      "ORGANIZATION",
      "OUTPUT",
      "OVERFLOW",
      "PAGE",
      "PAGE-COUNTER",
      "PERFORM",
      "PF",
      "PH",
      "PIC",
      "PICTURE",
      "PLUS",
      "POINTER",
      "POSITION",
      "POSITIVE",
      "PRINTING",
      "PROCEDURE",
      "PROCEDURES",
      "PROCEED",
      "PROGRAM",
      "PROGRAM-ID",
      "QUEUE",
      "QUOTE",
      "	QUOTES",
      "RANDOM",
      "RD",
      "READ",
      "RECEIVE",
      "RECORD",
      "RECORDS",
      "REDEFINES",
      "REEL",
      "REFERENCES",
      "RELATIVE",
      "RELEASE",
      "REMAINDER",
      "REMOVAL",
      "RENAMES",
      "REPLACING",
      "REPORT",
      "REPORTING",
      "REPORTS",
      "RERUN",
      "RESERVE",
      "RETURN",
      "REVERSED",
      "REWIND",
      "REWRITE",
      "RF",
      "RH",
      "RIGHT",
      "ROUNDED",
      "RUN",
      "SAME",
      "SD",
      "SEARCH",
      "SECTION",
      "SECURITY",
      "SEGMENT",
      "SEGMENT-LIMIT",
      "SELECT",
      "SEND",
      "SENTENCE",
      "SEPARATE",
      "SEQUENCE",
      "SEQUENTIAL",
      "SET",
      "SIGN",
      "SIZE",
      "SORT",
      "SORT-MERGE",
      "SOURCE",
      "SOURCE-COMPUTER",
      "SPACE",
      "SPACES",
      "SPECIAL-NAMES",
      "STANDARD",
      "STANDARD-1",
      "START",
      "STATUS",
      "STOP",
      "STRING",
      "SUB-QUEUE-1",
      "SUB-QUEUE-2",
      "SUB-QUEUE-3",
      "SUBTRACT",
      "SUM",
      "SUPER",
      "SYMBOLIC",
      "SYNC",
      "SYNCHRONIZED",
      "TALLYING",
      "TAPE",
      "TERMINAL",
      "TERMINATE",
      "TEXT",
      "THAN",
      "THROUGH",
      "THRU",
      "TIME",
      "TIMES",
      "TO",
      "TOP",
      "TRAILING",
      "TYPE",
      "UNIT",
      "UNSTRING",
      "UNTIL",
      "UP",
      "UPON",
      "USAGE",
      "USE",
      "USING",
      "VALUE",
      "VALUES",
      "VARYING",
      "WHEN",
      "WITH",
      "WORDS",
      "WORKING-STORAGE",
      "WRITE",
      "ZERO",
      "ZEROES",
      "ZEROS",
      "ALPHABET",
      "ALPHABETIC-LOWER",
      "ALPHABETIC-UPPER",
      "ALPHANUMERIC",
      "ALPHANUMERIC-EDITED",
      "ANY",
      "BINARY",
      "CLASS",
      "COMMON",
      "CONTENT",
      "CONTINUE",
      "CONVERTING",
      "DAY-OF-WEEK",
      "END-ADD",
      "END-CALL",
      "END-COMPUTE",
      "	END-DELETE",
      "END-DIVIDE",
      "END-EVALUATE",
      "END-IF",
      "END-MULTIPLY",
      "END-PERFORM",
      "END-READ",
      "END-RECEIVE",
      "END-RETURN",
      "END-REWRITE",
      "END-SEARCH",
      "END-START",
      "END-STRING",
      "END-SUBTRACT",
      "END-UNSTRING",
      "END-WRITE",
      "EVALUATE",
      "EXTERNAL",
      "	FALSE",
      "FUNCTION",
      "GLOBAL",
      "INITIALIZE",
      "NUMERIC-EDITED",
      "ORDER",
      "OTHER",
      "PACKED-DECIMAL",
      "PADDING",
      "PURGE",
      "REFERENCE",
      "REPLACE",
      "STANDARD-2",
      "TEST",
      "THEN",
      "TRUE"
    );
  }
}
