function myFuncion():boolean {         // Noncompliant {{Reduce the number of returns of this function 4, down to the maximum allowed 2.}}
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

function myFunction():boolean {        // Noncompliant
  if (condition1) {
    return true;
  } else if (condition2) {
      return false;
  }
  return false;
}

function myFunction():boolean {        // Noncompliant

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
