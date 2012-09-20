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

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.Test;
import org.sonar.plugins.flex.FlexPlugin;

import static org.fest.assertions.Assertions.assertThat;

public class FlexTest {

  @Test
  public void testGetFileSuffixes() {
    Configuration configuration = new BaseConfiguration();
    Flex flex = new Flex(configuration);

    assertThat(flex.getFileSuffixes()).isEqualTo(new String[] {"as", "mxml"});

    configuration.setProperty(FlexPlugin.FILE_SUFFIXES_KEY, "");
    assertThat(flex.getFileSuffixes()).isEqualTo(new String[] {"as", "mxml"});

    configuration.setProperty(FlexPlugin.FILE_SUFFIXES_KEY, "as, swc");
    assertThat(flex.getFileSuffixes()).isEqualTo(new String[] {"as", "swc"});
  }

}
