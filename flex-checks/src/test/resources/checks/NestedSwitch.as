switch (x) {         // OK

    case 1:
       switch (y) {  // Noncompliant {{Move this "switch" to a function or refactor the code to eliminate it.}}
           case 1:
           default:
       }

    case 2:
}

switch(z) {          // OK
    case 1:
    default:
}
