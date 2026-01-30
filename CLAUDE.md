# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SonarSource Flex Plugin - a static code analyzer for ActionScript/Flex language that integrates with SonarQube. The plugin parses Flex/ActionScript source files, computes metrics, and reports code quality issues.

## Build Commands

```bash
# Build entire project (skip tests)
mvn clean install -DskipTests

# Build with tests
mvn clean install

# Run unit tests for a specific module
mvn test -pl flex-checks

# Run a single test class
mvn test -pl flex-checks -Dtest=EmptyStatementCheckTest

# Run integration tests (requires QA environment)
mvn verify -Pit-ruling -pl its/ruling
```

## Module Structure

- **flex-squid**: Core parsing library using SonarSource SSLR (SonarSource Language Recognizer)
  - `FlexGrammar.java` - Complete ActionScript 3 grammar definition
  - `FlexVisitor.java` - AST visitor base class for traversing parsed trees
  - `FlexCheck.java` - Base interface for implementing code checks

- **flex-checks**: All code quality rules (~80 checks)
  - `CheckList.java` - Registry of all available checks
  - Each check extends `FlexVisitor` and implements `FlexCheck`

- **sonar-flex-plugin**: SonarQube plugin assembly
  - `FlexPlugin.java` - Plugin entry point, registers extensions
  - `FlexSquidSensor.java` - Main sensor that orchestrates file analysis

- **sslr-flex-toolkit**: Development tool for testing grammar

- **its/**: Integration tests (skipped by default)
  - `its/plugin` - Plugin integration tests
  - `its/ruling` - Rule regression tests against sample projects

## Writing Checks

Checks are AST visitors that subscribe to specific grammar nodes:

```java
public class MyCheck extends FlexVisitor implements FlexCheck {
  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.IF_STATEMENT);
  }

  @Override
  public void visitNode(AstNode node) {
    // Analyze node and report issues
    addIssue("Message", node);
  }
}
```

Test files use comment annotations for expected issues:
```actionscript
if (true) { } // Noncompliant {{Expected message}}
```

Testing uses `FlexVerifier`:
```java
FlexVerifier.verify(new File("src/test/resources/checks/MyCheck.as"), new MyCheck());
```

## Key Dependencies

- **SSLR** (`org.sonarsource.sslr`) - Parser framework
- **sonar-plugin-api** - SonarQube plugin API (provided scope)
- **sonar-analyzer-commons** - Shared analyzer utilities

## Grammar Reference

The grammar in `FlexGrammar.java` defines all ActionScript 3 constructs. Key entry points:
- `PROGRAM` - Root rule
- `CLASS_DEF`, `INTERFACE_DEF` - Type definitions
- `FUNCTION_DEF` - Function declarations
- `STATEMENT` - All statement types
- `ASSIGNMENT_EXPR` - Expression hierarchy root
