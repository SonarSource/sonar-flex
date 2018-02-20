switch(i) {
  case 1:
    //...
    break;
  case 2:
    break;
  case 1:  // Noncompliant {{This case duplicates the case on line 2 with condition "1".}}
    break;
  default:
}

switch(i) {
  case 1:
  case 2:
    break;
  case 1:  // Noncompliant {{This case duplicates the case on line 13 with condition "1".}}
    break;
}

switch(i) {
  case 1:
  case 2:
    break;
  case 3:
    break;
}
