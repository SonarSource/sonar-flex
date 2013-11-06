if (a === b) {        // NOK
  doSomething();
}

if (true) {
  doSomething();      // NOK
}

if (false) {
  doSomethingElse();  // NOK
}