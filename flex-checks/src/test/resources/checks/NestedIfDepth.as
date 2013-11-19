function sayHello() {
  if (true) { // level 1
    if (true) { // level 2
      if (true) { // level 3
        if (true) { // level 4
        }
      }
    }
  }
}

function sayHello() {
  if (true) {                     // level 1

    for (var i = 0; i < 7; i++) { // level 2

      if (true) {                 // level 3

        switch (i) {              // level 4
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
