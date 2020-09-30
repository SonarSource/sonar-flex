function f() {
  if (unexpectedCondition) {
    Alert.show("Unexpected Condition");  // Noncompliant {{Make sure this debug feature "Alert.show()" is deactivated before delivering the code in production.}}
  }
  return Alert.NO;                       // OK
}

trace("");                               // Noncompliant {{Make sure this debug feature "trace()" is deactivated before delivering the code in production.}}

obj.trace("");                           // OK
new trace();                             // OK
var a = function trace() {return 1;};    // OK
