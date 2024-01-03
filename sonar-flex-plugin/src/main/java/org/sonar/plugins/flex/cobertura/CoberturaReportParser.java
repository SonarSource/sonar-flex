/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2024 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plugins.flex.cobertura;

import com.ctc.wstx.stax.WstxInputFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.CheckForNull;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.api.utils.ParsingUtils;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.flex.core.Flex;

public class CoberturaReportParser {

  private static final Logger LOG = Loggers.get(CoberturaReportParser.class);

  private CoberturaReportParser() {
  }

  /**
   * Parse a Cobertura xml report and create measures accordingly
   */
  public static void parseReport(File xmlFile, final SensorContext context) {
    StaxParser parser = new StaxParser(rootCursor -> {
      rootCursor.advance();
      collectPackageMeasures(rootCursor.descendantElementCursor("package"), context);
    });
    try {
      parser.parse(xmlFile);
    } catch (XMLStreamException e) {
      throw new IllegalStateException(e);
    }
  }

  private static void collectPackageMeasures(SMInputCursor pack, SensorContext context) throws XMLStreamException {
    while (pack.getNext() != null) {
      collectFileMeasures(context, pack.descendantElementCursor("class"));
    }
  }

  private static void collectFileMeasures(SensorContext context, SMInputCursor clazz) throws XMLStreamException {
    FileSystem fileSystem = context.fileSystem();
    FilePredicates predicates = fileSystem.predicates();
    Map<String, InputFile> inputFileByFilename = new HashMap<>();
    while (clazz.getNext() != null) {
      String fileName = clazz.getAttrValue("filename");

      InputFile inputFile;
      // mxml files are not supported by the plugin
      if (inputFileByFilename.containsKey(fileName)) {
        inputFile = inputFileByFilename.get(fileName);
      } else {
        inputFile = findInputFile(fileSystem, predicates, fileName);
        inputFileByFilename.put(fileName, inputFile);
        if (inputFile == null && !fileName.endsWith(".mxml")) {
          LOG.warn("Cannot save coverage result for file: {}, because resource not found.", fileName);
        }
      }
      if (inputFile != null) {
        collectFileData(
          clazz,
          context.newCoverage()
            .onFile(inputFile));
      } else {
        SMInputCursor line = clazz.childElementCursor("lines").advance().childElementCursor("line");
        while (line.getNext() != null) {
          // advance
        }
      }
    }
  }

  @CheckForNull
  private static InputFile findInputFile(FileSystem fileSystem, FilePredicates predicates, String fileName) {
    String key = fileName.startsWith(File.separator) ? fileName : (File.separator + fileName);
    return fileSystem.inputFile(predicates.and(
      predicates.matchesPathPattern("**" + key.replace(File.separator, "/")),
      predicates.hasType(InputFile.Type.MAIN),
      predicates.hasLanguage(Flex.KEY)));
  }

  private static void collectFileData(SMInputCursor clazz, NewCoverage newCoverage) throws XMLStreamException {
    SMInputCursor line = clazz.childElementCursor("lines").advance().childElementCursor("line");
    while (line.getNext() != null) {
      int lineId = Integer.parseInt(line.getAttrValue("number"));
      try {
        newCoverage.lineHits(lineId, (int) ParsingUtils.parseNumber(line.getAttrValue("hits"), Locale.ENGLISH));
      } catch (ParseException e) {
        throw new IllegalStateException(e);
      }

      boolean isBranch = "true".equals(line.getAttrValue("branch"));
      String text = line.getAttrValue("condition-coverage");
      if (isBranch && text != null && !text.trim().isEmpty()) {
        String conditionCoverage = text.replaceFirst("^[^(]*+\\(([^)]*+)\\).*+$", "$1");
        String[] conditions = conditionCoverage.split("/");
        newCoverage.conditions(lineId, Integer.parseInt(conditions[1]), Integer.parseInt(conditions[0]));
      }
    }
    newCoverage.save();
  }

  /**
   * StaxParser copied and simplified from deprecated StaxParser SQ API 5.6
   */
  private static class StaxParser {

    private XmlStreamHandler streamHandler;
    private SMInputFactory inf;

    public StaxParser(XmlStreamHandler streamHandler) {
      this.streamHandler = streamHandler;
      XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
      if (xmlFactory instanceof WstxInputFactory) {
        WstxInputFactory wstxInputfactory = (WstxInputFactory) xmlFactory;
        wstxInputfactory.configureForLowMemUsage();
        wstxInputfactory.getConfig().setUndeclaredEntityResolver((publicID, systemID, baseURI, namespace) -> namespace);
      }
      xmlFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);
      xmlFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
      xmlFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
      xmlFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
      inf = new SMInputFactory(xmlFactory);
    }

    public void parse(File xmlFile) throws XMLStreamException {
      try (FileInputStream input = new FileInputStream(xmlFile)) {
        parse(inf.rootElementCursor(input));
      } catch (IOException e) {
        throw new XMLStreamException(e);
      }
    }

    private void parse(SMHierarchicCursor rootCursor) throws XMLStreamException {
      try {
        streamHandler.stream(rootCursor);
      } finally {
        rootCursor.getStreamReader().closeCompletely();
      }
    }

  }

  @FunctionalInterface
  private interface XmlStreamHandler {
    void stream(SMHierarchicCursor rootCursor) throws XMLStreamException;
  }
}
