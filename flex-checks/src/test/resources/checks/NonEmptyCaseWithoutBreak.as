function foo() {
  switch (param) {
    case 0: // OK
    case 1: // OK
      break;
    case 2: // OK
      return;
    case 3: // OK
      throw new Exception();
    case 4: // OK
    {
      doSomething();
      break;
    }
    case 5: // OK
      if (param) {
        return 42;
      } else {
        return 0;
      }
    default: // OK
      break;
  }

  switch (param) {
    case 0:
    case 1: // NOK
      doSomething();
    case 2: // NOK
      if (true) {
        break;
      }
    case 3: // NOK
      break;
      doSomething();
    default: // OK
      doSomethingElse();
  }

  while (cond) {
    switch (param) {
      case 0:    // OK
        continue;
      default:
        doSmth();
    }
  }

}
