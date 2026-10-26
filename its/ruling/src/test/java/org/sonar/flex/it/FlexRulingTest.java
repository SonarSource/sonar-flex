/*
 * SonarQube Flex Plugin
 * Copyright (C) SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * You can redistribute and/or modify this program under the terms of
 * the Sonar Source-Available License Version 1, as published by SonarSource Sàrl.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonar.flex.it;

import com.sonar.orchestrator.build.SonarScanner;
import com.sonar.orchestrator.container.Edition;
import com.sonar.orchestrator.config.Configuration;
import com.sonar.orchestrator.junit4.OrchestratorRule;
import com.sonar.orchestrator.locator.FileLocation;
import com.sonar.orchestrator.locator.MavenLocation;
import java.io.File;
import java.nio.file.Files;
import org.junit.ClassRule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FlexRulingTest {

  @ClassRule
  public static final OrchestratorRule ORCHESTRATOR = OrchestratorRule.builderEnv()
    .setEdition(Edition.ENTERPRISE_LW)
    .activateLicense()
    .useDefaultAdminCredentialsForBuilds(true)
    .setSonarVersion(Configuration.createEnv().getString("sonar.runtimeVersion"))
    .addPlugin(FileLocation.byWildcardMavenFilename(new File("../../sonar-flex-plugin/target"), "sonar-flex-plugin-*.jar"))
    .addPlugin(MavenLocation.of("org.sonarsource.sonar-lits-plugin","sonar-lits-plugin", "0.11.0.2659"))
    .restoreProfileAtStartup(FileLocation.of("src/test/resources/profile.xml"))
    .build();

  @Test
  public void as3_commons() throws Exception {
    runRulingTest("as3-commons");
  }

  @Test
  public void spring_actionscript() throws Exception {
    runRulingTest("spring-actionscript");
  }

  private static void runRulingTest(String projectKey) throws Exception {
    ORCHESTRATOR.getServer().provisionProject(projectKey, projectKey);
    ORCHESTRATOR.getServer().associateProjectToQualityProfile(projectKey, "flex", "rules");
    File litsDifferencesFile = FileLocation.of("target/differences-" + projectKey).getFile();

    SonarScanner build = SonarScanner.create(FileLocation.of("../sources/" + projectKey).getFile())
      .setProperty("sonar.scanner.skipJreProvisioning", "true")
      .setProjectKey(projectKey)
      .setProjectName(projectKey)
      .setProjectVersion("1")
      .setSourceDirs(".")
      .setSourceEncoding("UTF-8")
      .setProperty("sonar.lits.dump.old", FileLocation.of("src/test/resources/expected/" + projectKey).getFile().getAbsolutePath())
      .setProperty("sonar.lits.dump.new", FileLocation.of("target/actual/" + projectKey).getFile().getAbsolutePath())
      .setProperty("sonar.lits.differences", litsDifferencesFile.getAbsolutePath())
      .setProperty("sonar.cpd.exclusions", "**/*")
      .setDebugLogs(true)
      .setEnvironmentVariable("SONAR_RUNNER_OPTS", "-Xmx1000m");
    ORCHESTRATOR.executeBuild(build);

    assertThat(Files.readAllBytes(litsDifferencesFile.toPath())).isEmpty();
  }

}
