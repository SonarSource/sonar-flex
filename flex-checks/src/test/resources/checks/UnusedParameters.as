function f1(a, b):void      // OK
{
  compute(a);
  function inner() {
    compute(b);
  }
}

function f1(a, b, c):void  // NOK
{
  function f(d, e) {       // NOK
    compute(b);
    compute(d.getAttr());

  }
  compute(a);

}

class A {
  public function f(a, b) { // NOK
    compute(a);
  }
}
