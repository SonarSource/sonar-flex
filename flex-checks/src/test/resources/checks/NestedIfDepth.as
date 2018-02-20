function sayHello() {
  if (true) { // level 1
    if (true) { // level 2
      if (true) { // level 3
        if (true) { // Noncompliant {{Refactor this code to not nest more than 3 if/for/while/switch statements.}}
        }
      }
    }
  }
}

function sayHello() {
  if (true) {                     // level 1

    for (var i = 0; i < 7; i++) { // level 2

      if (true) {                 // level 3

        switch (i) {              // Noncompliant
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
  if (true) { // level 2
  }
} else if (true) { // level 1
  if (true) { // level 2
  }
}
