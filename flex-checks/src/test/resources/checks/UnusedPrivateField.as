public class A {
  private var foo;                      // Noncompliant {{Remove this unused 'foo' private field}}
  public var bar;                       // OK

  public function compute(a:int):int{
    return a * 4;
  }

  private class Inner {
      private var foo;                  // OK
      private var bar;                  // Noncompliant

    private function get Foo() {
      return this.foo;
    }

  }
}
