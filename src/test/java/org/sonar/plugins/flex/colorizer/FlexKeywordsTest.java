package org.sonar.plugins.flex.colorizer;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class FlexKeywordsTest {
  @Test
  public void testGet() throws Exception {
    assertThat(FlexKeywords.get().size(), is(75));
  }
}
