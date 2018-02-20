function f() {
  myLocalConnection.allowDomain("*");               // Noncompliant {{Replace this wildcard character '*' with a well defined domain}}
  localConnection.allowDomain("*");                 // Noncompliant
  localConnection.allowDomain("www.myDomain.com");  // OK
  Security.allowDomain("*");                        // OK (out of scope)
}
