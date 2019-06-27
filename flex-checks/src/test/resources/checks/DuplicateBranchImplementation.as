switch(i) {
  case 1:
    doSomething(1);
    break;
  case 2:
    doSomething(2);
    break;
  case 3:                 // Compliant by exception : oneliner
    doSomething(2);
    break;
  case 4:
  case 5:
    break;
  default:
}

switch(i) {
  case 1:
    doSomething(1);
    break;
  case 2:
    doSomething(2);
    break;
  case 3:                 // Compliant by exception : oneliner
    doSomething(2);
    break;
  case 4:
  case 5:
    break;
}

switch(i) {
  case 1:
    doSomething(1);       // Compliant by exception : oneliner
    break;
  case 2:
    doSomething(1);       // Compliant by exception : oneliner
    break;
  case 3:
    doSomething(1);       // Compliant by exception : oneliner
    break;
  case 4:
  case 5:
    break;
}

switch(i) { // in this example, non compliant because oneliner but all branches are not there and but all identical
  case 1:
    doSomething(1);
    break;
  case 2:                 // Noncompliant {{Either merge this case with the identical one on line "48" or change one of the implementations.}}
    doSomething(1);
    break;
  case 3:                 // Noncompliant {{Either merge this case with the identical one on line "48" or change one of the implementations.}}
    doSomething(1);
    break;
  case 4:                 // Noncompliant {{Either merge this case with the identical one on line "48" or change one of the implementations.}}
    doSomething(1);
    break;
  case 5:                 // Noncompliant {{Either merge this case with the identical one on line "48" or change one of the implementations.}}
    doSomething(1);
    break;
}


switch(i) {
  case 1:
    doSomething(1);
    doAnotherThing(1);
    break;
  case 2:
    doSomething(2);
    doAnotherThing(2);
    break;
  case 3:                 // Noncompliant {{Either merge this case with the identical one on line "71" or change one of the implementations.}}
    doSomething(2);
    doAnotherThing(2);
    break;
  case 4:
  case 5:
    break;
  default:
}

switch(i) {
    case 1:
        doSomething(1);
        break;
    case 2:
    case 3:
        break;
    default:              // Compliant by exception : oneliner, all branches are there but not identical
        doSomething(1);
        break;
}

switch(i) {
    case 1:
        doSomething(1);
        break;
    case 2:
    case 3:
        break;
    case 1:               // Compliant by exception : oneliner
        doSomething(1);
        break;
}

switch(i) {
    case 1:               // OK
    case 2:
    case 3:
      doSomething(1);
      break;
}

if(i==1) {
    doSomething(1);
    doAnotherThing(1);
} else if(i==2) {         // Noncompliant {{Either merge this case with the identical one on line "117" or change one of the implementations.}}
    doSomething(1);
    doAnotherThing(1);
} else {
    doSomething(2);
}

if(i==1) {
    doSomething(1);
} else if(i==2) {         // Compliant by exception : oneliner
    doSomething(1);
} else {
    doSomething(2);
}

if(i==1) { // in this example, non compliant because oneliner but all branches are not there and but all identical
    doSomething(1);
} else if(i==2) {         // Noncompliant {{Either merge this case with the identical one on line "135" or change one of the implementations.}}
    doSomething(1);
}

if(i==1) {
    doSomething(1);
} else if(i==2) {         // Compliant by exception : oneliner
    doSomething(1);
}
else {                    // Compliant by exception : oneliner
    doSomething(1);
}

// no crash in empty block 
if(i==1) {
} else if(i==2) {         // Compliant by exception : oneliner
    doSomething(1);
}
else {                    // Compliant by exception : oneliner
    doSomething(1);
}

// all empty blocks exception of the exception
if(i==1) {
} else if(i==2) {         // Noncompliant {{Either merge this case with the identical one on line "160" or change one of the implementations.}}
}

// all empty blocks in exception
if(i==1) {
} else if(i==2) {         // Compliant by exception : oneliner
}
else {                    // Compliant by exception : oneliner
}
