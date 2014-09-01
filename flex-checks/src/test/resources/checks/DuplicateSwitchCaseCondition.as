switch(i) {
  case 1:
    //...
    break;
  case 2:
    break;
  case 1:  // NOK
    break;
  default:
}

switch(i) {
  case 1:
  case 2:
    break;
  case 1:  // NOK
    break;
}

switch(i) {
  case 1:
  case 2:
    break;
  case 3:
    break;
}
