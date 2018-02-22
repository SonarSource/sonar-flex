public class Foo {

  var a:boolean;

  public function Foo() { // Noncompliant {{Extract the content of this "Foo" constructor into a dedicated function}}
    for (var i = 0; i < 7; i++) {
      if (i < 2 && a) {
        doSomething();
      }
    }
  }
}


public class Bar {
  private var _name;
  private var _age;

  public function Bar(name, age) { // OK
    _name = name;
    _age = age;
  }

  public function get name() {}
  public function get age() {}
}


public class FooBar {
  public function FooBar();
}

public class FooBar {
}
