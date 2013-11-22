public class A {
  public const foo:Foo = new Foo();         // NOK
}

public class B {
  public static const FOO:Foo = new Foo(); // OK
  private static var foo:Foo = new Foo();  // OK
  public static var foo:Foo = new Foo();   // OK
  const foo;                               // OK
}
