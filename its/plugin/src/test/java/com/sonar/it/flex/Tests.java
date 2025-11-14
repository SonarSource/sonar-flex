/*
 * SonarQube Flex Plugin
 * Copyright (C) 2012-2025 SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource Sàrl.
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

import com.sonar.orchestrator.build.MavenBuild;
import com.sonar.orchestrator.build.SonarScanner;
import com.sonar.orchestrator.container.Edition;
import com.sonar.orchestrator.config.Configuration;
import com.sonar.orchestrator.junit4.OrchestratorRule;
import com.sonar.orchestrator.locator.FileLocation;
import java.io.File;
import java.util.Collections;
import java.util.List;
import javax.annotation.CheckForNull;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.sonarqube.ws.Components;
import org.sonarqube.ws.Measures;
import org.sonarqube.ws.client.HttpConnector;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import org.sonarqube.ws.client.components.ShowRequest;
import org.sonarqube.ws.client.measures.ComponentRequest;

@RunWith(Suite.class)
@SuiteClasses({
  CoverageTest.class,
  FlexIntegrationTest.class,
  FlexSimpleProjectTest.class
})
public class Tests {

  @ClassRule
  public static final OrchestratorRule ORCHESTRATOR = OrchestratorRule.builderEnv()
    .setEdition(Edition.ENTERPRISE_LW)
    .activateLicense()
    .useDefaultAdminCredentialsForBuilds(true)
    .setSonarVersion(Configuration.createEnv().getString("sonar.runtimeVersion"))
    .addPlugin(FileLocation.byWildcardMavenFilename(new File("../../sonar-flex-plugin/target"), "sonar-flex-plugin-*.jar"))
    .restoreProfileAtStartup(FileLocation.ofClasspath("/it-profile_flex.xml"))
    .build();

  public static MavenBuild createMavenBuild() {
    return MavenBuild.create()
      .setProperty("sonar.scanner.skipJreProvisioning", "true");
  }

  public static SonarScanner createSonarScanner() {
    return SonarScanner.create()
      .setProperty("sonar.scanner.skipJreProvisioning", "true");
  }

  @CheckForNull
  static Measures.Measure getMeasure(String componentKey, String metricKey) {
    Measures.ComponentWsResponse response = newWsClient().measures().component(
      new ComponentRequest().setMetricKeys(Collections.singletonList(metricKey)).setComponent(componentKey));
    List<Measures.Measure> measures = response.getComponent().getMeasuresList();
    return measures.size() == 1 ? measures.get(0) : null;
  }

  @CheckForNull
  static Integer getMeasureAsInteger(String componentKey, String metricKey) {
    Measures.Measure measure = getMeasure(componentKey, metricKey);
    return (measure == null) ? null : Integer.parseInt(measure.getValue());
  }

  @CheckForNull
  static Double getMeasureAsDouble(String componentKey, String metricKey) {
    Measures.Measure measure = getMeasure(componentKey, metricKey);
    return (measure == null) ? null : Double.parseDouble(measure.getValue());
  }

  @CheckForNull
  static Components.Component getComponent(String componentKey) {
    return newWsClient().components().show(new ShowRequest().setComponent(componentKey)).getComponent();
  }

  static WsClient newWsClient() {
    return WsClientFactories.getDefault().newClient(HttpConnector.newBuilder()
      .url(ORCHESTRATOR.getServer().getUrl())
      .build());
  }

}
