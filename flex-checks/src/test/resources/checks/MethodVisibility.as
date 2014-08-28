class A {
}

class B {
    function f1() {}            // NOK

    public function f2() {}     // OK

    internal function f3() {}   // OK

    protected function f4() {}  // OK

    private function f5() {}    // OK
}