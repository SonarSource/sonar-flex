function sayHello() {
  if (true) { // level 1
    if (true) { // Noncompliant
      if (true) { // level 3
        if (true) {
        }
      }
    }
  }
}

function sayHello() {
  if (true) {                     // level 1

    for (var i = 0; i < 7; i++) { // Noncompliant

      if (true) {                 // level 3

        switch (i) {
          case 1:
            break;
          case 2:
            break;
        }
      }
    }
  }
}

if (true) { // level 1
  if (true) { // Noncompliant
  }
} else if (true) { // level 1
  if (true) { // Noncompliant
  }
}
