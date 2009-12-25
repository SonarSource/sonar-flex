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
import org.sonar.api.test.IsViolation;
import org.sonar.plugins.flex.FlexFile;
import org.apache.commons.io.FileUtils;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;

public class PmdViolationsXmlParserTest {

  @Test
  public void shouldSaveViolationsOnClasses() throws URISyntaxException, XMLStreamException {
    SensorContext context = mock(SensorContext.class);
    parse(context, "/org/sonar/plugins/flex/flexpmd/flexpmd-result-file.xml");

    verify(context, times(30)).saveViolation(argThat(new IsViolationOnFlexClass()));
    verify(context, times(4)).saveViolation(argThat(new IsViolationOnFlexClass(new FlexFile("ch.hortis.sonar.mvn.ClassWithComments"))));

    Violation wanted = new Violation(null, new FlexFile("ch.hortis.sonar.mvn.ClassWithComments"))
        .setMessage("Avoid unused local variables such as 'toto'.")
        .setLineId(22);
    verify(context, times(1)).saveViolation(argThat(new IsViolation(wanted)));
  }

  @Test
  public void defaultPackageShouldBeSetOnclassWithoutPackage() throws URISyntaxException, XMLStreamException {
    SensorContext context = mock(SensorContext.class);
    parse(context, "/org/sonar/plugins/flex/pmd-class-without-package.xml");
    verify(context, times(3)).saveViolation(argThat(new IsViolationOnFlexClass(new FlexFile("ClassOnDefaultPackage"))));
  }

  @Test
  public void unknownXMLEntity() throws URISyntaxException, XMLStreamException {
    SensorContext context = mock(SensorContext.class);
    parse(context, "/org/sonar/plugins/flex/pmd-result-with-unknown-entity.xml");
    verify(context, times(2)).saveViolation(argThat(new IsViolationOnFlexClass(new FlexFile("test.Test"))));
  }


  @Test
  public void ISOControlCharsXMLFile() throws URISyntaxException, XMLStreamException {
    SensorContext context = mock(SensorContext.class);
    parse(context, "/org/sonar/plugins/flex/pmd-result-with-control-char.xml");
    verify(context, times(1)).saveViolation(argThat(new IsViolationOnFlexClass(new FlexFile("test.Test"))));
  }

  private void parse(SensorContext context, String xmlPath) throws URISyntaxException, XMLStreamException {
    DefaultProjectFileSystem fileSystem = mock(DefaultProjectFileSystem.class);
    when(fileSystem.getSourceDirs()).thenReturn(Arrays.asList(new File("/test/src/main/java")));

    Project project = mock(Project.class);
    when(project.getFileSystem()).thenReturn(fileSystem);

    RulesManager manager = mock(RulesManager.class);
    when(manager.getPluginRule(anyString(), anyString())).thenAnswer(new Answer<Rule>() {
      public Rule answer(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        return new Rule((String) args[1], (String) args[1], null, (String) args[0], "");
      }
    });
    RulesProfile profile = mock(RulesProfile.class);
    when(profile.getActiveRule(anyString(), anyString())).thenReturn(new ActiveRule(null, null, RulePriority.MINOR));
    PmdViolationsXmlParser parser = new PmdViolationsXmlParser(project, context, manager, profile);

    File xmlFile = FileUtils.toFile(getClass().getResource(xmlPath));
    parser.parse(xmlFile);
  }


  private class IsViolationOnFlexClass extends BaseMatcher<Violation> {

    private FlexFile flexClass;


    private IsViolationOnFlexClass(FlexFile flexClass) {
      this.flexClass = flexClass;
    }

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
