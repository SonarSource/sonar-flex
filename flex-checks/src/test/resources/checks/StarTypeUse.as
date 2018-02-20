var obj;                   // OK
var obj:Something;                   // OK
var foo:Something = new Something(); // OK

var obj:*;                   // Noncompliant {{Remove usage of this "star" type}}
var foo:* = new Something(); // Noncompliant
