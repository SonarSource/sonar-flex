public static class A {  // Noncompliant {{Add the missing ASDoc for this class.}}

  /**
   * ASDoc
   */
  public var field1:String;   // OK
  public var fields2:String;  // Noncompliant {{Add the missing ASDoc for this field declaration.}}

  /**
   * @param param1 my doc
   */
  public function method1(param1:String):Boolean {}  // Noncompliant {{Add the missing "@return" ASDoc for the return value of this method.}}

  /**
   * @param
   */
  public function method2(param1:String):void {}     // Noncompliant {{Add the missing "@param" ASDoc for: param1.}}

  public function method3(param1:String) {}          // Noncompliant {{Add the missing ASDoc for this method.}}

  /**
   * my doc
   * @param param1 my doc
   * @return my doc
   */
  public function myMethod(param1:String):Boolean {} // OK
}


class B {                      // Noncompliant {{Add the missing ASDoc for this class.}}

  // comment
  function f1(p1, p2) {}       // Noncompliant {{Add the missing ASDoc for this method.}}

  /**
   * ASDoc
   */
  function f2(p1, p2) {}       // Noncompliant {{Add the missing "@param" ASDoc for: p1, p2.}}

  /*
   * Not ASDoc
   */
  function f3() {              // Noncompliant {{Add the missing ASDoc for this method.}}

  }

  /**
   * ASDoc
   */
  function f4() {              // OK
  }

}


/**
 * ASDoc
 */
class C {     // OK

    internal function f1() {}   // OK
    private function f2() {}    // OK
    protected function f3() {}  // OK

}

/**
 * @private
 */
class C {     // OK

   public function f1() {}   // OK - class has private tag

}

/**
 * ASDoc
 */
class C {     // OK

    /**
     * @private
     */
    public var field1:String;           // OK - has private tag

    /**
     * @private
     */
    public function f1(p1):Boolean {}   // OK - has private tag

}

/**
 * ASDoc
 */
class C {     // OK

    /**
     * ASDoc
     */
    [Bindable]
    public var field1;

    [Bindable]
    /**
     * ASDoc
     */
    public var field1;

    /**
     * @inheritDoc
     */
    public function f1(p1):Boolean {}   // OK - class has inherit tag

    /**
     * @param p1:int doc
     * @param p2 doc
     */
    public function f1(p1) {}           // OK

    /**
     * @param P1:int
     */
    public function f1(p1) {}           // OK

    /**
     * @param P1:int
     */
    [Bindable]
    public function f1(p1) {}           // OK
}
