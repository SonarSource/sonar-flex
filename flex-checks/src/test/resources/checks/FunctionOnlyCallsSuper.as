class A {
  private function f():void;

  private override function f():void { // Compliant
  }

  private override function f():void { // Compliant
    return 0;
  }

  private override function f():void { // Non-Compliant
    super.f();
  }

  private override function f():void { // Non-Compliant
    return super.f();
  }

  private override function f():void { // Compliant
    super.f(0);
  }

  private override function f(a:int):void { // Compliant
    super.f();
  }

  private override function f(a:int):int { // Non-Compliant
    super.f(a);
  }

  private override function f(a:int):int { // Non-Compliant
    return super.f(a);
  }

  private override function f(a:int):int { // Compliant
    super.f(a);
    return a;
  }

  private override function f(a:int, b:int):void { // Compliant
    super.f(b, a);
  }

  private override function f(... a:int):void { // Non-Compliant
    super.f(a);
  }

  private override function f():void { // Compliant
    foo();
  }

  private override function f():void { // Compliant
    return;
  }

  public function A() { // Compliant
    super();
  }

}
