function doSomething():void {
  ;                                            // Noncompliant {{Remove this empty statement.}}
}

function doSomethingElse():void {
  trace("Hello, world!");;                    // Noncompliant
  for (var int:i = 0; i < 3; trace(i), i++);  // Noncompliant
}

interface Greetings {
  function hello();                           // OK
}
