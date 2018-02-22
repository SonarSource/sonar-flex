public class A {
  public const foo:Foo = new Foo();         // Noncompliant {{Make this const field "foo" static too}}
}

public class B {
  public static const FOO:Foo = new Foo(); // OK
  private static var foo:Foo = new Foo();  // OK
  public static var foo:Foo = new Foo();   // OK
  const foo;                               // OK
}
