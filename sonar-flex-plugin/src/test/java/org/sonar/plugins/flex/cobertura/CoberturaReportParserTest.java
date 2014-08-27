/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
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
package org.sonar.plugins.flex.cobertura;

import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.utils.XmlParserException;
import org.sonar.test.TestUtils;

import static org.mockito.Mockito.mock;

public class CoberturaReportParserTest {

  @Test(expected = XmlParserException.class)
  public void invalidXmlFile() throws Exception {
    SensorContext context = mock(SensorContext.class);
    CoberturaReportPasrer.parseReport(TestUtils.getResource("org/sonar/plugins/flex/cobertura/coverage-invalid.xml"),
      context, new Project(""), mock(ModuleFileSystem.class));
  }
}
