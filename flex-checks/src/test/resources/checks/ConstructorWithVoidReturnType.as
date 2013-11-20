public class Foo
{
  public function Foo() : void {  // NOK
  }

  public function f() : void {
  }
}

public class Foo
{
  public function Foo() {         // OK
  }
}

public class Foo
{
  public function foo() : void {
  }

  public function Foo() : void {  // NOK
  }
}

public class Foo {
}


public class Foo {
  private var _field;
}
