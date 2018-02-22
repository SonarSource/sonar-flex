function f1(a, b):void      // OK
{
  compute(a);
  function inner() {
    compute(b);
  }
}

function f1(a, b, c):void  // Noncompliant
{
  function f(d, e) {       // Noncompliant
    compute(b);
    compute(d.getAttr());

  }
  compute(a);

}

class A {
  public function f(a, b) { // Noncompliant
    compute(a);
  }
}

public interface I {

  public function fI();    // OK
}
