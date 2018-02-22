if (a === b) {
  doSomething();
}

if (true) {     // Noncompliant {{Remove this if statement.}}
  doSomething();
}

if (false) {    // Noncompliant
  doSomethingElse();
}
