class A {
  private function f():void;

  private override function f():void { // OK
  }

  private override function f():void { // OK
    return 0;
  }

  private override function f():void { // NOK
    super.f();
  }

  private override function f():void { // NOK
    return super.f();
  }

  private override function f():void { // OK
    super.f(0);
  }

  private override function f(a:int):void { // OK
    super.f();
  }

  private override function f(a:int):int { // NOK
    super.f(a);
  }

  private override function f(a:int):int { // NOK
    return super.f(a);
  }

  private override function f(a:int):int { // OK
    super.f(a);
    return a;
  }

  private override function f(a:int, b:int):void { // NOK
    super.f(b, a);
  }

  private override function f(... a:int):void { // NOK
    super.f(a);
  }

  [Metadata]
  private override function f(... a:int):void { // OK
    super.f(a);
  }

  private override function f():void { // OK
    foo();
  }

  private override function f():void { // OK
    return;
  }

  public function A() { // OK
    super();
  }

}
