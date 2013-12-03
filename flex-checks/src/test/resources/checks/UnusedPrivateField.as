public class A {
  private var foo;                      // NOK
  public var bar;                       // OK

  public function compute(a:int):int{
    return a * 4;
  }

  private class Inner {
      private var foo;                  // OK
      private var bar;                  // NOK

    private function get Foo() {
      return this.foo;
    }

  }
}
