/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2020 SonarSource SA
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.flex.checks.CheckList;
import org.sonar.plugins.flex.core.Flex;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

public final class FlexRulesDefinition implements RulesDefinition {

  private static final String REPOSITORY_NAME = "SonarAnalyzer";
  private static final String RESOURCE_BASE_PATH = "org/sonar/l10n/flex/rules/flex";
  private static final Set<String> TEMPLATE_RULE_KEYS = new HashSet<>(Arrays.asList("XPath", "CommentRegularExpression"));

  @Override
  public void define(Context context) {
    NewRepository repository = context
      .createRepository(CheckList.REPOSITORY_KEY, Flex.KEY)
      .setName(REPOSITORY_NAME);

    RuleMetadataLoader ruleMetadataLoader = new RuleMetadataLoader(RESOURCE_BASE_PATH, FlexProfile.SONAR_WAY_PROFILE_PATH);
    ruleMetadataLoader.addRulesByAnnotatedClass(repository, CheckList.getChecks());

    TEMPLATE_RULE_KEYS.forEach(key -> repository.rule(key).setTemplate(true));

    repository.done();
  }
}
