class Ok {
  function ko() // +1
  {
    switch (foo) // +1
    {
      case 1: // +1
      case 2: // +1
      default: // +1
      ;
    }
  }
}

class Ok {
  function ok() {
  }
}
