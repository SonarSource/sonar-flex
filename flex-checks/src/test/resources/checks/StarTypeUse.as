var obj:Something;                   // OK
var foo:Something = new Something(); // OK

var obj:*;                   // NOK
var foo:* = new Something(); // NOK