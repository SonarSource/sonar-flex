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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.resources.Directory;
import org.sonar.api.resources.File;

public class FlexResourceBridgeTest {

  private FlexResourceBridge resourceBridge;

  @Before
  public void setup() throws Exception {
    resourceBridge = new FlexResourceBridge();
    resourceBridge.indexFile(new File("org/sonar/test/Bar.as"));
    resourceBridge.indexFile(new File("org/sonar/test/Foo.swc"));
  }

  @Test
  public void shouldFindFileForClassName() throws Exception {
    assertThat(resourceBridge.findFile("org.sonar.test.Bar"), is(new File("org/sonar/test/Bar.as")));
  }

  @Test
  public void shouldFindFileForMethodName() throws Exception {
    assertThat(resourceBridge.findFile("org.sonar.test.Foo::main"), is(new File("org/sonar/test/Foo.swc")));
  }

  @Test
  public void shouldFindDirectoryForPacakgeName() throws Exception {
    assertThat(resourceBridge.findDirectory("org.sonar.test"), is(new Directory("org/sonar/test")));
  }

  @Test(expected = IllegalStateException.class)
  public void shouldNotIndexFileIfBridgeLocked() throws Exception {
    FlexResourceBridge bridge = new FlexResourceBridge();
    bridge.indexFile(new File("org/sonar/test/Bar.as"));
    bridge.lock();
    bridge.indexFile(new File("org/sonar/test/Foo.swc"));
  }

}
