function sayHello() {
  var foo = "foo";var bar = "bar"; // NOK

  alert("Hello World!"); alert("Hello World!"); // NOK

  if (a) {} // OK

  if (a) {} if (b) {} // NOK

  while (condition); // OK

  label: while (condition) { // OK
    break label; // OK
  }
}
