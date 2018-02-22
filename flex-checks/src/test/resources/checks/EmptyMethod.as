class A extends B {
    function f() {}          // Noncompliant {{Add a nested comment explaining why this method is empty, throw an NotSupportedException or complete the implementation.}}

    override function g() {} // Noncompliant

    override function h() {  // OK
        // NOP
    }

    function i() {           // OK
        trace("");
    }
}
