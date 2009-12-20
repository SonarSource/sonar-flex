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

package org.sonar.plugins.flex.flexmetrics;

import org.junit.Test;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.plugins.flex.flexdecorators.FlexCommentsDensityDecorator;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class FlexCommentsDensityDecoratorTest {

  @Test
  public void testCommentsDensityConsolidation() {
    DecoratorContext context = mock(DecoratorContext.class);
    FlexCommentsDensityDecorator decorator = new FlexCommentsDensityDecorator();
    when(context.getMeasure(CoreMetrics.COMMENT_LINES)).thenReturn(new Measure(CoreMetrics.COMMENT_LINES, 24.0));
    when(context.getMeasure(CoreMetrics.NCLOC)).thenReturn(new Measure(CoreMetrics.NCLOC, 96.0));
    decorator.decorate(null, context);

    verify(context).saveMeasure(CoreMetrics.COMMENT_LINES_DENSITY, 20.0);
  }

  @Test
  public void testCommentsDensityWhenNoNCLOC() {
    DecoratorContext context = mock(DecoratorContext.class);
    FlexCommentsDensityDecorator decorator = new FlexCommentsDensityDecorator();
    when(context.getMeasure(CoreMetrics.COMMENT_LINES)).thenReturn(new Measure(CoreMetrics.COMMENT_LINES, 24.0));
    when(context.getMeasure(CoreMetrics.NCLOC)).thenReturn(null);
    decorator.decorate(null, context);

    verify(context, never()).saveMeasure(eq(CoreMetrics.COMMENT_LINES_DENSITY), anyDouble());
  }

  @Test
  public void testCommentsDensityWhenNoComments() {
    DecoratorContext context = mock(DecoratorContext.class);
    FlexCommentsDensityDecorator decorator = new FlexCommentsDensityDecorator();
    when(context.getMeasure(CoreMetrics.COMMENT_LINES)).thenReturn(null);
    when(context.getMeasure(CoreMetrics.NCLOC)).thenReturn(new Measure(CoreMetrics.NCLOC, 96.0));
    decorator.decorate(null, context);

    verify(context, never()).saveMeasure(eq(CoreMetrics.COMMENT_LINES_DENSITY), anyDouble());
  }
}
