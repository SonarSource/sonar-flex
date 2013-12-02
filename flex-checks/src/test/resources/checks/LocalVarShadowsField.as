class A {
  public var field:int;

  public function set field(field) { // OK (Accessor)
    this.field = field
  }

  public function A(field:int) {    // OK (Constructor)
    this.field = field;
  }

  public function f1() {
    var field:int = 0;              // NOK
  }

  public function f2(field) {}      // NOK

  public function f3() {
    public function nestedFunction() {
      var field;                    // NOK
    }
  }

}
