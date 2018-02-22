public static class A {  // Noncompliant {{Add the missing ASDoc for this class.}}

  public var fields2:String;

  public function method3(param1:String) {}

}


class B {                      // Noncompliant {{Add the missing ASDoc for this class.}}

  // comment
  function f1(p1, p2) {}

}


/**
 * ASDoc
 */
class C {     // OK

    internal function f1() {}
    private function f2() {}
    protected function f3() {}

}

/**
 * @private
 */
class C {     // OK

   public function f1() {}

}

