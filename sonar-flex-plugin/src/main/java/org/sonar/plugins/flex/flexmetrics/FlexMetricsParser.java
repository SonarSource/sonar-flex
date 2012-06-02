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

package org.sonar.plugins.flex.flexmetrics;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.BatchExtension;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.RangeDistributionBuilder;
import org.sonar.api.utils.ParsingUtils;
import org.sonar.api.utils.StaxParser;
import org.sonar.plugins.flex.core.FlexResourceBridge;
import org.sonar.plugins.flex.core.FlexUtils;

import javax.xml.stream.XMLStreamException;

import java.io.File;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that parses the report of Flex PMD Metrics
 */
public class FlexMetricsParser implements BatchExtension {

  private static final Number[] FUNCTIONS_DISTRIB_BOTTOM_LIMITS = {1, 2, 4, 6, 8, 10, 12};
  private static final Number[] CLASSES_DISTRIB_BOTTOM_LIMITS = {0, 5, 10, 20, 30, 60, 90};

  private SensorContext context;
  private FlexResourceBridge resourceBridge;

  /**
   * Creates a {@link FlexMetricsParser}.
   * <br/>
   * <b>Do not call, this constructor is called by Pico container.</b>
   *
   * @param context the sensor context
   * @param resourceBridge the resource bridge
   */
  public FlexMetricsParser(SensorContext context, FlexResourceBridge resourceBridge) {
    this.context = context;
    this.resourceBridge = resourceBridge;
  }

  /**
   * Parses the XML report
   *
   * @param xmlFile the report file
   */
  public void parse(File xmlFile) {
    StaxParser parser = new StaxParser(new StaxParser.XmlStreamHandler() {
      public void stream(SMHierarchicCursor rootCursor) throws XMLStreamException {
        rootCursor.advance(); // <javancss>
        SMInputCursor cursor = rootCursor.childElementCursor();
        while (cursor.getNext() != null) {
          String nodeName = cursor.getLocalName();
          if (StringUtils.equals("packages", nodeName)) {
            SMInputCursor packagesCursor = cursor.childElementCursor("package");
            processPackages(packagesCursor);
          } else if (StringUtils.equals("objects", nodeName)) {
            SMInputCursor objectsCursor = cursor.childElementCursor("object");
            processObjects(objectsCursor);
          } else if (StringUtils.equals("functions", nodeName)) {
            SMInputCursor functionsCursor = cursor.childElementCursor("function");
            processFunctions(functionsCursor);
          }
        }
      }
    });

    try {
      parser.parse(xmlFile);
    } catch (XMLStreamException e) {
      FlexUtils.LOG.error("Error parsing file : " + xmlFile, e);
    }
  }

  private void processPackages(SMInputCursor packagesCursor) throws XMLStreamException {
    while (packagesCursor.getNext() != null) {
      Map<String, String> values = processChildren(packagesCursor);
      String packageName = values.get("name");
      if (!packageName.equals(".")) {
        context.saveMeasure(resourceBridge.findDirectory(packageName), CoreMetrics.PACKAGES, 1.0);
      }
    }
  }

  private void processObjects(SMInputCursor objectsCursor) throws XMLStreamException {
    while (objectsCursor.getNext() != null) {
      Map<String, String> values = processChildren(objectsCursor);

      org.sonar.api.resources.File file = resourceBridge.findFile(values.get("name"));

      if (file != null) {
        // double lines = parseDouble(values.get("javadocs"))
        // + parseDouble(values.get("single_comment_lines"))
        // + parseDouble(values.get("multi_comment_lines"));

        // context.saveMeasure(file, CoreMetrics.COMMENT_LINES, lines);
        // context.saveMeasure(file, CoreMetrics.CLASSES, 1.0);
        // context.saveMeasure(file, CoreMetrics.FILES, 1.0);
        // context.saveMeasure(file, CoreMetrics.NCLOC, parseDouble(values.get("ncss")));
        // context.saveMeasure(file, CoreMetrics.FUNCTIONS, parseDouble(values.get("functions")));
      }
    }
  }

