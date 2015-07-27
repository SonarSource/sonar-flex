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
package org.sonar.plugins.flex.core;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.resources.Directory;
import org.sonar.api.resources.File;

import static org.fest.assertions.Assertions.assertThat;

public class FlexResourceBridgeTest {

  private FlexResourceBridge resourceBridge;

  @Before
  public void setup() throws Exception {
    resourceBridge = new FlexResourceBridge();
    resourceBridge.indexFile(new File("org/sonar/test/Bar.as"));
    resourceBridge.indexFile(new File("org/sonar/test/Foo.as"));
    resourceBridge.indexFile(new File("org/sonar/test/Foo.mxml"));
  }

  @Test
  public void shouldFindFileForClassName() throws Exception {
    assertThat(resourceBridge.findFile("org.sonar.test.Bar")).isEqualTo(new File("org/sonar/test/Bar.as"));
  }

  @Test
  public void shouldFindFileForMethodName() throws Exception {
    assertThat(resourceBridge.findFile("org.sonar.test.Foo::main")).isEqualTo(new File("org/sonar/test/Foo.as"));
  }

  @Test
  public void shouldFindDirectoryForPacakgeName() throws Exception {
    assertThat(resourceBridge.findDirectory("org.sonar.test")).isEqualTo(new Directory("org/sonar/test"));
  }

  @Test
  public void shouldKeepExtensionForFilesOtherThanAS() throws Exception {
    assertThat(resourceBridge.findFile("org.sonar.test.Foo.mxml")).isEqualTo(new File("org/sonar/test/Foo.mxml"));
  }

  @Test(expected = IllegalStateException.class)
  public void shouldNotIndexFileIfBridgeLocked() throws Exception {
    FlexResourceBridge bridge = new FlexResourceBridge();
    bridge.indexFile(new File("org/sonar/test/Bar.as"));
    bridge.lock();
    bridge.indexFile(new File("org/sonar/test/Foo.as"));
  }

}
