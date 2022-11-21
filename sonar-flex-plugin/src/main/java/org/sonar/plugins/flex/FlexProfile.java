/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2022 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plugins.flex;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.flex.checks.CheckList;
import org.sonar.plugins.flex.core.Flex;
import org.sonarsource.analyzer.commons.BuiltInQualityProfileJsonLoader;

public class FlexProfile implements BuiltInQualityProfilesDefinition {

  public static final String SONAR_WAY_PROFILE_PATH = "org/sonar/l10n/flex/rules/flex/Sonar_way_profile.json";

  @Override
  public void define(Context context) {
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(CheckList.SONAR_WAY_PROFILE, Flex.KEY);
    BuiltInQualityProfileJsonLoader.load(profile, CheckList.REPOSITORY_KEY, SONAR_WAY_PROFILE_PATH);
    profile.done();
  }
}
