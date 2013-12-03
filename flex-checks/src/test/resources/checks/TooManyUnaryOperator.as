function f() {
  var j:int = foo++ - --bar;  // NOK

  --b + a++;                  // NOK

  --b;                        // OK

  i = i++ + a;                // OK
}
