function foo() {

  switch (param) {
    case 0:
      break;
    case 1:
    default: // OK
      break;
  }

  switch (param) { // Noncompliant {{Avoid switch statement without a "default" clause.}}
    case 1:
      break;
  }

  switch (param) {
    case 0:
      break;
    default:
      break;
    case 1:
      break;
  }

}
