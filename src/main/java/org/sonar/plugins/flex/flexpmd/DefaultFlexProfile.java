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

package org.sonar.plugins.flex.flexpmd;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.flex.Flex;

import java.io.InputStreamReader;
import java.io.Reader;

public class DefaultFlexProfile extends ProfileDefinition {

  private FlexPmdProfileImporter importer;

  public DefaultFlexProfile(FlexPmdProfileImporter importer) {
    this.importer = importer;
  }

  @Override
  public RulesProfile createProfile(ValidationMessages messages) {
    Reader reader = new InputStreamReader(getClass().getResourceAsStream("/org/sonar/plugins/flex/flexpmd/default-flex-profile.xml"));
    RulesProfile profile = importer.importProfile(reader, messages);
    profile.setLanguage(Flex.KEY);
    profile.setName("Default Flex Profile");
    return profile;
  }

}
