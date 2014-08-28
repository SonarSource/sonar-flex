class A extends B {
    function f() {}          // NOK

    override function g() {} // OK

    override function h() {  // OK
        // NOP
    }

    function i() {           // OK
        trace("");
    }
}
