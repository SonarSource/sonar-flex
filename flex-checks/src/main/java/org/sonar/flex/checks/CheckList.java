/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2021 SonarSource SA
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

import com.google.common.collect.ImmutableList;

import java.util.List;

public final class CheckList {

  public static final String REPOSITORY_KEY = "flex";

  public static final String SONAR_WAY_PROFILE = "Sonar way";

  private CheckList() {
  }

  public static List<Class<?>> getChecks() {
    return ImmutableList.of(
      CommentRegularExpressionCheck.class,
      LineLengthCheck.class,
      ControlFlowStmtDepthCheck.class,
      XPathCheck.class,
      FunctionComplexityCheck.class,
      ClassComplexityCheck.class,
      OneStatementPerLineCheck.class,
      CommentedCodeCheck.class,
      SwitchWithoutDefaultCheck.class,
      WithStatementCheck.class,
      NonEmptyCaseWithoutBreakCheck.class,
      FunctionSinglePointOfExitCheck.class,
      ActionScript2Check.class,
      FunctionWithTooManyParametersCheck.class,
      ConstantNameCheck.class,
      FieldNameCheck.class,
      StarTypeUseCheck.class,
      NotEnoughCaseForSwitchCheck.class,
      EmptyStatementCheck.class,
      TooManyReturnCheck.class,
      CollapsibleIfStatementCheck.class,
      EqEqEqCheck.class,
      ClassNameCheck.class,
      PackageNameCheck.class,
      TooManyLinesInCaseCheck.class,
      PackageDefInClassDefCheck.class,
      OctalValueCheck.class,
      ClassWithTooManyFunctionsCheck.class,
      IfConditionAlwaysTrueOrFalseCheck.class,
      LocalVarAndParameterNameCheck.class,
      BooleanEqualityComparisonCheck.class,
      PublicStaticFieldCheck.class,
      DynamicClassCheck.class,
      LabelPlacementCheck.class,
      ConstructorWithVoidReturnTypeCheck.class,
      ConstructorNotLightweightCheck.class,
      EmptyNestedBlockCheck.class,
      AlertShowUseCheck.class,
      DebugFeaturesCheck.class,
      FunctionOnlyCallsSuperCheck.class,
      HardcodedEventNameCheck.class,
      PublicConstNotStaticCheck.class,
      EventMetadataShouldBeTypedCheck.class,
      ArrayFieldElementTypeCheck.class,
      StarUseForDomainCheck.class,
      OverrideEventCloneFunctionCheck.class,
      StarUseForLocalConnectionCheck.class,
      ExactSettingsSetToFalseCheck.class,
      PrivateStaticConstLoggerCheck.class,
      FunctionNameCheck.class,
      SemicolonCheck.class,
      ObjectTypeUseCheck.class,
      ManagedEventTagWithEventCheck.class,
      ConstructorCallsDispatchEventCheck.class,
      UnusedPrivateFunctionCheck.class,
      LocalVarShadowsFieldCheck.class,
      UnusedPrivateFieldCheck.class,
      UnusedLocalVariableCheck.class,
      TooManyUnaryOperatorCheck.class,
      TooManyLinesInFunctionCheck.class,
      TraceUseCheck.class,
      MethodVisibilityCheck.class,
      NestedSwitchCheck.class,
      EmptyMethodCheck.class,
      ClassWithTooManyFieldsCheck.class,
      UnusedFunctionParametersCheck.class,
      FileHeaderCheck.class,
      DuplicateSwitchCaseConditionCheck.class,
      InstantiationInLoopCheck.class,
      ASDocCheck.class,
      OnEnterFrameUseCheck.class,
      VariantStopConditionInForLoopCheck.class,
      DuplicateBranchImplementationCheck.class,
      DefaultCasePositionCheck.class,
      AllBranchesIdenticalCheck.class,
      ParsingErrorCheck.class);
  }

}
