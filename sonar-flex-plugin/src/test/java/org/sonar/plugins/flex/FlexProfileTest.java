/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2023 SonarSource SA
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

import java.util.List;
import org.junit.jupiter.api.Test;
import org.sonar.flex.checks.CheckList;
import org.sonar.plugins.flex.core.Flex;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.BuiltInActiveRule;
import static org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.BuiltInQualityProfile;
import static org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.Context;

public class FlexProfileTest {

  @Test
  public void should_create_sonar_way_profile() {
    Context context = new Context();

    FlexProfile definition = new FlexProfile();
    definition.define(context);

    BuiltInQualityProfile profile = context.profile(Flex.KEY, CheckList.SONAR_WAY_PROFILE);

    assertThat(profile).isNotNull();

    List<BuiltInActiveRule> activeRules = profile.rules();
    assertThat(activeRules.size()).isGreaterThan(40);
    assertThat(activeRules).extracting(BuiltInActiveRule::ruleKey)
      .contains("S1871")
      .contains("S101")
      .doesNotContain("S1469")
      .doesNotContain("S1176");
  }
}
