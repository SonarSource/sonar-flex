function f() {
  if (unexpectedCondition) {
    Alert.show("Unexpected Condition");  // Noncompliant {{Remove this usage of Alert.show().}}
  }
  return Alert.NO;                       // OK
}

trace("");                               // Noncompliant {{Remove this use of the "trace" function.}}

obj.trace("");                           // OK
new trace();                             // OK
var a = function trace() {return 1;};    // OK
