function f() {
  if (unexpectedCondition) {
    Alert.show("Unexpected Condition");  // Noncompliant
  }
  return Alert.NO;                       // OK
}

