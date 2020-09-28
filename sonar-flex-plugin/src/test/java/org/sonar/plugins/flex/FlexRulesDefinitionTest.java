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

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Rule;
import org.sonar.flex.checks.CheckList;

import static org.assertj.core.api.Assertions.assertThat;

public class FlexRulesDefinitionTest {

  @Test
  public void test() {
    FlexRulesDefinition rulesDefinition = new FlexRulesDefinition();
    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);
    RulesDefinition.Repository repository = context.repository("flex");

    assertThat(repository.name()).isEqualTo("SonarAnalyzer");
    assertThat(repository.language()).isEqualTo("flex");
    assertThat(repository.rules()).hasSize(CheckList.getChecks().size());

    Rule functionComplexityRule = repository.rule("FunctionComplexity");
    assertThat(functionComplexityRule).isNotNull();
    assertThat(functionComplexityRule.name()).isEqualTo("Functions should not be too complex");

    for (Rule rule : repository.rules()) {
      for (RulesDefinition.Param param : rule.params()) {
        assertThat(param.description()).as("description for " + param.key() + " of " + rule.key()).isNotEmpty();
      }
    }

    assertThat(repository.rules().stream().filter(Rule::template))
      .extracting(Rule::key)
      .containsOnly("XPath", "CommentRegularExpression");
  }
}
