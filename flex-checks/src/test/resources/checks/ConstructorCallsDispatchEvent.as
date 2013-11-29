public class MyClass
{
  public function MyClass() {
    dispatchEvent(new Event("uselessEvent"));   // NOK
  }

  public function dispatchEvent() {
    dispatchEvent("Event");                     // OK
  }

  public class InnerFirst {
    public function InnerFirst() {
      dispatchEvent(new Event("uselessEvent")); // NOK
    }
  }
}

public class MyClass
{
  public function MyClass() {
    dispatchEvent(new Event("uselessEvent"));   // NOK
  }
}

public class MyClass
{
  public function MyClass() {
    doSomething();
  }
}

dispatchEvent(new Event("uselessEvent"));       // OK