  private void processFunctions(SMInputCursor functionsCursor) throws XMLStreamException {
    Map<String, Integer> ccnCountPerClass = new HashMap<String, Integer>();
    Map<String, RangeDistributionBuilder> ccnDistributionPerClass = new HashMap<String, RangeDistributionBuilder>();

    while (functionsCursor.getNext() != null) {
      Map<String, String> values = processChildren(functionsCursor);
      String fullFunctionName = values.get("name");
      Integer ccnForFunction = Integer.valueOf(values.get("ccn"));
      String packageAndClassName = getPackageAndClassFromFunction(fullFunctionName);
      addUpComplexityToClass(ccnCountPerClass, ccnForFunction, packageAndClassName);
      addUpComplexityToClassDistribution(ccnDistributionPerClass, ccnForFunction, packageAndClassName);
    }

    for (Map.Entry<String, Integer> entry : ccnCountPerClass.entrySet()) {
      String fullname = entry.getKey();
      org.sonar.api.resources.File file = resourceBridge.findFile(fullname);
      if (file != null) {
        Integer ccnForClass = entry.getValue();
        context.saveMeasure(file, CoreMetrics.COMPLEXITY, ccnForClass.doubleValue());
        RangeDistributionBuilder ccnDistribution =
            new RangeDistributionBuilder(CoreMetrics.CLASS_COMPLEXITY_DISTRIBUTION, CLASSES_DISTRIB_BOTTOM_LIMITS);
        ccnDistribution.add(ccnForClass.doubleValue());
        context.saveMeasure(file, ccnDistribution.build().setPersistenceMode(PersistenceMode.MEMORY));
      }
    }
    for (Map.Entry<String, RangeDistributionBuilder> entry : ccnDistributionPerClass.entrySet()) {
      String fullname = entry.getKey();
      org.sonar.api.resources.File file = resourceBridge.findFile(fullname);
      if (file != null) {
        RangeDistributionBuilder ccnDistributionForClass = entry.getValue();
        context.saveMeasure(file, ccnDistributionForClass.build().setPersistenceMode(PersistenceMode.MEMORY));
      }
    }
  }

  private static void addUpComplexityToClass(Map<String, Integer> ccnCountperClass, Integer ccnForFunction, String packageAndClassName) {
    Integer ccnSum = ccnCountperClass.get(packageAndClassName);
    if (ccnSum == null) {
      ccnSum = 0;
    }
    ccnSum += ccnForFunction;
    ccnCountperClass.put(packageAndClassName, ccnSum);
  }

  private static void addUpComplexityToClassDistribution(Map<String, RangeDistributionBuilder> ccnDistributionperClass,
      Integer ccnForFunction,
      String packageAndClassName) {
    RangeDistributionBuilder ccnDistribution = ccnDistributionperClass.get(packageAndClassName);
    if (ccnDistribution == null) {
      ccnDistribution = new RangeDistributionBuilder(CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION, FUNCTIONS_DISTRIB_BOTTOM_LIMITS);
      ccnDistributionperClass.put(packageAndClassName, ccnDistribution);
    }
    ccnDistribution.add(ccnForFunction);
  }

  static String getPackageAndClassFromFunction(String functionName) {
    return StringUtils.substringBefore(functionName, "::");
  }

  private static Map<String, String> processChildren(SMInputCursor cursor) throws XMLStreamException {
    SMInputCursor objectCursor = cursor.childElementCursor();
    Map<String, String> values = Maps.newHashMap();
    while (objectCursor.getNext() != null) {
      String nodeName = objectCursor.getLocalName();
      values.put(nodeName, objectCursor.collectDescendantText());
    }
    return values;
  }

  private static double parseDouble(String value) {
    if (StringUtils.isBlank(value)) {
      return 0.0;
    }
    try {
      return ParsingUtils.parseNumber(value);
    } catch (ParseException e) {
      return 0.0;
    }
  }
}
