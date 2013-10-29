switch (variable) { // NOK
  case 1:
    doSomething();
    break;
  default:
    doSomethingElse();
    break;
}

switch (variable) { // OK
  case 1:
    doSomething();
    break;
  case 2:
    doSomething();
    break;
  case 3:
    doSomething();
    break;
  default:
    doSomethingElse();
    break;
}