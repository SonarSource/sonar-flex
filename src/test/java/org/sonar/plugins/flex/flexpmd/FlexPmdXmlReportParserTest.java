/*
 * Sonar Flex Plugin
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

package org.sonar.plugins.flex.flexpmd;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;

import java.io.File;

public class FlexPmdXmlReportParserTest {

  @Test
  public void shouldSaveViolationsOnClasses() {
    File xmlFile = FileUtils.toFile(getClass().getResource("/org/sonar/plugins/flex/flexpmd/flexpmd-result-file.xml"));

    SensorContext context = mock(SensorContext.class);

    FlexPmdXmlReportParser parser = spy(new FlexPmdXmlReportParser(null, context, null));
    doNothing().when(parser).log((SensorContext) anyObject(), anyString(), anyString(), anyInt(), anyString());

    parser.parse(xmlFile);

    verify(parser, times(18)).log(eq(context), anyString(), anyString(), anyInt(), anyString());
    verify(parser, times(3)).log(eq(context), anyString(), eq("E:/TestHudson/flex/src/com/almirun/common/events/LoremIpsumEvent.as"),
        anyInt(), anyString());
    verify(parser).log(eq(context), eq("com.adobe.ac.pmd.rules.event.EventMissingCloneFunctionRule"),
        eq("E:/TestHudson/flex/src/com/almirun/common/events/LoremIpsumEvent.as"), eq(7), anyString());
    verify(parser).log(eq(context), eq("com.adobe.ac.pmd.rules.maintanability.ArrayFieldWithNoArrayElementTypeRule"),
        eq("E:/TestHudson/flex/src/com/almirun/common/events/LoremIpsumEvent.as"), eq(18), anyString());
  }
}
