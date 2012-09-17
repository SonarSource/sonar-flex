function foo() {
  switch (param) {
    case 0: // OK
    case 1: // OK
      break;
    case 2: // OK
      return;
    case 3: // OK
      throw new Exception();
    default: // OK
      break;
  }

  switch (param) {
    case 0: // NOK
      doSomething();
    default: // NOK
      doSomethingElse();
  }
}
