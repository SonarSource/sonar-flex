function DoSomething(){      // NOK
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
    function _foo() {        // NOK

    }
}
