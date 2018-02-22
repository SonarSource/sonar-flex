class A {
  public var field:int;

  public function set field(field) { // OK (Accessor)
    this.field = field
  }

  public function setField(field) { // OK
    this.field = field;
  }

  public function A(field:int) {    // OK (Constructor)
    this.field = field;
  }

  public function f1(param) {       // OK
    var foo;                        // OK
    var field:int = 0;              // Noncompliant {{Rename "field" which hides the field declared at line 2.}}
  }

  public function f2(field) {}      // Noncompliant

  public function f3() {
    public function nestedFunction() {
      var field;                    // Noncompliant
    }
  }

}

var field:int = 0;                  // OK
