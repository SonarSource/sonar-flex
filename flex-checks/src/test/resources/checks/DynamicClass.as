class Foo {}                       // OK

public class Foo {}                // OK

public dynamic class DynamicFoo {} // NOK

public class Parent {
  class nested {}
}
