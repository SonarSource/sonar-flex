/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010 SonarSource
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

package org.sonar.plugins.flex.flexpmd;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.DefaultProjectFileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.*;
import org.sonar.plugins.flex.FlexFile;
import org.apache.commons.io.FileUtils;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;

public class FlexPmdViolationsXmlParserTest {

  @Test
  public void shouldSaveViolationsOnClasses() throws URISyntaxException, XMLStreamException {
    SensorContext context = mock(SensorContext.class);

    RulesManager manager = mock(RulesManager.class);
    when(manager.getPluginRule(anyString(), anyString())).thenAnswer(new Answer<Rule>() {
      public Rule answer(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        return new Rule((String) args[1], (String) args[1], null, (String) args[0], "");
      }
    });

    DefaultProjectFileSystem fileSystem = mock(DefaultProjectFileSystem.class);
    when(fileSystem.getSourceDirs()).thenReturn(Arrays.asList(new File("E:/TestHudson/flex/src")));

    Project project = mock(Project.class);
    when(project.getFileSystem()).thenReturn(fileSystem);


    FlexPmdRulesRepository repository = new FlexPmdRulesRepository(null);
    RulesProfile profile = repository.getProvidedProfiles().get(0);

    FlexPmdViolationsXmlParser parser = new FlexPmdViolationsXmlParser(project, context, manager, profile);


    File xmlFile = FileUtils.toFile(getClass().getResource("/org/sonar/plugins/flex/flexpmd/flexpmd-result-file.xml"));
    parser.parse(xmlFile);

    verify(context, times(18)).saveViolation(argThat(new IsViolationOnFlexClass()));
  }


  private class IsViolationOnFlexClass extends BaseMatcher<Violation> {

    private FlexFile flexClass;

    private IsViolationOnFlexClass() {
    }

    public boolean matches(Object o) {
      Violation v = (Violation) o;
      boolean ok = (v.getResource() != null) && (v.getResource() instanceof FlexFile);
      if (ok && flexClass != null) {
        ok = flexClass.equals(v.getResource());
      }
      return ok;
    }

    public void describeTo(Description description) {
    }
  }
}
