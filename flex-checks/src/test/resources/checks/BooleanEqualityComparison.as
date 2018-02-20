if (booleanVariable == true) {  // Noncompliant
}
if (booleanVariable != true) {  // Noncompliant
}

if (booleanVariable != x) {     // OK
}

if (booleanVariable) {          // OK
}
if (!booleanVariable) {         // OK
}
if (super.someMethod()) {       // OK
}
