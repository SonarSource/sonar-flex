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
import org.sonar.plugins.flex.Flex;
import org.sonar.plugins.flex.flexpmd.xml.Ruleset;
import org.sonar.plugins.flex.flexpmd.xml.Rule;
import org.sonar.api.rules.ActiveRule;
import org.apache.commons.io.IOUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.InputStream;
import java.io.IOException;

public class FlexPmdRulesRepositoryTest {

  private FlexPmdRulesRepository repository;

  @Before
  public void setup() {
    repository = new FlexPmdRulesRepository(Flex.INSTANCE);
  }

  @Test
  public void loadFlexRulesRepositoryAndCheckMandatoryFields() {
    List<org.sonar.api.rules.Rule> rules = repository.getInitialReferential();
    assertThat(rules.size(), is(82));
    for (org.sonar.api.rules.Rule rule : rules) {
      assertNotNull(rule.getKey());
      assertNotNull(rule.getDescription());
      assertNotNull(rule.getConfigKey());
      assertNotNull(rule.getName());
    }
  }

  @Test
  public void testBuiltInProfiles() {
    Map<String, String> defaults = repository.getBuiltInProfiles();
    assertThat(defaults.size(), is(1));
    assertNotNull(defaults.get("Default Flex Profile"));
  }


  private Ruleset createRuleSet(String path) throws IOException {
    InputStream input = getClass().getResourceAsStream(path);
    return repository.buildRuleSetFromXml(IOUtils.toString(input, "UTF-8"));
  }

  @Test
  public void testBuildRuleSetFromXml() throws IOException {
    Ruleset ruleSet = createRuleSet("/org/sonar/plugins/flex/flexpmd/flexpmd-import-configuration.xml");
    assertThat(ruleSet.getRules().size(), is(8));
  }

  @Test
  public void testBuildActiveRulesFromRuleSet() throws IOException {
    Ruleset ruleSet = createRuleSet("/org/sonar/plugins/flex/flexpmd/flexpmd-import-configuration.xml");
    List<ActiveRule> activeRules = repository.getActiveRulesFromRuleSet(ruleSet, repository.getInitialReferential());
    assertThat(activeRules.size(), is(6));
  }

  @Test
  public void testBuildActiveRulesFromEmptyRuleSet() throws IOException {
    Ruleset ruleSet = new Ruleset();
    ruleSet.setRules(null);
    List<ActiveRule> activeRules = repository.getActiveRulesFromRuleSet(ruleSet, repository.getInitialReferential());
    assertThat(activeRules.size(), is(0));

    ruleSet.setRules(new ArrayList<Rule>());
    activeRules = repository.getActiveRulesFromRuleSet(ruleSet, repository.getInitialReferential());
    assertThat(activeRules.size(), is(0));
  }

  @Test
  public void testActiveRulesParams() throws IOException {
    Ruleset ruleSet = createRuleSet("/org/sonar/plugins/flex/flexpmd/flexpmd-import-configuration.xml");
    org.sonar.api.rules.Rule noParamRule = null;
    org.sonar.api.rules.Rule oneParamRuleNotInRepository = null;
    org.sonar.api.rules.Rule oneParamRule = null;

    for (org.sonar.api.rules.Rule rule : repository.getInitialReferential()) {
      if (rule.getKey().equals("com.adobe.ac.pmd.rules.cairngorm.CairngormEventDispatcherCallExplicitlyRule")) {
        noParamRule = rule;
      }
      if (rule.getKey().equals("com.adobe.ac.pmd.rules.cairngorm.ModelLocatorReferencedMoreThanOncePerClassRule")) {
        oneParamRuleNotInRepository = rule;
      }
      if (rule.getKey().equals("com.adobe.ac.pmd.rules.maintanability.ExcessiveImportRule")) {
        oneParamRule = rule;
      }
    }
    assertNotNull(noParamRule);
    assertNotNull(oneParamRuleNotInRepository);
    assertThat(repository.getActiveRuleParams(ruleSet.getRules().get(2), noParamRule, null).size(), is(0));
    assertThat(repository.getActiveRuleParams(ruleSet.getRules().get(3), oneParamRuleNotInRepository, null).size(), is(0));
    assertThat(repository.getActiveRuleParams(ruleSet.getRules().get(5), oneParamRule, null).size(), is(1));
  }
}
