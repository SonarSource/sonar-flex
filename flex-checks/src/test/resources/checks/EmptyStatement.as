function doSomething():void {
  ;                                            // NOK
}

function doSomethingElse():void {
  trace("Hello, world!");;                    // NOK
  for (var int:i = 0; i < 3; trace(i), i++);  // NOK
}

interface Greetings {
  function hello();                           // OK
}