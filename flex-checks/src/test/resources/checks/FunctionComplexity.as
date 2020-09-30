function ko() // Noncompliant {{Function has a complexity of 3 which is greater than 0 authorized.}} [[effortToFix=3]]
{
  switch (foo)
  {
    case 1: // +1
    case 2: // +1
    default:
    ;
  }
}

function empty() {  // Noncompliant {{Function has a complexity of 1 which is greater than 0 authorized.}} [[effortToFix=1]]
}

function withReturn() {   // Noncompliant {{Function has a complexity of 1 which is greater than 0 authorized.}} [[effortToFix=1]]
  return 1;
}

function withTernary(i:int) {  // Noncompliant {{Function has a complexity of 2 which is greater than 0 authorized.}} [[effortToFix=2]]
  var str = (i > 0) ? "true" : "false";
}

function nestedFunctionOK() {  // Noncompliant {{Function has a complexity of 1 which is greater than 0 authorized.}} [[effortToFix=1]]
  function ok() {  // Noncompliant {{Function has a complexity of 1 which is greater than 0 authorized.}} [[effortToFix=1]]
  }
}

function nestedFunctionKO() {  // Noncompliant {{Function has a complexity of 1 which is greater than 0 authorized.}} [[effortToFix=1]]
  function ko(i:int) {  // Noncompliant {{Function has a complexity of 2 which is greater than 0 authorized.}} [[effortToFix=2]]
    if (i > 0) {
      return;
    }
  }
}
