function func1() { // OK
  return;
}

function func2() { // OK
}

function func3(); // OK

function func4() { // NOK
  if (false) {
    return;
  }
}

function func5(a) { // NOK
  if (a > 0) {
    return 0;
  }
  return -1;
}
