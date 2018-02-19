/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2018 SonarSource SA
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

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.flex.checks.CheckList;
import org.sonar.plugins.flex.core.Flex;
import org.sonarsource.analyzer.commons.ProfileDefinitionReader;

public class FlexProfile extends ProfileDefinition {

  private final RuleFinder ruleFinder;

  public FlexProfile(RuleFinder ruleFinder) {
    this.ruleFinder = ruleFinder;
  }

  @Override
  public RulesProfile createProfile(ValidationMessages validation) {
    RulesProfile profile = RulesProfile.create(CheckList.SONAR_WAY_PROFILE, Flex.KEY);

    ProfileDefinitionReader definitionReader = new ProfileDefinitionReader(ruleFinder);
    definitionReader.activateRules(profile, CheckList.REPOSITORY_KEY, "org/sonar/l10n/flex/rules/flex/Sonar_way_profile.json");

    return profile;
  }
}
