public static class A {

  public var fields2:String;

  public var fields3:String;

  public function method2(param1:String) {} // Noncompliant {{Add the missing ASDoc for this method.}}

/**
 * ASDoc
 */
  public function method3(param1:String) {}

}
