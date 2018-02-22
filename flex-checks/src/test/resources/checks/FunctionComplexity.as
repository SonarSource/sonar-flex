function ko() // Noncompliant {{Function has a complexity of 3 which is greater than 1 authorized.}} [[effortToFix=2]]
{
  switch (foo)
  {
    case 1: // +1
    case 2: // +1
    default:
    ;
  }
}

function ok() {
}
