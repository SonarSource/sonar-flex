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

class B {

  function f1(p1) {                  // OK
  }

  function f2(p1) {                  // OK
      throw new IllegalOperationError("f2() is abstract");
  }

  function f3(p1) {                  // OK
    // Abstract
  }

}

class C implements I {

  function f1(p1) {                  // OK
  }

}



function cHandler(e:String)      { return; } // NOK
function cHandler(e:*)           { return; } // NOK
function something(e:MouseEvent) { return; } // NOK

function aHandler(e:MouseEvent)  { return; } // OK
function bHandler(e:x.ClickEvent){ return; } // OK
function dHandler()              { return; } // OK
function handleSomething(e:MouseEvent){ return; } // OK
function onSomething(e:MouseEvent){ return; } // OK
function onSomething()              { return; } // OK

function onlySomething(e:MouseEvent) { return; } // NOK
function on(e:MouseEvent) { return; } // OK
