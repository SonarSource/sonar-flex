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
package org.sonar.flex.grammar.definitions.statements;

import org.junit.Ignore;
import org.junit.Test;
import org.sonar.flex.FlexGrammar;
import static org.sonar.flex.FlexGrammar.FOR_IN_BINDING;
import static org.sonar.flex.FlexGrammar.TYPE_EXPR;
import static org.sonar.flex.FlexGrammar.VARIABLE_BINDING;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.tests.Assertions;

public class GlobalStatementsTest {

  private LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void ifStatement() {
    Assertions.assertThat(g.rule(FlexGrammar.IF_STATEMENT))
      .matches("if (x >1);")
      .matches("if (x >1) {  q=b;}")
      .matches("if (x >1) {  q=b; \n var student : Object = {FirstName:\"John\", LastName:\"Smith\"};}");
  }

  @Test
  public void switchStatement() {
    Assertions.assertThat(g.rule(FlexGrammar.SWITCH_STATEMENT))
      .matches("switch (dayNum) {    case 0: trace(\"Sunday\");"
      + "        break;"
      + "    case 1: trace(\"Monday\");"
      + "        break;"
      + "    default: trace(\"Out of range\");"
      + "        break;}");

    Assertions.assertThat(g.rule(FlexGrammar.SWITCH_STATEMENT))
      .matches("switch (dayNum) {  }")
      .matches("switch (dayNum) {  case 0:}");

    Assertions.assertThat(g.rule(FlexGrammar.SWITCH_STATEMENT))
      .matches("switch (day) {  case 0:trace(\"Sunday\");\n"
      + "  break;\n"
      + " case 1:\n"
      + " trace(\"Monday\");\n"
      + " break; "
      + "default: trace(\"blabla\"); break;}");
  }

  @Test
  public void forStatement() {
    // Demonstrates problem with "allowIn" vs "noIn":
    //Assertions.assertThat(g.rule(FlexGrammar.FOR_IN_BINDING))
    //  .matches("var i:String in myObj");
    //Assertions.assertThat(g.rule(FlexGrammar.FOR_STATEMENT))
    //  .matches("for (var i:String in myObj) { trace(i + \": \" + myObj[i]);}")
    
    Assertions.assertThat(g.rule(FlexGrammar.FOR_STATEMENT))
      .matches("for (i = 0; i < 5; i++) { trace(i);}")
      .matches("for each (var num in myObj) {trace(num);}");
  }
  
  @Ignore
  @Test
  public void helloWorld() {
    Assertions.assertThat(g.rule(FlexGrammar.PROGRAM))
      .matches("package{\n"
      + "\n"
      + "   import flash.display.Sprite;\n"
      + "\n /* comment *()(*\n"
      + "    * blabla"
      + "    */"
      + "   public class FilledCircle extends Sprite {\n"
      + "\n // comment"
      + "\n       function HelloWorld():void {\n"
      + "\n"
      + "         trace(\"Hello World!\");\n"
      + "       }\n"
      + "    }\n"
      + " }");
  }
}
