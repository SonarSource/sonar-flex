class A {
  function f() {
  if (false) { // Compliant
  }

  if (false) { // Compliant
  } else {
  }

  if (false) { // Compliant
    if (false) { // Noncompliant
    }
  }

  if (false) { // Compliant
    if (false) { // Compliant
    }
    doSomething();
  }

  if (false) { // Compliant
    var a:int;
    if (a) { // Compliant
    }
  }

  if (false) { // Compliant
    if (false) { // Compliant
    }
  } else {
  }

  if (false) { // Compliant
    if (false) { // Compliant
    } else {
    }
  }

  if (false) { // Compliant
  } else if (false) { // Compliant
    if (false) { // Noncompliant
    }
  }

  if (false) // Compliant
    if (true) { // Noncompliant
    }

  if (false) { // Compliant
    while (true) {
      if (true) { // Compliant
      }
    }

    while (true)
      if(true) { // Compliant
      }
  }
}

    if (false) { // Compliant
    }

    if (false) { // Compliant
      doSomething();
    }

}
