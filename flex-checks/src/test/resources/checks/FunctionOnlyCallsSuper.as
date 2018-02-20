class A {
  private function f():void;

  private override function f():void { // OK
  }

  private override function f():void { // OK
    return 0;
  }

  private override function f():void { // Noncompliant {{Remove this method "f" to simply inherit it.}}
    super.f();
  }

  private override function f():void { // Noncompliant
    return super.f();
  }

  private override function f():void { // OK
    super.f(0);
  }

  private override function f(a:int):void { // OK
    super.f();
  }

  private override function f(a:int):int { // Noncompliant
    super.f(a);
  }

  private override function f(a:int):int { // Noncompliant
    return super.f(a);
  }

  private override function f(a:int):int { // OK
    super.f(a);
    return a;
  }

  private override function f(a:int, b:int):void {
    super.f(b, a);
  }

  private override function f(... a:int):void { // Noncompliant
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
