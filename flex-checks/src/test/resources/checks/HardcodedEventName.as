class A {

  public function foo() {
    addEventListener("Event", handler);       // Noncompliant {{The event name "Event" should be defined in a constant variable.}}
    this.addEventListener("Event", handler);  // Noncompliant
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

