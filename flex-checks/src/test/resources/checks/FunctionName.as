function DoSomething(){      // Noncompliant {{Rename this "DoSomething" function to match the regular expression ^[a-z][a-zA-Z0-9]*$}}
}

function doSomething(){      // OK
}


class Foo {
    function Foo() {         // OK
    }

    class Nested {
        function Nested() {  // OK
        }
    }
    function _foo() {        // Noncompliant

    }
}
