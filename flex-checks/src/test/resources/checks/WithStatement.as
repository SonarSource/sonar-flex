function sayHello() {
  with (foo) { // Noncompliant {{Usage of "with" statement should be avoided. Instead, prefer explicitly specify variable scopes to make code clearer.}}
  }
}
