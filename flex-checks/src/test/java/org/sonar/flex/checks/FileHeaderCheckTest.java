/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonar.flex.checks;

import java.io.File;
import org.junit.jupiter.api.Test;

public class FileHeaderCheckTest {
  private static final File FILE1 = new File("src/test/resources/checks/headercheck/file1.as");
  private static final File FILE2 = new File("src/test/resources/checks/headercheck/file2.as");
  private static final File FILE3 = new File("src/test/resources/checks/headercheck/file3.as");

  @Test
  public void plain_text_header() {
    assertNoIssue(createCheck("// copyright 2005", false), FILE1);
    assertHasIssue(createCheck("// copyright 20\\d\\d", false), FILE1);
    assertHasIssue(createCheck("// copyright 2005", false), FILE2);
    assertNoIssue(createCheck("// copyright 2012", false), FILE2);
    assertNoIssue(createCheck("// copyright 2012\n// foo", false), FILE2);
    assertNoIssue(createCheck("// copyright 2012\r\n// foo", false), FILE2);
    assertNoIssue(createCheck("// copyright 2012\r// foo", false), FILE2);
    assertHasIssue(createCheck("// copyright 2012\r\r// foo", false), FILE2);
    assertHasIssue(createCheck("// copyright 2012\n// foo\n\n\n\n\n\n\n\n\n\ngfoo", false), FILE2);
    assertNoIssue(createCheck("/*foo http://www.example.org*/", false), FILE3);
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
    FlexVerifier.verifySingleIssueOnFile(file, check, "Add or update the header of this file.");
  }

  private void assertNoIssue(FileHeaderCheck check, File file) {
    FlexVerifier.verifyNoIssue(file, check);
  }

}
