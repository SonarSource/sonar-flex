package P {  // Compliant
  class A {
  }
}

class P.A {  // Noncompliant {{Make the Package definition nest the Class definition}}
}
