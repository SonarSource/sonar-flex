switch (myVariable) {
  case 0:                     // OK
    trace("");
    trace("");
    trace("");
    break;
  case 1:
  default:                    // NOK
    trace("");
    trace("");
    trace("");
    trace("");
    break;
}