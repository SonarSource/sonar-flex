trace("");                              // Noncompliant {{Remove this use of the "trace" function.}}

obj.trace("");                         // OK
new trace();                            // OK
var a = function trace() {return 1;};   // OK
