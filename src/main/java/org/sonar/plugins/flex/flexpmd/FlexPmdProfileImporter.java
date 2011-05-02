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

import org.apache.commons.io.IOUtils;
import org.sonar.api.profiles.ProfileImporter;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.flex.Flex;
import org.sonar.plugins.flex.flexpmd.xml.FlexRulesUtils;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class FlexPmdProfileImporter extends ProfileImporter {

  public FlexPmdProfileImporter() {
    super(FlexPmdConstants.REPOSITORY_KEY, FlexPmdConstants.REPOSITORY_NAME);
    setSupportedLanguages(Flex.KEY);
  }

  @Override
  public RulesProfile importProfile(Reader reader, ValidationMessages messages) {
    RulesProfile profile = RulesProfile.create();
    try {
      List<ActiveRule> activeRules = FlexRulesUtils.importConfiguration(
          IOUtils.toString(reader),
          FlexRulesUtils.getInitialReferential());
      profile.setActiveRules(activeRules);
    } catch (IOException e) {
      messages.addErrorText(e.getMessage());
    }
    return profile;
  }
}
