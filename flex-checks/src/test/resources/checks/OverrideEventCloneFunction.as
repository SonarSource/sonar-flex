public class MyEvent extends Event {         // NOK

  public function clone():void {}
  public function doSomething():boolean {}

}


public class MyEvent extends Event {         // OK

  override public function clone():Event {
    return new MyEvent();
  }
}

public class MyEvent extends MyCustomEvent { // OK

  override public function clone():Event {
    return new MyEvent();
  }
}

class A {}                                  // OK
