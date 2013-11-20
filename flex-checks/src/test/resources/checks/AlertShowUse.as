function f() {
  if (unexpectedCondition) {
    Alert.show("Unexpected Condition");  // NOK
  }
  return Alert.NO;                       // OK
}

