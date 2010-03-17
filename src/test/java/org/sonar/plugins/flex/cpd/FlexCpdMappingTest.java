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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.pmd.cpd.*;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class FlexCpdMappingTest {

  @Test
  public void testSetLoadSourceCodeSlicesToFalse() throws IOException {
    TokenEntry.clearImages();
    AbstractLanguage cpdLanguage = new AbstractLanguage(new JavaTokenizer()) {
    };
    CPD cpd = new CPD(50, cpdLanguage);
    cpd.setEncoding(Charset.defaultCharset().name());
    cpd.add(FileUtils.toFile(CPD.class.getResource("/org/sonar/plugins/flex/cpd/CPDFile1.as3")));
    cpd.add(FileUtils.toFile(CPD.class.getResource("/org/sonar/plugins/flex/cpd/CPDFile2.as3")));
    cpd.go();

    List<Match> matches = getMatches(cpd);
    assertThat(matches.size(), is(1));

    Match match = matches.get(0);
    assertThat(match.getLineCount(), is(23));
    assertThat(match.getFirstMark().getBeginLine(), is(7));
  }

  private List<Match> getMatches(CPD cpd) {
    List<Match> matches = new ArrayList<Match>();
    Iterator<Match> matchesIter = cpd.getMatches();
    while (matchesIter.hasNext()) {
      matches.add(matchesIter.next());
    }
    return matches;
  }
}
