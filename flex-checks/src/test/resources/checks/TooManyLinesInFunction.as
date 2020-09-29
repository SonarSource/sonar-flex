function f() {         // Noncompliant {{This function has 6 lines of code, which is greater than the 3 lines authorized. Split it into smaller functions.}}
    function f() {     // Noncompliant {{This function has 4 lines of code, which is greater than the 3 lines authorized. Split it into smaller functions.}}
        // comment
        var i = 1;
        return i;
    }
}

var f = function () {  // OK
    // vertical spaces do not count


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
