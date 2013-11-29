public class A
{
  public function dispatchEvent() {
    dispatchEvent("Event");                     // OK
  }

  public class Inner {
    public function Inner() {
      dispatchEvent(new Event("Event")); // NOK
    }
  }

  public function A() {
    dispatchEvent(new Event("Event"));   // NOK
  }
}

public class B
{
  public function B() {
    dispatchEvent(new Event("Event"));   // NOK
  }
}

public class C
{
  public function C() {
    doSomething();
  }
}

dispatchEvent(new Event("uselessEvent"));       // OK
