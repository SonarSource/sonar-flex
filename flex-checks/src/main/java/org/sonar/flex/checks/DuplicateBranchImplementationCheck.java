/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import java.text.MessageFormat;
import org.sonar.check.Rule;

@Rule(key = "S1871")
public class DuplicateBranchImplementationCheck extends ConditionalStructureCheckBase {
  AstNode prevBranch1 = null;

  @Override
  public void visitConditionalStructure(ConditionalStructure cond) {
    prevBranch1 = null;
    cond.forEachBranchDuplication(
      (branch1, branch2) -> {
        if (branch1 == prevBranch1) {
          return;
        }
        prevBranch1 = branch1;
        String message = MessageFormat.format(
          "Either merge this case with the identical one on line \"{0}\" or change one of the implementations.",
          branch2.getTokenLine());
        addIssue(message, branch1);
      }
    );
  }

}
