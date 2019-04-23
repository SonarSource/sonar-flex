/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2019 SonarSource SA
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
package org.sonar.plugins.flex.cobertura;

import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sonar.api.batch.sensor.internal.SensorContextTester;

public class CoberturaReportParserTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void invalidXmlFile() throws Exception {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("com.ctc.wstx.exc.WstxEOFException: Unexpected EOF; was expecting a close tag for element <coverage>\n" +
      " at [row,col {unknown-source}]: [5,0]");

    CoberturaReportParser.parseReport(
      new File("src/test/resources/org/sonar/plugins/flex/cobertura/coverage-invalid.xml"),
      SensorContextTester.create(new File(".")));
  }

  @Test
  public void nonExistingFile() throws Exception {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("javax.xml.stream.XMLStreamException: java.io.FileNotFoundException: fakeFile.xml (No such file or directory)");

    CoberturaReportParser.parseReport(
      new File("fakeFile.xml"),
      SensorContextTester.create(new File(".")));
  }
}
