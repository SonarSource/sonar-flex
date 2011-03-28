/*
 * Sonar Flex Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.plugins.flex.flexpmd;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.utils.ValidationMessages;

import java.io.InputStreamReader;
import java.io.Reader;

public class FlexPmdProfileImporterTest {

  private FlexPmdProfileImporter importer;

  @Before
  public void setUp() {
    importer = new FlexPmdProfileImporter();
  }

  @Test
  public void shouldImport() {
    Reader reader = createProfileReader("flexpmd-simple-profile-with-fake.xml");
    ValidationMessages messages = ValidationMessages.create();
    RulesProfile profile = importer.importProfile(reader, messages);

    assertThat(messages.hasErrors(), is(false));
    assertThat(messages.hasWarnings(), is(false));
    assertThat(messages.hasInfos(), is(false));
    assertThat(profile.getActiveRules().size(), is(7));
  }

  @Test
  public void shouldImport2() {
    Reader reader = createProfileReader("flexpmd-fullmonty-profile.xml");
    ValidationMessages messages = ValidationMessages.create();
    RulesProfile profile = importer.importProfile(reader, messages);

    assertThat(messages.hasErrors(), is(false));
    assertThat(messages.hasWarnings(), is(false));
    assertThat(messages.hasInfos(), is(false));
    assertThat(profile.getActiveRules().size(), is(66));
  }

  @Test
  public void testImportWrongFormat() {
    RulesProfile profile = importer.importProfile(createProfileReader("flexpmd-wrong-format-import.xml"), ValidationMessages.create());
    assertThat(profile.getActiveRules().size(), is(5));
  }

  @Test
  public void testExplicitPriority() {
    RulesProfile profile = importer.importProfile(createProfileReader("flexpmd-explicit-priority.xml"), ValidationMessages.create());
    assertThat(profile.getActiveRules().size(), is(1));
    assertThat(profile.getActiveRules().get(0).getSeverity(), is(RulePriority.CRITICAL));
  }

  @Test
  public void testImplicitPriority() {
    RulesProfile profile = importer.importProfile(createProfileReader("flexpmd-implicit-priority.xml"), ValidationMessages.create());
    assertThat(profile.getActiveRules().size(), is(1));
    assertThat(profile.getActiveRules().get(0).getSeverity(), is(RulePriority.BLOCKER));
  }

  static Reader createProfileReader(String name) {
    return new InputStreamReader(FlexPmdProfileImporterTest.class.getResourceAsStream("/org/sonar/plugins/flex/flexpmd/" + name));
  }

}
