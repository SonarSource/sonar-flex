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

package org.sonar.plugins.flex;

import org.sonar.api.Extension;
import org.sonar.api.Plugin;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.plugins.flex.cobertura.FlexCoberturaSensor;
import org.sonar.plugins.flex.colorizer.FlexColorizerFormat;
import org.sonar.plugins.flex.cpd.FlexCpdMavenPluginHandler;
import org.sonar.plugins.flex.cpd.FlexCpdMavenSensor;
import org.sonar.plugins.flex.flexmetrics.FlexMetricsMavenPluginHandler;
import org.sonar.plugins.flex.flexmetrics.FlexMetricsSensor;
import org.sonar.plugins.flex.flexpmd.*;
import org.sonar.plugins.flex.squid.FlexNoSonarFilter;
import org.sonar.plugins.flex.squid.FlexSquidSensor;
import org.sonar.plugins.flex.surefire.FlexMojosMavenPluginHandler;
import org.sonar.plugins.flex.surefire.FlexSurefireSensor;

import java.util.ArrayList;
import java.util.List;

@Properties({ @Property(
    key = FlexPlugin.FILE_SUFFIXES_KEY,
    defaultValue = FlexPlugin.FILE_SUFFIXES_DEFVALUE,
    name = "File suffixes",
    description = "Comma-separated list of suffixes for files to analyze. To not filter, leave the list empty.",
    global = true,
    project = true) })
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
    list.add(FlexPmdRuleRepository.class);
    list.add(DefaultFlexProfile.class);
    list.add(FlexPmdProfileExporter.class);
    list.add(FlexPmdProfileImporter.class);

    list.add(FlexSurefireSensor.class);
    list.add(FlexMojosMavenPluginHandler.class);

    list.add(FlexCoberturaSensor.class);

    list.add(FlexSquidSensor.class);

    return list;
  }
}
