for (;;) {
  var a = new MyObject();             // NOK
}

for (;;) {
 doSomething(new MyObject());         // NOK
}

for (;;) {
  var a = {a:String, b:String}        // NOK
}

for (;;) {
  var a = new new super.MyObject();   // NOK
}

for (;;) {
  var a = new Vector<MyObject>[1]();  // NOK
}

var obj = new Object();
for (;;) {
  var a:Object = obj                  // OK
}
