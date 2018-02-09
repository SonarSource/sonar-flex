/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2018 SonarSource SA
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
package org.sonar.flex.checks;

import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.impl.Parser;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexConfiguration;
import org.sonar.flex.FlexVisitorContext;
import org.sonar.flex.Issue;
import org.sonar.flex.parser.FlexParser;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.sslr.parser.LexerlessGrammar;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FlexCheckTester {

  public static Collection<CheckMessage> checkMessages(File file, FlexCheck check) {
    Parser<LexerlessGrammar> parser = FlexParser.create(new FlexConfiguration(UTF_8));
    FlexVisitorContext context;
    try {
      context = new FlexVisitorContext(file, parser.parse(file));
    } catch (RecognitionException e) {
      context = new FlexVisitorContext(file, e);
    }

    List<CheckMessage> messages = new ArrayList<>();
    for (Issue issue : check.scanFileForIssues(context)) {
      CheckMessage checkMessage = new CheckMessage(check, issue.message());
      Integer line = issue.line();
      if (line != null) {
        checkMessage.setLine(line);
      }
      messages.add(checkMessage);
    }
    return messages;
  }

}
