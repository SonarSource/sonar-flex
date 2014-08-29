function f1(a:int, b:int) {          // NOK - b
  return a;
}

function f1(a:int, b:int, c:int) {   // NOK - b
  return a;
}

var myC = new C();

function f2(a:int, b:int) {          // NOK - b
  myC.b += b();
  return a;
}

function f3(a:int, b:int) {          // NOK - b
  var a = function (c:int) {         // NOK - c
    a = 1;
  }
}

function f4(a:int) {                 // OK
  return a.b();
}

function f4(a:int, b:int) {          // OK
  return a + b;
}

interface I {
 function f(a:int);                  // OK
}

class A extends B {
  override function f(a:int) {       // OK
  }
}
