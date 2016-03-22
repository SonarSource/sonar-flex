/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
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

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Test;
import org.sonar.squidbridge.commonrules.internal.CommonRulesConstants;

public class FlexCommonRulesEngineTest {

  @Test
  public void provide_extensions() {
    FlexCommonRulesEngine engine = new FlexCommonRulesEngine();
    assertThat(engine.provide()).isNotEmpty();
  }

  @Test
  public void enable_common_rules() {
    FlexCommonRulesEngine engine = new FlexCommonRulesEngine();

    assertThat(engine.newRepository().enabledRuleKeys()).containsOnly(
      CommonRulesConstants.RULE_DUPLICATED_BLOCKS,
      CommonRulesConstants.RULE_INSUFFICIENT_COMMENT_DENSITY,
      CommonRulesConstants.RULE_INSUFFICIENT_LINE_COVERAGE,
      CommonRulesConstants.RULE_INSUFFICIENT_BRANCH_COVERAGE);
  }
}
