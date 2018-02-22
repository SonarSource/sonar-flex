if (x === y); // OK
if (x !== y); // OK

if (x == y);  // Noncompliant {{Replace == with ===}}
if (x != y);  // Noncompliant
