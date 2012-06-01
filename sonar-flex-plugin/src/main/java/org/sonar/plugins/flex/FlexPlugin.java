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

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.Extension;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;
import org.sonar.plugins.flex.cobertura.FlexCoberturaSensor;
import org.sonar.plugins.flex.colorizer.FlexColorizerFormat;
import org.sonar.plugins.flex.core.Flex;
import org.sonar.plugins.flex.core.FlexResourceBridge;
import org.sonar.plugins.flex.core.FlexSourceImporter;
import org.sonar.plugins.flex.duplications.FlexCpdMapping;
import org.sonar.plugins.flex.flexmetrics.FlexMetricsMavenPluginHandler;
import org.sonar.plugins.flex.flexmetrics.FlexMetricsParser;
import org.sonar.plugins.flex.flexmetrics.FlexMetricsSensor;
import org.sonar.plugins.flex.flexpmd.DefaultFlexProfile;
import org.sonar.plugins.flex.flexpmd.FlexPmdMavenPluginHandler;
import org.sonar.plugins.flex.flexpmd.FlexPmdProfileExporter;
import org.sonar.plugins.flex.flexpmd.FlexPmdProfileImporter;
import org.sonar.plugins.flex.flexpmd.FlexPmdRuleRepository;
import org.sonar.plugins.flex.flexpmd.FlexPmdSensor;
import org.sonar.plugins.flex.squid.FlexNoSonarFilter;
import org.sonar.plugins.flex.squid.FlexSquidSensor;
import org.sonar.plugins.flex.surefire.FlexMojosMavenPluginHandler;
import org.sonar.plugins.flex.surefire.FlexSurefireSensor;

/**
 * Plugin Flex that analyses ActionScript source code.
 */
@Properties({@Property(
  key = FlexPlugin.FILE_SUFFIXES_KEY,
  defaultValue = Flex.DEFAULT_FILE_SUFFIXES,
  name = "File suffixes",
  description = "Comma-separated list of suffixes for files to analyze. To not filter, leave the list empty.",
  global = true,
  project = true)})
public class FlexPlugin extends SonarPlugin {

  /** Parameters of the flex plugin. */
  public static final String FILE_SUFFIXES_KEY = "sonar.flex.file.suffixes";

  /**
   * Gets the extensions.
   * 
   * @return the extensions
   * @see org.sonar.api.Plugin#getExtensions()
   */
  public List<Class<? extends Extension>> getExtensions() {
    List<Class<? extends Extension>> list = new ArrayList<Class<? extends Extension>>();

    list.add(Flex.class);
    list.add(FlexSourceImporter.class);
    list.add(FlexResourceBridge.class);
    list.add(FlexColorizerFormat.class);
    list.add(FlexNoSonarFilter.class);

    list.add(FlexCpdMapping.class);

    list.add(FlexMetricsMavenPluginHandler.class);
    list.add(FlexMetricsParser.class);
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
