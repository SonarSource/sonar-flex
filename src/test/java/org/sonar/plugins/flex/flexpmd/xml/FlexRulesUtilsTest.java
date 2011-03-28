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

package org.sonar.plugins.flex.flexpmd.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.sonar.api.rules.ActiveRule;

import java.util.List;

public class FlexRulesUtilsTest {
  @Test
  public void testActiveRulesParams() {
    List<ActiveRule> activeRules = getActiveRules("/org/sonar/plugins/flex/flexpmd/flexpmd-simple-profile-with-fake.xml");

    // no property
    assertThat(activeRules.get(2).getActiveRuleParams().size(), is(0));

    // property defined, but does not exist in repo
    assertThat(activeRules.get(3).getActiveRuleParams().size(), is(0));

    // property defined and exist in repo
    assertThat(activeRules.get(5).getActiveRuleParams().size(), is(1));

    // property defined, but the one in repo is different
    assertThat(activeRules.get(6).getActiveRuleParams().size(), is(0));
  }

  @Test
  public void testBuildRulesetFromActiveRules() {
    List<ActiveRule> activeRules = getActiveRules("/org/sonar/plugins/flex/flexpmd/flexpmd-fullmonty-profile.xml");

    Ruleset ruleset = FlexRulesUtils.buildRulesetFromActiveProfile(activeRules);
    assertThat(ruleset.getRules().size(), is(66));
  }

  @Test
  public void testPriorityInBuildingRulesetFromActiveRules() {
    List<ActiveRule> activeRules = getActiveRules("/org/sonar/plugins/flex/flexpmd/flexpmd-explicit-priority.xml");
    Ruleset ruleset = FlexRulesUtils.buildRulesetFromActiveProfile(activeRules);
    assertThat(ruleset.getRules().size(), is(1));
    assertThat(ruleset.getRules().get(0).getPriority(), is("2"));

    activeRules = getActiveRules("/org/sonar/plugins/flex/flexpmd/flexpmd-implicit-priority.xml");
    ruleset = FlexRulesUtils.buildRulesetFromActiveProfile(activeRules);
    assertThat(ruleset.getRules().size(), is(1));
    assertThat(ruleset.getRules().get(0).getPriority(), is("1"));
  }

  private List<ActiveRule> getActiveRules(String path) {
    List<ActiveRule> activeRules = FlexRulesUtils.importConfiguration(
        FlexRulesUtils.getConfigurationFromFile(path),
        FlexRulesUtils.getInitialReferential());
    return activeRules;
  }
}
