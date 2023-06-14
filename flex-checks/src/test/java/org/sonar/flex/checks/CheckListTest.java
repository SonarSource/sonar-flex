/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2023 SonarSource SA
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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CheckListTest {

  /**
   * Enforces that each check declared in list.
   */
  @Test
  public void count() throws IOException {
    Path path = FileSystems.getDefault().getPath("src/main/java/org/sonar/flex/checks/");
    try (Stream<Path> stream = Files.list(path).filter(file -> file.getFileName().toString().endsWith("Check.java"))) {
      assertThat(CheckList.getChecks()).hasSize((int) stream.count());
    }
  }

  /**
   * Enforces that each check has plain_text_header, name and description.
   */
  @Test
  public void test() throws IOException {
    List<Class<?>> checks = CheckList.getChecks();

    for (Class cls : checks) {
      String testName = '/' + cls.getName().replace('.', '/') + "Test.class";
      assertThat(getClass().getResource(testName))
          .overridingErrorMessage("No plain_text_header for " + cls.getSimpleName())
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
      .filter(path -> path.endsWith(".html"))
      .map(path -> path.getFileName().toString().replace(".html", ""))
      .filter(s -> !ruleKeys.contains(s))
      .collect(Collectors.toSet());
    assertThat(nonLinkedFiles).as("Unexpected html description files found").isEmpty();
  }

}
