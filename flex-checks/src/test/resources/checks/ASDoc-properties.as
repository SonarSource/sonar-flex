public static class A {

  public var fields2:String; // Noncompliant {{Add the missing ASDoc for this field declaration.}}

/**
 * ASDoc
 */
  public var fields3:String;

  public function method3(param1:String) {}

}
