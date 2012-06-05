function foo() {

  switch (c) { // OK
    case '0':
      break;
    default:
      break;
  }

  switch (c) { // NOK
    case '0':
      break;
  }

}
