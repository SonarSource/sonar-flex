function myMethod():boolean { // NOK
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

function myMethod():boolean { // OK
  if (condition1) {
    return true;
  } else if (condition2) {
      return false;
  }
  return false;
}