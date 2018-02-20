public class Foo
{
  public function Foo() : void {  // Noncompliant {{Remove the "void" return type from this "Foo" constructor}}
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

  public function Foo() : void {  // Noncompliant
  }
}

public class Foo {
}


public class Foo {
  private var _field;
}
