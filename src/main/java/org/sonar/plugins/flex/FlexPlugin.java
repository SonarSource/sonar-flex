/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.plugins.flex;

import org.sonar.plugins.flex.colorizer.FlexColorizerFormat;
import org.sonar.plugins.flex.squid.FlexNoSonarFilter;
import org.sonar.plugins.flex.squid.FlexSquidSensor;
import org.sonar.plugins.flex.flexmetrics.*;
import org.sonar.plugins.flex.flexpmd.FlexPmdSensor;
import org.sonar.plugins.flex.flexpmd.FlexPmdMavenPluginHandler;
import org.sonar.plugins.flex.flexpmd.FlexPmdRulesRepository;
import org.sonar.plugins.flex.cpd.FlexCpdMavenSensor;
import org.sonar.plugins.flex.cpd.FlexCpdMavenPluginHandler;
import org.sonar.api.Extension;
import org.sonar.api.Plugin;
import org.sonar.api.Properties;
import org.sonar.api.Property;

import java.util.ArrayList;
import java.util.List;

@Properties({
  @Property(key = FlexPlugin.FILE_SUFFIXES_KEY, defaultValue = FlexPlugin.FILE_SUFFIXES_DEFVALUE, name = "File suffixes",
      description = "Comma-separated list of suffixes for files to analyze. To not filter, leave the list empty.",
  global=true,
  project = true)
})
public class FlexPlugin implements Plugin {
  public final static String FLEXPMD_PLUGIN = "flexplugin";

  static final String FILE_SUFFIXES_KEY = "sonar.flex.file.suffixes";
  static final String FILE_SUFFIXES_DEFVALUE = "as";

  // the flexpmd rules engine and the flex plugin must have the same key due to sonar-1342
  public String getKey() {
    return FLEXPMD_PLUGIN;
  }

  public String getName() {
    return "Flex";
  }

  public String getDescription() {
    return "Analysis of Flex projects";
  }

  public List<Class<? extends Extension>> getExtensions() {
    List<Class<? extends Extension>> list = new ArrayList<Class<? extends Extension>>();

    list.add(FlexColorizerFormat.class);
    list.add(Flex.class);
    list.add(FlexNoSonarFilter.class);
    list.add(FlexSourceImporter.class);

    list.add(FlexCpdMavenSensor.class);
    list.add(FlexCpdMavenPluginHandler.class);

    list.add(FlexMetricsMavenPluginHandler.class);
    list.add(FlexMetricsSensor.class);

    list.add(FlexPmdSensor.class);
    list.add(FlexPmdMavenPluginHandler.class);
    list.add(FlexPmdRulesRepository.class);


    list.add(FlexSquidSensor.class);

    return list;
  }
}
