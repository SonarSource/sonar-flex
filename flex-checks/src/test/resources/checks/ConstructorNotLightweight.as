public class Foo {

  var a:boolean;

  public function Foo() {                     // NOK
    for (var i = 0; i < 7; i++) {
      if (i < 2 && a) {
        doSomething();
      }
    }
  }
}


public class Bar {

  public function Bar(name:String, age:Age) { // OK
    _name = name;
    _age = age;
  }
}

public class FooBar {
}
