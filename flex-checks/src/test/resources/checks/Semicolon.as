[Test]
function f() {
  return                   // Noncompliant {{Add a semicolon at the end of this statement}}
  5                        // Noncompliant
}

if (condition) {           // OK
}

for (i = 0; i < 10; i++) { // OK
}

label: while (condition) { // OK
  break label; // OK
}

var f = function(x:int) {
  return x%2;
} // Noncompliant

function sayHello() {
  var a = {
    'i': 1,
    'j': 2
  } // Noncompliant
}

{ doSomething() } // Noncompliant
