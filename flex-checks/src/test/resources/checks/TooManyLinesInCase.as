switch (myVariable) {
  case 0:                     // OK
    trace("");
    trace("");
    trace("");
    break;
  case 1:                     // OK, vertical spaces do not count




    break;
  case 2:                     // OK, comments do not count
    // comment
    // comment
    // comment
    // comment
    break;
  case 3:
  default:                    // Noncompliant {{Reduce this switch case number of lines of code from 6 to at most 5, for example by extracting code into methods.}}
    trace("");
    trace("");
    trace("");
    trace("");
    break;
}
