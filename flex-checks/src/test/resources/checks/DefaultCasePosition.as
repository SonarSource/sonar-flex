function foo() {

  switch (param) {
    case 0:
      break;
    case 1:
    default: // OK
      break;
  }

  switch (param) {
    default: // OK
      break;
    case 0:
      break;
    case 1:
      break;
  }

  switch (param) {
    case 1:
      break;
  }

  switch (param) {
    case 0:
      break;
    default: // Noncompliant {{Move this "default" clause to the beginning or end of this "switch" statement.}}
      break;
    case 1:
      break;
  }

  switch (param) {
    case 0:
      break;
    case 2:
    default: // Noncompliant {{Move this "default" clause to the beginning or end of this "switch" statement.}}
    case 3:
      break;
    case 1:
      break;
  }

}
