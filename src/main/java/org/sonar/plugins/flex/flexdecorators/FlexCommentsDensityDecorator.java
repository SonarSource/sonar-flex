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
package org.sonar.plugins.flex.flexdecorators;

import org.sonar.plugins.flex.Flex;
import org.sonar.api.batch.Decorator;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Project;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;

import java.util.Arrays;
import java.util.List;

public class FlexCommentsDensityDecorator implements Decorator {

  @DependsUpon
  public List<Metric> dependsOnMetrics() {
    return Arrays.asList(CoreMetrics.NCLOC, CoreMetrics.COMMENT_LINES);
  }

  @DependedUpon
  public List<Metric> generatesMetrics() {
    return Arrays.asList(CoreMetrics.COMMENT_LINES_DENSITY);
  }

  public void decorate(Resource resource, DecoratorContext context) {
    Measure ncloc = context.getMeasure(CoreMetrics.NCLOC);
    Measure comments = context.getMeasure(CoreMetrics.COMMENT_LINES);
    if (ncloc != null && comments != null) {
      context.saveMeasure(CoreMetrics.COMMENT_LINES_DENSITY, 100.0 * (comments.getValue() / (comments.getValue() + ncloc.getValue())));
    }
  }

  public boolean shouldExecuteOnProject(Project project) {
    return Flex.INSTANCE.equals(project.getLanguageKey());
  }
}
