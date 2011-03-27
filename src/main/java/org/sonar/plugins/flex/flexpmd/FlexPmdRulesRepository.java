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

import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.*;
import org.sonar.plugins.flex.Flex;
import org.sonar.plugins.flex.FlexPlugin;
import org.sonar.plugins.flex.flexpmd.xml.FlexRule;
import org.sonar.plugins.flex.flexpmd.xml.FlexRulesUtils;
import org.sonar.plugins.flex.flexpmd.xml.Property;
import org.sonar.plugins.flex.flexpmd.xml.Ruleset;

import java.util.ArrayList;
import java.util.List;

public class FlexPmdRulesRepository implements RulesRepository<Flex>, ConfigurationExportable, ConfigurationImportable {
  private FlexPmdRulePriorityMapper priorityMapper = new FlexPmdRulePriorityMapper();
  private String resourcePath = "/org/sonar/plugins/flex/flexpmd/";
  private Flex flex;

  public FlexPmdRulesRepository(Flex flex) {
    this.flex = flex;
  }

  public Flex getLanguage() {
    return flex;
  }

  public final List<Rule> getInitialReferential() {
    return parseReferential(resourcePath + "rules.xml");
  }

  public List<Rule> parseReferential(String path) {
    Ruleset ruleset = FlexRulesUtils.buildRuleSetFromXml(FlexRulesUtils.getConfigurationFromFile(path));
    List<Rule> rulesRepository = new ArrayList<Rule>();
    for (FlexRule fRule : ruleset.getRules()) {
      rulesRepository.add(createRepositoryRule(fRule));
    }
    return rulesRepository;
  }

  public final List<RulesProfile> getProvidedProfiles() {
    List<RulesProfile> profiles = new ArrayList<RulesProfile>();
    profiles.add(buildProfile("Default Flex Profile", resourcePath + "default-flex-profile.xml"));
    return profiles;
  }

  public final RulesProfile buildProfile(String name, String path) {
    RulesProfile profile = new RulesProfile(name, Flex.KEY);
    List<ActiveRule> activeRules = importConfiguration(FlexRulesUtils.getConfigurationFromFile(path), getInitialReferential());
    profile.setActiveRules(activeRules);
    return profile;
  }

  public List<ActiveRule> importConfiguration(String configuration, List<Rule> rulesRepository) {
    Ruleset ruleset = FlexRulesUtils.buildRuleSetFromXml(configuration);
    List<ActiveRule> activeRules = new ArrayList<ActiveRule>();
    for (FlexRule fRule : ruleset.getRules()) {
      ActiveRule activeRule = createActiveRule(fRule, rulesRepository);
      if (activeRule != null) {
        activeRules.add(activeRule);
      }
    }
    return activeRules;
  }

  public String exportConfiguration(RulesProfile activeProfile) {
    Ruleset tree = buildRulesetFromActiveProfile(activeProfile.getActiveRulesByPlugin(FlexPlugin.FLEXPMD_PLUGIN));
    return FlexRulesUtils.buildXmlFromRuleset(tree);
  }

  private Rule createRepositoryRule(FlexRule fRule) {
    RulesCategory category = FlexRulesUtils.matchRuleCategory(fRule.getCategory());
    RulePriority priority = priorityMapper.from(fRule.getPriority());
    Rule rule = new Rule(FlexPlugin.FLEXPMD_PLUGIN, fRule.getClazz(), fRule.getMessage(), category, priority);
    rule.setDescription(fRule.getDescription());
    List<RuleParam> ruleParams = new ArrayList<RuleParam>();
    if (fRule.getProperties() != null) {
      for (Property property : fRule.getProperties()) {
        RuleParam param = new RuleParam(rule, property.getName(), property.getName(), "i");
        ruleParams.add(param);
      }
    }
    rule.setParams(ruleParams);
    return rule;
  }

  private ActiveRule createActiveRule(FlexRule fRule, List<Rule> rulesRepository) {
    String clazz = fRule.getClazz();
    RulePriority fRulePriority = priorityMapper.from(fRule.getPriority());
    for (Rule rule : rulesRepository) {
      if (rule.getKey().equals(clazz)) {
        RulePriority priority = fRulePriority != null ? fRulePriority : rule.getSeverity();
        ActiveRule activeRule = new ActiveRule(null, rule, priority);
        activeRule.setActiveRuleParams(buildActiveRuleParams(fRule, rule, activeRule));
        return activeRule;
      }
    }
    return null;
  }

  protected List<ActiveRuleParam> buildActiveRuleParams(FlexRule flexRule, Rule repositoryRule, ActiveRule activeRule) {
    List<ActiveRuleParam> activeRuleParams = new ArrayList<ActiveRuleParam>();
    if (flexRule.getProperties() != null) {
      for (Property property : flexRule.getProperties()) {
        if (repositoryRule.getParams() != null) {
          for (RuleParam ruleParam : repositoryRule.getParams()) {
            if (ruleParam.getKey().equals(property.getName())) {
              activeRuleParams.add(new ActiveRuleParam(activeRule, ruleParam, property.getValue()));
            }
          }
        }
      }
    }
    return activeRuleParams;
  }

  protected Ruleset buildRulesetFromActiveProfile(List<ActiveRule> activeRules) {
    Ruleset ruleset = new Ruleset();
    for (ActiveRule activeRule : activeRules) {
      if (activeRule.getRule().getRepositoryKey().equals(FlexPlugin.FLEXPMD_PLUGIN)) {
        String key = activeRule.getRule().getKey();
        String priority = priorityMapper.to(activeRule.getSeverity());
        FlexRule flexRule = new FlexRule(key, priority);
        List<Property> properties = new ArrayList<Property>();
        for (ActiveRuleParam activeRuleParam : activeRule.getActiveRuleParams()) {
          properties.add(new Property(activeRuleParam.getRuleParam().getKey(), activeRuleParam.getValue()));
        }
        flexRule.setProperties(properties);
        flexRule.setMessage(activeRule.getRule().getName());
        ruleset.addRule(flexRule);
      }
    }
    return ruleset;
  }
}
