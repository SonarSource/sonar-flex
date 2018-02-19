/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2018 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.flex.checks;

import java.io.File;
import org.junit.Test;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import static org.sonar.flex.checks.FlexCheckTester.checkMessages;

public class FileHeaderCheckTest {
  private static final File FILE1 = new File("src/test/resources/checks/headercheck/file1.as");
  private static final File FILE2 = new File("src/test/resources/checks/headercheck/file2.as");
  private static final File FILE3 = new File("src/test/resources/checks/headercheck/file3.as");

  @Test
  public void test() {
    FileHeaderCheck check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2005";

    CheckMessagesVerifier.verify(checkMessages(FILE1, check)).noMore();

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 20\\d\\d";

    CheckMessagesVerifier.verify(checkMessages(FILE1, check))
      .next().atLine(null);

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2005";

    CheckMessagesVerifier.verify(checkMessages(FILE2, check))
      .next().atLine(null).withMessage("Add or update the header of this file.");

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2012";

    CheckMessagesVerifier.verify(checkMessages(FILE2, check))
      .noMore();

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2012\n// foo";

    CheckMessagesVerifier.verify(checkMessages(FILE2, check))
      .noMore();

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2012\r\n// foo";

    CheckMessagesVerifier.verify(checkMessages(FILE2, check))
      .noMore();

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2012\r// foo";

    CheckMessagesVerifier.verify(checkMessages(FILE2, check))
      .noMore();

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2012\r\r// foo";

    CheckMessagesVerifier.verify(checkMessages(FILE2, check))
      .next().atLine(null);

    check = new FileHeaderCheck();
    check.headerFormat = "// copyright 2012\n// foo\n\n\n\n\n\n\n\n\n\ngfoo";

    CheckMessagesVerifier.verify(checkMessages(FILE2, check))
      .next().atLine(null);

    check = new FileHeaderCheck();
    check.headerFormat = "/*foo http://www.example.org*/";

    CheckMessagesVerifier.verify(checkMessages(FILE3, check))
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
    CheckMessagesVerifier.verify(checkMessages(file, check)).next().atLine(null);
  }

  private void assertNoIssue(FileHeaderCheck check, File file) {
    CheckMessagesVerifier.verify(checkMessages(file, check)).noMore();
  }

}
