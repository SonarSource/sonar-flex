function sayHello() {
  var foo = "foo";var bar = "bar"; // Noncompliant {{At most one statement is allowed per line, but 2 statements were found on this line.}}

  alert("Hello World!"); alert("Hello World!"); // Noncompliant

  if (a) {} // OK

  if (a) {} if (b) {} // Noncompliant

  while (condition); // OK

  label: while (condition) { // OK
    break label; // OK
  }
}
