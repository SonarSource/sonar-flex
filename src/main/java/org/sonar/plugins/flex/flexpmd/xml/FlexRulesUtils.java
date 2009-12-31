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

package org.sonar.plugins.flex.flexpmd.xml;

import com.thoughtworks.xstream.XStream;
import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.sonar.api.utils.SonarException;
import org.sonar.api.rules.RulesCategory;
import org.sonar.api.rules.Iso9126RulesCategories;

public class FlexRulesUtils {
  public static Ruleset buildRuleSetFromXml(String configuration) {
    XStream xstream = new XStream();
    xstream.processAnnotations(Ruleset.class);
    xstream.processAnnotations(FlexRule.class);
    xstream.processAnnotations(Property.class);
    xstream.aliasSystemAttribute("ref", "class");

    return (Ruleset) xstream.fromXML(configuration);
  }

  public static RulesCategory matchRuleCategory(String category) {
    for (RulesCategory ruleCategory : Iso9126RulesCategories.ALL) {
      if (ruleCategory.getName().equalsIgnoreCase(category)) {
        return ruleCategory;
      }
    }
    throw new IllegalArgumentException("Unexpected category name " + category);
  }

  public static String buildXmlFromRuleset(Ruleset tree) {
    XStream xstream = new XStream();
    xstream.processAnnotations(Ruleset.class);
    xstream.processAnnotations(FlexRule.class);
    xstream.processAnnotations(Property.class);
    return addHeaderToXml(xstream.toXML(tree));
  }

  private static String addHeaderToXml(String xmlModules) {
    String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    return header + xmlModules;
  }

  public static String getConfigurationFromFile(String path) {
    InputStream inputStream = Ruleset.class.getResourceAsStream(path);
    String configuration = null;
    try {
      configuration = IOUtils.toString(inputStream, "UTF-8");
    }
    catch (IOException e) {
      throw new SonarException("Configuration file not found for the profile : " + configuration, e);
    }
    finally {
      IOUtils.closeQuietly(inputStream);
    }
    return configuration;
  }

}
