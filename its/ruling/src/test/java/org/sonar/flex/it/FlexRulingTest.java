/*
 * Copyright (C) 2012-2014 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */
package org.sonar.flex.it;

import com.google.common.io.Files;
import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.build.SonarRunner;
import com.sonar.orchestrator.locator.FileLocation;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class FlexRulingTest {

  @ClassRule
  public static Orchestrator ORCHESTRATOR = Orchestrator.builderEnv()
    .addPlugin(FileLocation.of("../../sonar-flex-plugin/target/sonar-flex-plugin.jar"))
    .setOrchestratorProperty("litsVersion", "0.5")
    .addPlugin("lits")
    .restoreProfileAtStartup(FileLocation.of("src/test/resources/profile.xml"))
    .build();

  @Test
  public void test() throws Exception {
    assertTrue(
      "SonarQube 5.1 is the minimum version to generate the issues report, change your orchestrator.properties",
      ORCHESTRATOR.getConfiguration().getSonarVersion().isGreaterThanOrEquals("5.1"));
    File litsDifferencesFile = FileLocation.of("target/differences").getFile();

    SonarRunner build = SonarRunner.create(FileLocation.of("../sources/src").getFile())
      .setProjectKey("project")
      .setProjectName("project")
      .setProjectVersion("1")
      .setSourceDirs(".")
      .setLanguage("flex")
      .setSourceEncoding("UTF-8")
      .setProfile("rules")
      .setProperty("sonar.analysis.mode", "preview")
      .setProperty("sonar.issuesReport.html.enable", "true")
      .setProperty("dump.old", FileLocation.of("src/test/resources/expected").getFile().getAbsolutePath())
      .setProperty("dump.new", FileLocation.of("target/actual").getFile().getAbsolutePath())
      .setProperty("lits.differences", litsDifferencesFile.getAbsolutePath())
      .setProperty("sonar.cpd.skip", "true")
      .setEnvironmentVariable("SONAR_RUNNER_OPTS", "-Xmx1000m");
    ORCHESTRATOR.executeBuild(build);

    assertThat(Files.toString(litsDifferencesFile, StandardCharsets.UTF_8)).isEmpty();
  }

}
