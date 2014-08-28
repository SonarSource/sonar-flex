switch (x) {         // OK

    case 1:
       switch (y) {  // NOK
           case 1:
           default:
       }

    case 2:
}

switch(z) {          // OK
    case 1:
    default:
}