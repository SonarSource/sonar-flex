class A {

  public function foo() {
    addEventListener("Event", handler);       // NOK
    this.addEventListener("Event", handler);  // NOK
  }
}


class B {

  public const EVENT:String = "Event";

  public function foo() {
    addEventListener(EVENT, handler);       // OK
    this.addEventListener(EVENT, handler);       // OK
  }
}

public function foo() {
  a.f();
  doSomething("Just do it");
}

