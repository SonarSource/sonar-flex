function f() {         // Noncompliant {{This function has 6 lines, which is greater than the 3 lines authorized. Split it into smaller functions.}}
    function f() {     // Noncompliant {{This function has 4 lines, which is greater than the 3 lines authorized. Split it into smaller functions.}}
        // comment
        return 1;
    }
}

var f = function () {  // Noncompliant
    // comment
    return 1;
}

function f() {         // Noncompliant
    // comment
    return 1;
    function f() {     // OK
    }
}

var f = function () {  // OK
    return 1;
}

function f() {         // OK
    return 1;
}
