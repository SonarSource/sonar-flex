function f() {
  var a;              // Noncompliant {{Remove this unused 'a' local variable.}}
  return;
}

function f() {
  var a;              // OK

  function inner() {
    doSomething(b);
    var c;            // Noncompliant
  }

  doSomething(a);
}
