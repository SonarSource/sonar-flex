[Test]
function f() {
  return                   // NOK
  5                        // NOK
}

if (condition) {           // OK
}

for (i = 0; i < 10; i++) { // OK
}

label: while (condition) { // OK
  break label; // OK
}

var f = function(x:int) {  // NOK
  return x%2;
}

function sayHello() {
  var a = {                // NOK
    'i': 1,
    'j': 2
  }
}

{ doSomething() }
