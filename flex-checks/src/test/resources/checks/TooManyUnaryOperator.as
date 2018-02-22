function f() {
  var j:int = foo++ - --bar;  // Noncompliant {{Split this expression into multiple expressions so that each one contains no more than a single "++" or "--" unary operator}}

  --b + a++;                  // Noncompliant

  --b;                        // OK

  i = i++ + a;                // OK
}
