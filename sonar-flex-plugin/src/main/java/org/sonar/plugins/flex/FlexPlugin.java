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

import com.google.common.collect.ImmutableList;
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
import org.sonar.plugins.flex.flexpmd.*;
import org.sonar.plugins.flex.surefire.FlexMojosMavenPluginHandler;
import org.sonar.plugins.flex.surefire.FlexSurefireSensor;

import java.util.List;

@Properties({@Property(
  key = FlexPlugin.FILE_SUFFIXES_KEY,
  defaultValue = Flex.DEFAULT_FILE_SUFFIXES,
  name = "File suffixes",
  description = "Comma-separated list of suffixes for files to analyze. To not filter, leave the list empty.",
  global = true,
  project = true)})
public class FlexPlugin extends SonarPlugin {

  public static final String FILE_SUFFIXES_KEY = "sonar.flex.file.suffixes";

  public List<Class<? extends Extension>> getExtensions() {
    return ImmutableList.of(
        Flex.class,
        FlexSourceImporter.class,
        FlexResourceBridge.class,
        FlexColorizerFormat.class,

        FlexCpdMapping.class,

        FlexPmdSensor.class,
        FlexPmdRuleRepository.class,
        DefaultFlexProfile.class,
        FlexPmdProfileExporter.class,
        FlexPmdProfileImporter.class,

        FlexSurefireSensor.class,
        FlexMojosMavenPluginHandler.class,

        FlexCoberturaSensor.class,

        FlexSquidSensor.class,

        FlexRuleRepository.class,
        FlexProfile.class);
  }

}
