function f() {
  var a;              // NOK
  return;
}

function f() {
  var a;              // OK

  function inner() {
    doSomething(b);
    var c;            // NOK
  }

  doSomething(a);
}
