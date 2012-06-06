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

import com.google.common.collect.Lists;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.plugins.flex.core.Flex;
import org.sonar.plugins.flex.flexpmd.xml.FlexRulesUtils;

import java.util.List;

public class FlexPmdRuleRepository extends RuleRepository {

  public static final String REPOSITORY_KEY = "flexplugin";
  public static final String REPOSITORY_NAME = "Flex PMD";

  public FlexPmdRuleRepository() {
    super(REPOSITORY_KEY, Flex.KEY);
    setName(REPOSITORY_NAME);
  }

  @Override
  public List<Rule> createRules() {
    List<Rule> rules = Lists.newArrayList();
    rules.addAll(FlexRulesUtils.getInitialReferential());
    return rules;
  }

}
