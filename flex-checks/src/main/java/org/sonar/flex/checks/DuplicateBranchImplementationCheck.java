/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2020 SonarSource SA
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
