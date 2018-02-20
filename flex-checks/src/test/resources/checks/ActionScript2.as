intrinsic public function foo() {
  if (true <> false) {} // Noncompliant {{Operator '<>' not available in ActionScript 3.0}}
  if (true != false) {}
  //if (not true) {}
  if (!true) {}
  if (false or true) {} // Noncompliant {{Operator 'or' not available in ActionScript 3.0}}
  if (false || true) {}
  if (false and true) {} // Noncompliant
  if (false && true) {}
  if (0 lt 1) {} // Noncompliant
  if (0 < 1) {}
  set("varName", value);
}
