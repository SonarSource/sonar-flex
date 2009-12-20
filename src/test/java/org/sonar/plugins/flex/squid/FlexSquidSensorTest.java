package org.sonar.plugins.flex.squid;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.resources.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import org.junit.Test;

public class FlexSquidSensorTest {
  @Test
  public void computesLines() throws IOException, URISyntaxException {
    File flex = new File(getClass().getResource("/org/sonar/plugins/flex/squid/TimeFormatter.as").toURI());

    ProjectFileSystem fileSystem = mock(ProjectFileSystem.class);
    when(fileSystem.getSourceCharset()).thenReturn(Charset.defaultCharset());
    SensorContext context = mock(SensorContext.class);
    new FlexSquidSensor().analyzeFile(flex, fileSystem, context);

    verify(fileSystem).toResource(flex);
    verify(context).saveMeasure((Resource) anyObject(), eq(CoreMetrics.LINES), eq(102.0));
  }
}

