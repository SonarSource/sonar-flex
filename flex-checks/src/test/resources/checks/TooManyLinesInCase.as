switch (myVariable) {
  case 0:                     // OK
    trace("");
    trace("");
    trace("");
    break;
  case 1:
  default:                    // Noncompliant {{Reduce this switch case number of lines from 6 to at most 5, for example by extracting code into methods.}}
    trace("");
    trace("");
    trace("");
    trace("");
    break;
}
