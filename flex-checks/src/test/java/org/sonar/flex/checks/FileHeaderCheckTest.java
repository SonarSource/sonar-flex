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
package org.sonar.flex.checks;

import com.google.common.base.Charsets;
import org.junit.Test;
import org.sonar.flex.FlexAstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import java.io.File;

public class FileHeaderCheckTest {
  private static final File FILE1 = new File("src/test/resources/checks/headercheck/file1.as");
  private static final File FILE2 = new File("src/test/resources/checks/headercheck/file2.as");
  private static final File FILE3 = new File("src/test/resources/checks/headercheck/file3.as");

  @Test
  public void test() {
    FileHeaderCheck check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2005";

    SourceFile file = FlexAstScanner.scanSingleFile(FILE1, check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .noMore();

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 20\\d\\d";

    file = FlexAstScanner.scanSingleFile(FILE1, check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(null);

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2005";

    file = FlexAstScanner.scanSingleFile(FILE2, check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(null).withMessage("Add or update the header of this file.");

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2012";

    file = FlexAstScanner.scanSingleFile(FILE2, check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .noMore();

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2012\n// foo";

    file = FlexAstScanner.scanSingleFile(FILE2, check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .noMore();

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2012\r\n// foo";

    file = FlexAstScanner.scanSingleFile(FILE2, check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .noMore();

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2012\r// foo";

    file = FlexAstScanner.scanSingleFile(FILE2, check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .noMore();

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2012\r\r// foo";

    file = FlexAstScanner.scanSingleFile(FILE2, check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(null);

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2012\n// foo\n\n\n\n\n\n\n\n\n\ngfoo";

    file = FlexAstScanner.scanSingleFile(FILE2, check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(null);

    check = new FileHeaderCheck();
    check.headerFormat = "/*foo http://www.example.org*/";

    file = FlexAstScanner.scanSingleFile(FILE3, check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .noMore();
  }

  @Test
  public void regular_expression() throws Exception {
    String headerFormat = "// copyright 20[0-9][0-9]";
    assertHasIssue(createCheck(headerFormat, false), FILE1);
    assertHasIssue(createCheck(headerFormat, false), FILE3);
    assertNoIssue(createCheck(headerFormat, true), FILE1);
    assertHasIssue(createCheck(headerFormat, true), FILE3);
    assertHasIssue(createCheck("copyright", true), FILE1);
    assertHasIssue(createCheck("// copyright", false), FILE1);
    assertHasIssue(createCheck("// copyright", true), FILE1);
    assertNoIssue(createCheck("// copyright 2005", true), FILE1);
    assertNoIssue(createCheck("// copyright 2005", false), FILE1);

    File FILE1_WIN = new File("src/test/resources/checks/headercheck/file1_win.as");
    assertHasIssue(createCheck("// copyright", false), FILE1_WIN);
    assertHasIssue(createCheck("// copyright", true), FILE1_WIN);
    assertNoIssue(createCheck("// copyright 2005", true), FILE1_WIN);
    assertNoIssue(createCheck("// copyright 2005", false), FILE1_WIN);
  }

  private FileHeaderCheck createCheck(String format, boolean isRegularExpression) {
    FileHeaderCheck check = new FileHeaderCheck();
    check.headerFormat = format;
    check.isRegularExpression = isRegularExpression;
    return check;
  }

  private void assertHasIssue(FileHeaderCheck check, File file) {
    SourceFile sourceFile = FlexAstScanner.scanSingleFile(file, check);
    CheckMessagesVerifier.verify(sourceFile.getCheckMessages()).next().atLine(null);
  }

  private void assertNoIssue(FileHeaderCheck check, File file) {
    SourceFile sourceFile = FlexAstScanner.scanSingleFile(file, check);
    CheckMessagesVerifier.verify(sourceFile.getCheckMessages()).noMore();
  }

  @Test(expected = IllegalStateException.class)
  public void ioexception_with_plaintext() throws Exception {
    FileHeaderCheck check = new FileHeaderCheck();
    check.setCharset(Charsets.UTF_8);
    check.matchesPlainTextHeader(new File("unknown"));
  }

  @Test(expected = IllegalStateException.class)
  public void ioexception_with_regexp() throws Exception {
    FileHeaderCheck check = new FileHeaderCheck();
    check.setCharset(Charsets.UTF_8);
    check.matchesRegularExpression(new File("unknown"));
  }

}
