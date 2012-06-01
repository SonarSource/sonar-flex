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
package org.sonar.plugins.flex.duplications;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

import net.sourceforge.pmd.cpd.SourceCode;
import net.sourceforge.pmd.cpd.TokenEntry;
import net.sourceforge.pmd.cpd.Tokenizer;
import net.sourceforge.pmd.cpd.Tokens;

import org.apache.commons.io.IOUtils;
import org.sonar.api.batch.AbstractCpdMapping;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.flex.core.Flex;
import org.sonar.plugins.flex.duplications.internal.Token;
import org.sonar.plugins.flex.duplications.internal.TokenChunker;
import org.sonar.plugins.flex.duplications.internal.TokenQueue;

/**
 * Temporary Flex CPD engine mapping class, used until we can migrate to Sonar CPD Engine.
 * 
 */
public class FlexCpdMapping extends AbstractCpdMapping {

  private Flex flex;
  private Project project;

  /**
   * Creates a {@link FlexCpdMapping} object.
   * <br/>
   * <b>Do not call, this constructor is called by Pico container.</b>
   * 
   * @param flex
   * @param project
   */
  public FlexCpdMapping(Flex flex, Project project) {
    this.flex = flex;
    this.project = project;
  }

  /**
   * Returns the language
   * 
   * @return the language
   */
  public Language getLanguage() {
    return flex;
  }

  /**
   * Returns the tokenizer
   * 
   * @return the tokenizer
   */
  public Tokenizer getTokenizer() {
    return new FlexCpdTokenizer();
  }

  class FlexCpdTokenizer implements Tokenizer {

    private TokenChunker tokenChunker;
    private static final String IMPORT_KEYWORD = "import";
    private static final String SEMI_COLON = ";";

    /**
     * Creates a {@link {@link FlexCpdTokenizer}
     */
    public FlexCpdTokenizer() {
      this.tokenChunker = FlexTokenProducer.build();
    }

    /**
     * Cuts the given source into a list of tokens.
     */
    public final void tokenize(SourceCode source, Tokens cpdTokens) {
      String fileName = source.getFileName();

      Reader reader = null;
      try {
        reader = new InputStreamReader(new FileInputStream(fileName), project.getFileSystem().getSourceCharset());
        TokenQueue queue = tokenChunker.chunk(reader);

        Iterator<Token> iterator = queue.iterator();
        // we currently use this hack to remove "import" directives
        boolean importDirective = false;
        while (iterator.hasNext()) {
          Token token = (Token) iterator.next();
          if (token.getValue().equalsIgnoreCase(IMPORT_KEYWORD)) {
            importDirective = true;
          } else if (importDirective) {
            // We do nothing as we want to ignore "import" directives
            if (token.getValue().equalsIgnoreCase(SEMI_COLON)) {
              importDirective = false;
            }
          } else {
            cpdTokens.add(new TokenEntry(token.getValue(), fileName, token.getLine()));
          }
        }
      } catch (FileNotFoundException e) {
        throw new SonarException(e);
      } finally {
        IOUtils.closeQuietly(reader);
      }

      cpdTokens.add(TokenEntry.getEOF());
    }

  }

}
