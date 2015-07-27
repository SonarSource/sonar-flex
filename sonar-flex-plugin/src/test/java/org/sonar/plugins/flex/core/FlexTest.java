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

import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.plugins.flex.FlexPlugin;

import static org.fest.assertions.Assertions.assertThat;

public class FlexTest {

  @Test
  public void testGetFileSuffixes() {
    Settings settings = new Settings();
    Flex flex = new Flex(settings);

    assertThat(flex.getFileSuffixes()).isEqualTo(new String[] {"as"});

    settings.setProperty(FlexPlugin.FILE_SUFFIXES_KEY, "");
    assertThat(flex.getFileSuffixes()).isEqualTo(new String[] {"as"});

    settings.setProperty(FlexPlugin.FILE_SUFFIXES_KEY, "as, swc");
    assertThat(flex.getFileSuffixes()).isEqualTo(new String[] {"as", "swc"});
  }

}
