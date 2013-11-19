myLabel:if (a == b) {               // NOK
  if (b == c) {
    trace(c);
    break myLabel;
  }
  trace(a);
}

myLabel:for (i = 0; i < 10; i++) {   // OK
  trace("Loop");
  break myLabel;
}

myLabel:while (a != null) {          // OK
  trace("Loop");
  break myLabel;
}

myLabel:do {                        // OK
  trace("Loop");
  break myLabel;
} while (a != null);
