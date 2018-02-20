class Foo {}                       // OK

public class Foo {}                // OK

public dynamic class DynamicFoo {} // Noncompliant {{Make this "DynamicFoo" class non-dynamic}}

public class Parent {
  class nested {}
}
