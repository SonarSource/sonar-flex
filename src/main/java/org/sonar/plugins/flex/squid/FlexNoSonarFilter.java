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

package org.sonar.plugins.flex.squid;

import org.sonar.squid.text.Source;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.Violation;
import org.sonar.api.rules.ViolationFilter;

import java.util.HashMap;
import java.util.Map;

public class FlexNoSonarFilter implements ViolationFilter {

  private Map<Resource, Source> squidSourceFiles = new HashMap<Resource, Source>();

  protected FlexNoSonarFilter addSquidSource(Resource sonarResource, Source squidSource) {
    if (sonarResource != null && squidSource != null) {
      squidSourceFiles.put(sonarResource, squidSource);
    }
    return this;
  }

  public boolean isIgnored(Violation violation) {
    if (violation.getResource() != null && violation.getLineId() != null) {
      Source source = squidSourceFiles.get(violation.getResource());
      if (source != null) {
        return source.getNoSonarTagLines().contains(violation.getLineId());
      }
    }
    return false;
  }
}
