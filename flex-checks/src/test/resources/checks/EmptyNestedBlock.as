for (var i:int = 0; i < 42; i++){}  // Noncompliant

for (var i:int = 0; i < 42; i++);   // OK

if (myVar == 4)                     // OK
{
  // Do nothing because of X and Y
}

try                                 // Noncompliant@+1
{
}catch (error)                      // OK
{
  // Ignore
}

switch (a) {                        // OK
  case 1:
    break;
  default:
    break;
}

function f()                       // OK
{
  doSomething();
}

class c {}                        // OK
