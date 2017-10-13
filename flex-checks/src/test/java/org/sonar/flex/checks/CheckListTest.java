/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2017 SonarSource SA
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

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class CheckListTest {

  /**
   * Enforces that each check declared in list.
   */
  @Test
  public void count() {
    int count = 0;
    List<File> files = (List<File>) FileUtils.listFiles(new File("src/main/java/org/sonar/flex/checks/"), new String[] {"java"}, false);
    for (File file : files) {
      if (file.getName().endsWith("Check.java")) {
        count++;
      }
    }
    assertThat(CheckList.getChecks().size()).isEqualTo(count);
  }

  /**
   * Enforces that each check has test, name and description.
   */
  @Test
  public void test() throws IOException {
    List<Class> checks = CheckList.getChecks();

    for (Class cls : checks) {
      String testName = '/' + cls.getName().replace('.', '/') + "Test.class";
      assertThat(getClass().getResource(testName))
          .overridingErrorMessage("No test for " + cls.getSimpleName())
          .isNotNull();
    }

    RulesDefinition.NewRepository repository = new RulesDefinition.Context().createRepository("key", "lang");
    new RulesDefinitionAnnotationLoader().load(repository, checks.toArray(new Class[checks.size()]));
    Iterable<RulesDefinition.NewRule> rules = repository.rules();
    assertThat(rules).isNotEmpty();
    Set<String> ruleKeys = new HashSet<>();
    for (RulesDefinition.NewRule rule : rules) {
      assertThat(getClass().getResource("/org/sonar/l10n/flex/rules/flex/" + rule.key() + ".html"))
          .overridingErrorMessage("No description for " + rule.key())
          .isNotNull();

      try {
        rule.setMarkdownDescription("-42");
      } catch (IllegalStateException e) {
        // it means that the html description was already set in Rule annotation
        fail("Description of " + rule.key() + " should be in separate file");
      }
      ruleKeys.add(rule.key());
    }
    Set<String> nonLinkedFiles = Files.list(Paths.get("src/main/resources/org/sonar/l10n/flex/rules/flex"))
      .map(path -> path.getFileName().toString().replace(".html", ""))
      .filter(s -> !ruleKeys.contains(s))
      .collect(Collectors.toSet());
    assertThat(nonLinkedFiles).as("Unexpected html description files found").isEmpty();
  }

}
