switch (myVariable) {
  case 0:                     // Noncompliant
    trace("");
    trace("");
    trace("");
    break;
  case 1:
  default:                    // Noncompliant {{Reduce this switch case number of lines of code from 6 to at most 4, for example by extracting code into methods.}}
    trace("");
    trace("");
    trace("");
    trace("");
    break;
}
