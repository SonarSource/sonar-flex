/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2024 SonarSource SA
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
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.sensor.internal.SensorContextTester;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoberturaReportParserTest {

  @Test
  public void invalidXmlFile() throws Exception {
    IllegalStateException e = assertThrows(IllegalStateException.class, () ->
      CoberturaReportParser.parseReport(
        new File("src/test/resources/org/sonar/plugins/flex/cobertura/coverage-invalid.xml"),
        SensorContextTester.create(new File("."))));
    assertTrue(e.getMessage().startsWith("com.ctc.wstx.exc.WstxEOFException: Unexpected EOF; was expecting a close tag for element " +
      "<coverage>"));
    assertTrue(e.getMessage().endsWith(" at [row,col {unknown-source}]: [5,0]"));
  }

  @Test
  public void nonExistingFile() {
    IllegalStateException e = assertThrows(IllegalStateException.class, () ->
      CoberturaReportParser.parseReport(
        new File("fakeFile.xml"),
        SensorContextTester.create(new File("."))));
    assertTrue(e.getMessage().startsWith("javax.xml.stream.XMLStreamException: java.io.FileNotFoundException: fakeFile.xml"));
  }
}
