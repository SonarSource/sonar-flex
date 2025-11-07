/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
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
