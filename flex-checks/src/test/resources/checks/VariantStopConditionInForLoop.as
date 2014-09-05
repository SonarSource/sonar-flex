var a;
for (a = 0; a < 42; a++) {
    a = 0;                                // NOK
}

for (var d = 0, e = 0; d < 42; d++) {
    d = 0;                                // NOK
    e = 0;                                // NOK
}

g;
for (var f = 0; f < 42; f++) {
    f = 0;                                // NOK
    g = 0;                                // OK
    for (var g = 0; g < 42; g++) {
        g = 0;                            // NOK
        f = 0;                            // NOK
    }
    f = 0;                                // NOK
    g = 0;                                // OK
}

g = 0;                                    // OK

for (var h = 0; h < 42; h++) {
    h =                                   // NOK
            h =                           // NOK
                    0;
}

g++;                                      // OK
++g;                                      // OK
g = 0;                                    // OK
doSomething(i);                           // OK

for (var i = 0; 0 < 42; i++) {
    i++;                                  // NOK
    ++i;                                  // NOK
    --i;                                  // NOK
    i--;                                  // NOK
}

for (var j = 0; j < 42; j++) {            // OK
    for (var k = 0; j++ < 42; k++) {      // NOK
    }
}

for (var i = 0; i < 42; i++) {
    typeof i;                             // OK
}

for (var i = 0; i < 10; i++) {
    for (var k = 0; k < 20; i++) {        // NOK
        trace("Hello");
        doSomething(i = 0);               // NOK
    }
}

for (var i = 0; list.length > 1; i++) {   // NOK
}

for (var i = 0; doSomething() > 1; i++) { // NOK
}

i = 0;
for (; i > 0; i++) {
    i = 1;                                // OK
}

for (++i; i > 0; i++) {
    i = 1;                                // NOK
}

for (myArray in i) {
    i = 0;                                // OK - not supported
}
