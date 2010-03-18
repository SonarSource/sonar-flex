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

package org.sonar.plugins.flex.cpd;

import org.sonar.plugins.flex.FlexFile;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.test.IsResource;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Project;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.Ignore;

import java.net.URISyntaxException;
import java.io.File;

@Ignore
public class CpdMavenSensorTest {

  @Test
  public void shouldGenerateClassMeasures() throws Exception {

    SensorContext context = mock(SensorContext.class);

    when(context.getResource(new FlexFile("BatchedQuery"))).thenReturn(new FlexFile("BatchedQuery").setEffectiveKey("testGroupId:testArtifactId:[default].BatchedQuery"));

    analyse(context, "/org/sonar/plugins/flex/cpd/cpd.xml");

    //verify(context, times(2)).saveMeasure(argThat(new IsResource(anyString(), anyString())), eq(CoreMetrics.DUPLICATED_FILES), eq(1d));

    verify(context).saveMeasure(argThat(new IsResource(Resource.SCOPE_ENTITY, Resource.QUALIFIER_CLASS, "[default].BatchedQuery")), eq(CoreMetrics.DUPLICATED_LINES), eq(45d));
    verify(context).saveMeasure(argThat(new IsResource(Resource.SCOPE_ENTITY, Resource.QUALIFIER_CLASS, "com.almirun.common.data.BatchedQuery")), eq(CoreMetrics.DUPLICATED_LINES), eq(45d));

    verify(context).saveMeasure(argThat(new IsResource(Resource.SCOPE_ENTITY, Resource.QUALIFIER_CLASS, "[default].BatchedQuery")), eq(CoreMetrics.DUPLICATED_BLOCKS), eq(1d));
    verify(context).saveMeasure(argThat(new IsResource(Resource.SCOPE_ENTITY, Resource.QUALIFIER_CLASS, "com.almirun.common.data.BatchedQuery")), eq(CoreMetrics.DUPLICATED_BLOCKS), eq(1d));
  }

  private SensorContext analyse(SensorContext context, String xmlFilename) throws URISyntaxException {
    File xmlFile = new File(getClass().getResource(xmlFilename).toURI());
    FlexCpdMavenPluginHandler pluginHandler = mock(FlexCpdMavenPluginHandler.class);

    FlexCpdMavenSensor sensor = new FlexCpdMavenSensor(pluginHandler);
    Project pom = mock(Project.class);
    sensor.collect(xmlFile, pom, context);
    return context;
  }
}
