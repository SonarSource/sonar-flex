for (;;) {
  var a = new MyObject();             // Noncompliant {{Move the instantiation of this "MyObject" outside the loop.}}
}

for (;;) {
 doSomething(new MyObject());         // Noncompliant
}

for (;;) {
  var a = {a:String, b:String}        // Noncompliant
}

for (;;) {
  var a = new new super.MyObject();   // Noncompliant {{Move the instantiation of this "super.MyObject" outside the loop.}}
}

for (;;) {
  var a = new Vector<MyObject>[1]();  // Noncompliant
}

var obj = new Object();
for (;;) {
  var a:Object = obj                  // OK
}
