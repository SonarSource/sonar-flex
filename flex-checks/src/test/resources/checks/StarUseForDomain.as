function f() {
  Security.allowDomain("*");                 // Noncompliant

  Security.allowDomain("www.myDomain.com");
}
