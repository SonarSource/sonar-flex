/*
 * SonarQube Flex Plugin
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
package com.adobe.ac.pmd.engines;

import com.adobe.ac.pmd.FlexPmdViolations;
import com.adobe.ac.pmd.IFlexViolation;
import com.adobe.ac.pmd.files.IFlexFile;
import com.adobe.ac.pmd.rules.core.ViolationPriority;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map.Entry;

public final class PmdEngineUtils
{
  public static String findFirstViolationError(final FlexPmdViolations violations)
  {
    final StringBuffer buffer = new StringBuffer();
    final String message = "An error violation has been found on the file {0} at "
        + "line {1}, with the rule \"{2}\": {3}";
    final MessageFormat form = new MessageFormat(message);

    for (final Entry<IFlexFile, List<IFlexViolation>> violatedFile : violations.getViolations()
        .entrySet())
    {
      for (final IFlexViolation violation : violatedFile.getValue())
      {
        if (violation.getRule().getPriority() == Integer.parseInt(ViolationPriority.HIGH.toString()))
        {
          final String[] formatArgument = computeArgumentFormat(violation);
          buffer.append(form.format(formatArgument));
          buffer.append('\n');
        }
      }
    }
    return buffer.toString();
  }

  private static String[] computeArgumentFormat(final IFlexViolation violation)
  {
    final String[] formatArgument = new String[]
    {violation.getFilename(),
      String.valueOf(violation.getBeginLine()),
      violation.getRule().getRuleClass(),
      violation.getRuleMessage()};
    return formatArgument;
  }

  private PmdEngineUtils()
  {
  }
}
