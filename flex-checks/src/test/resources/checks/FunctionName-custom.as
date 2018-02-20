function DoSomething(){
}

function doSomething(){      // Noncompliant {{Rename this "doSomething" function to match the regular expression ^[A-Z][a-zA-Z0-9]*$}}
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
