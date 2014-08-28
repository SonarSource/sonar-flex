trace("");                              // NOK

obj.trace("");                         // OK
new trace();                            // OK
var a = function trace() {return 1;};   // OK