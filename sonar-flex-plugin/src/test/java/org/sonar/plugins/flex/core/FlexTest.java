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
package org.sonar.plugins.flex.core;

import org.junit.jupiter.api.Test;
import org.sonar.api.config.Configuration;
import org.sonar.api.config.internal.ConfigurationBridge;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.plugins.flex.FlexPlugin;

import static org.assertj.core.api.Assertions.assertThat;

public class FlexTest {

  @Test
  public void testGetFileSuffixes() {
    MapSettings settings = new MapSettings();
    Configuration config = new ConfigurationBridge(settings);
    Flex flex = new Flex(config);

    assertThat(flex.getFileSuffixes()).isEqualTo(new String[] {"as"});

    settings.setProperty(FlexPlugin.FILE_SUFFIXES_KEY, "");
    assertThat(flex.getFileSuffixes()).isEqualTo(new String[] {"as"});

    settings.setProperty(FlexPlugin.FILE_SUFFIXES_KEY, "as, swc");
    assertThat(flex.getFileSuffixes()).isEqualTo(new String[] {"as", "swc"});

    settings.setProperty(FlexPlugin.FILE_SUFFIXES_KEY, ",as, ,, swc,");
    assertThat(flex.getFileSuffixes()).isEqualTo(new String[] {"as", "swc"});
  }

}
