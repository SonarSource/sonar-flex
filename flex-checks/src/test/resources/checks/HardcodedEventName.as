class A {

  public function foo() {
    addEventListener("CustomEvent", clickHandler);    // NOK
  }
}


class B {

  public const CUSTOM_EVENT:String = "CustomEvent";

  public function foo() {
    addEventListener(CUSTOM_EVENT, clickHandler);     // OK
  }
}


class C {

  public function foo() {
    a.f();
    doSomething("Just do it");
  }
}
