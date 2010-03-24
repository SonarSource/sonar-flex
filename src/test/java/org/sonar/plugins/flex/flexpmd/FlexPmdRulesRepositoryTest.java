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
package org.sonar.plugins.flex.flexpmd;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.plugins.flex.flexpmd.xml.Ruleset;
import org.sonar.plugins.flex.flexpmd.xml.FlexRulesUtils;
import java.util.List;

public class FlexPmdRulesRepositoryTest {

  private FlexPmdRulesRepository repository;

  @Before
  public void setup() {
    repository = new FlexPmdRulesRepository(null);
  }

  @Test
  public void loadFlexRulesRepositoryAndCheckMandatoryFields() {
    List<Rule> rules = repository.getInitialReferential();
    assertThat(rules.size(), is(82));
    for (Rule rule : rules) {
      assertNotNull(rule.getKey());
      assertNotNull(rule.getDescription());
      assertNotNull(rule.getName());
      assertNotNull(rule.getPluginName());
      assertNotNull(rule.getRulesCategory());
      assertNotNull(rule.getPriority());
    }
  }

  @Test
  public void testProvidedProfiles() {
    List<RulesProfile> profiles = repository.getProvidedProfiles();
    assertThat(profiles.size(), is(1));
  }

  @Test
  public void testBuildProvidedProfile() {
    List<ActiveRule> defaultProfile = repository.getProvidedProfiles().get(0).getActiveRules();
    assertThat(defaultProfile.size(), is(66));
  }

  @Test
  public void testImportConfiguration() {
    List<ActiveRule> activeRules = getActiveRules("/org/sonar/plugins/flex/flexpmd/flexpmd-simple-profile-with-fake.xml");
    assertThat(activeRules.size(), is(7));

    activeRules = getActiveRules("/org/sonar/plugins/flex/flexpmd/flexpmd-fullmonty-profile.xml");
    assertThat(activeRules.size(), is(66));
  }

  @Test
  public void testImportWrongFormat() {
    List<ActiveRule> activeRules = getActiveRules("/org/sonar/plugins/flex/flexpmd/flexpmd-wrong-format-import.xml");
    assertThat(activeRules.size(), is(5));
  }

  @Test
  public void testExplicitPriority() {
    List<ActiveRule> activeRules = getActiveRules("/org/sonar/plugins/flex/flexpmd/flexpmd-explicit-priority.xml");
    assertThat(activeRules.size(), is(1));
    assertThat(activeRules.get(0).getPriority(), is(RulePriority.CRITICAL));
  }

  @Test
  public void testImplicitPriority() {
    List<ActiveRule> activeRules = getActiveRules("/org/sonar/plugins/flex/flexpmd/flexpmd-implicit-priority.xml");
    assertThat(activeRules.size(), is(1));
    assertThat(activeRules.get(0).getPriority(), is(RulePriority.BLOCKER));
  }

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
    ;
    Ruleset ruleset = repository.buildRulesetFromActiveProfile(activeRules);
    assertThat(ruleset.getRules().size(), is(66));
  }

  @Test
  public void testPriorityInBuildingRulesetFromActiveRules() {
    List<ActiveRule> activeRules = getActiveRules("/org/sonar/plugins/flex/flexpmd/flexpmd-explicit-priority.xml");
    Ruleset ruleset = repository.buildRulesetFromActiveProfile(activeRules);
    assertThat(ruleset.getRules().size(), is(1));
    assertThat(ruleset.getRules().get(0).getPriority(), is("2"));

    activeRules = getActiveRules("/org/sonar/plugins/flex/flexpmd/flexpmd-implicit-priority.xml");
    ruleset = repository.buildRulesetFromActiveProfile(activeRules);
    assertThat(ruleset.getRules().size(), is(1));
    assertThat(ruleset.getRules().get(0).getPriority(), is("1"));
  }

  @Ignore
  public void testFullLoadAndUnloadOfProfile() {
    RulesProfile profile = repository.buildProfile("test-profile", "/org/sonar/plugins/flex/flexpmd/flexpmd-simple-profile-with-fake.xml");
    String output = repository.exportConfiguration(profile);
    //assertEquals(output, "toto");
  }

  private List<ActiveRule> getActiveRules(String path) {
    List<ActiveRule> activeRules = repository.importConfiguration(FlexRulesUtils.getConfigurationFromFile(path), repository.getInitialReferential());
    return activeRules;
  }

}
