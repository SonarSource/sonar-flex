function func1() { // OK
  return;
}

function func2() { // OK
}

function func3(); // OK

function func4() { // Noncompliant {{A function shall have a single point of exit at the end of the function.}}
  if (false) {
    return;
  }
}

function func5(a) { // Noncompliant
  if (a > 0) {
    return 0;
  }
  return -1;
}
