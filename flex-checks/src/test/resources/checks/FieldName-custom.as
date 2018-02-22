class A {
  public const BAR;
  public var var1, var2 = 1;  // Noncompliant 2
  public var VAR:int = 1;     // Noncompliant
}

// Rule should only check class field
var FOO;
