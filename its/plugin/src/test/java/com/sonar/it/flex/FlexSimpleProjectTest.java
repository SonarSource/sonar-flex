/*
 * SonarQube Flex Plugin
 * Copyright (C) 2012-2025 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package com.sonar.it.flex;

import com.sonar.orchestrator.build.SonarScanner;
import com.sonar.orchestrator.junit4.OrchestratorRule;
import java.io.File;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static com.sonar.it.flex.Tests.getComponent;
import static com.sonar.it.flex.Tests.getMeasureAsInteger;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class is used to reproduce various issues.
 */
public class FlexSimpleProjectTest {

  private static final String PROJECT = "simple-project";

  @ClassRule
  public static OrchestratorRule orchestrator = Tests.ORCHESTRATOR;

  private static String keyFor(String s) {
    return PROJECT + ":src/" + s;
  }

  @BeforeClass
  public static void init() {
    SonarScanner runner = Tests.createSonarScanner()
      .setProjectDir(new File("projects/simple-project"))
      .setProperty("sonar.profile", "it-profile");
    orchestrator.executeBuild(runner);
  }

  @Test
  public void projectIsAnalyzed() {
    assertThat(getComponent(PROJECT).getName()).isEqualTo("Flex IT :: Simple Project");
  }

  /**
   * SONARPLUGINS-903: metrics from FlexMetrics were associated to the wrong file
   * if 2 files had the same name but different extensions
   */
  @Test
  public void projectTestMetrics() {
    assertThat(getMeasureAsInteger(keyFor("same_name/XMLUtil.as"), "ncloc")).isEqualTo(49);
    // TODO mxml support has been broken
    // assertThat(getFileMeasure(PROJECT + ":same_name/XMLUtil.mxml", "ncloc").getIntValue(), is(12));
  }

}
