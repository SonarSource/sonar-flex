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

package org.sonar.plugins.flex.core;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FlexSourceImporterTest {

  private FlexSourceImporter importer;
  private FlexResourceBridge resourceBridge;

  @Before
  public void setup() throws Exception {
    resourceBridge = mock(FlexResourceBridge.class);
    importer = new FlexSourceImporter(new Flex(null), resourceBridge);
  }

  @Test
  public void testFlexSourceImporter() throws Exception {
    assertThat(importer.toString()).isEqualTo("Flex Source Importer");
  }

  @Test
  public void shouldIndexFile() throws Exception {
    File file = new File("target/file");
    importer.createResource(file, Lists.newArrayList(file.getParentFile()), false);
    verify(resourceBridge).indexFile(new org.sonar.api.resources.File("file"));
  }

  @Test
  public void shouldLockIndexOnFinish() throws Exception {
    importer.onFinished();
    verify(resourceBridge).lock();
  }

}
