class Ok { // Noncompliant {{Class has a complexity of 3 which is greater than 1 authorized.}} [[effortToFix=2]]
  function ko() // +1
  {
    switch (foo)
    {
      case 1: // +1
      case 2: // +1
      default:
      ;
    }
  }
}

class Ok {
  function ok() {
  }
}
