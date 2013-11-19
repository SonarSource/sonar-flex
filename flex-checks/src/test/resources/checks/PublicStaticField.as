public class Greeter {
  public static var foo:Foo = new Foo();   // NOK
}

public class Greeter {
  public static const FOO:Foo = new Foo(); // OK

  private static var foo:Foo = new Foo();  // OK
  public var foo;                          // OK
  var foo;                                 // OK
}
