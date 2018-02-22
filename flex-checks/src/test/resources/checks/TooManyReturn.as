function myFuncion():boolean {         // Noncompliant
  if (condition1) {
    return true;
  } else {
    if (condition2) {
      return false;
    } else {
      return true;
    }
  }
  return false;
}

function myFunction():boolean {        // OK
  if (condition1) {
    return true;
  } else if (condition2) {
      return false;
  }
  return false;
}

function myFunction():boolean {        // OK

  function nestedFunction():boolean {
    return true;                      // Should not count return of nested function in enclosing function return counter
  }

  if (condition1) {
    return true;
  } else if (condition2) {
    return false;
  }
  return false;
}
