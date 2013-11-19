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

public class Bla
{
  public function bla() : void {
  }

  public function Bla() : void {  // nOK
  }
}
