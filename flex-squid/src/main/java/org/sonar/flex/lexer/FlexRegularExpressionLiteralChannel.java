/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource SA
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
package org.sonar.flex.lexer;

import com.sonar.sslr.api.Token;
import com.sonar.sslr.impl.Lexer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.sonar.flex.api.FlexTokenType;
import org.sonar.sslr.channel.Channel;
import org.sonar.sslr.channel.CodeReader;

import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.regexp;

/**
 * Provides a heuristic to guess whether a forward slash starts a regular expression.
 * http://stackoverflow.com/questions/7936593/finding-regular-expression-literals-in-a-string-of-javascript-code
 */
public class FlexRegularExpressionLiteralChannel extends Channel<Lexer> {

  private final Channel<Lexer> delegate;

  private static final Set<String> WHOLE_TOKENS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
    "break",
    "case",
    "continue",
    "delete",
    "do",
    "else",
    "finally",
    "in",
    "instanceof",
    "return",
    "throw",
    "try",
    "typeof",
    "void",
    // Binary operators which cannot be followed by a division operator:
    // Match + but not ++. += is handled below.
    "+",
    // Match - but not --. -= is handled below.
    "-",
    // Match . but not a number with a trailing decimal.
    ".",
    // Match /, but not a regexp. /= is handled below.
    "/",
    // Second binary operand cannot start a division.
    ",",
    // Ditto binary operand.
    "*")));

  private static final String[] ENDS = new String[] {
    // ! prefix operator operand cannot start with a division
    "!",
    // % second binary operand cannot start with a division
    "%",
    // &, && ditto binary operand
    "&",
    // ( expression cannot start with a division
    "(",
    // : property value, labelled statement, and operand of ?: cannot start with a division
    ":",
    // ; statement & for condition cannot start with division
    ";",
    // TODO Godin: next line was commented-out in order to support XML
    // , "<" // <, <<, << ditto binary operand
    // !=, !==, %=, &&=, &=, *=, +=, -=, /=, <<=, <=, =, ==, ===, >=, >>=, >>>=,
    // ^=, |=, ||=
    // All are binary operands (assignment ops or comparisons) whose right
    // operand cannot start with a division operator
    "=",
    // >, >>, >>> ditto binary operand
    ">",
    // ? expression in ?: cannot start with a division operator
    "?",
    // [ first array value & key expression cannot start with a division
    "[",
    // ^ ditto binary operand
    "^",
    // { statement in block and object property key cannot start with a division
    "{",
    // |, || ditto binary operand
    "|",
    // } PROBLEMATIC: could be an object literal divided or a block.
    // More likely to be start of a statement after a block which cannot start with a /.
    "}",
    // ~ ditto binary operand
    "~"
  };

  public FlexRegularExpressionLiteralChannel() {
    this.delegate = regexp(FlexTokenType.REGULAR_EXPRESSION_LITERAL, "/([^/\\n\\\\]*+(\\\\.)?+)*+/\\p{javaJavaIdentifierPart}*+");
  }

  @Override
  public boolean consume(CodeReader code, Lexer output) {
    if (code.peek() == '/') {
      Token lastToken = getLastToken(output);
      if (lastToken == null || guessNextIsRegexp(lastToken.getValue())) {
        return delegate.consume(code, output);
      }
    }
    return false;
  }

  private static Token getLastToken(Lexer output) {
    List<Token> tokens = output.getTokens();
    return tokens.isEmpty() ? null : tokens.get(tokens.size() - 1);
  }

  // The exclusion of ++ and -- from the above is also problematic.
  // Both are prefix and postfix operators.
  // Given that there is rarely a good reason to increment a regular expression
  // and good reason to have a post-increment operator as the left operand of
  // a division (x++ / y) this pattern treats ++ and -- as division preceders.

  /**
   * Returns true if a slash after given token starts a regular expression instead of div operator.
   * <p>
   * This fails on some valid but nonsensical JavaScript programs like
   * {@code x = ++/foo/i} which is quite different than
   * {@code x++/foo/i}, but is not known to fail on any known useful programs.
   * </p>
   *
   * @param preceder non-whitespace, non comment token preceding the slash
   */
  // Visible for testing
  static boolean guessNextIsRegexp(String preceder) {
    if (WHOLE_TOKENS.contains(preceder)) {
      return true;
    }
    for (String end : ENDS) {
      if (preceder.endsWith(end)) {
        return true;
      }
    }
    return false;
  }

}
