class Ok {
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
