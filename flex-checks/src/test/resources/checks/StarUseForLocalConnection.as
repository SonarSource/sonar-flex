function f() {
  myLocalConnection.allowDomain("*");               // NOK
  localConnection.allowDomain("*");                 // NOK
  localConnection.allowDomain("www.myDomain.com");  // OK
  Security.allowDomain("*");                        // OK (out of scope)
}
