function ko() // Noncompliant {{Function has a complexity of 4 which is greater than 2 authorized.}} [[effortToFix=2]]
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

function withReturn() {   // OK
  return 1;
}

function withTernary(i:int) { // Noncompliant {{Function has a complexity of 3 which is greater than 2 authorized.}} [[effortToFix=1]]
  var str = (i > 0) ? "true" : "false";
}

function nestedFunctionOK() {
  function ok() {
  }
}

function nestedFunctionKO() {
  function ko(i:int) { // Noncompliant {{Function has a complexity of 3 which is greater than 2 authorized.}} [[effortToFix=1]]
    if (i > 0) {
      return;
    }
  }
}