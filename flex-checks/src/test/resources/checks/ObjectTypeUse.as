var obj:Object = new String(); // Noncompliant
var obj:Object = "Hello";      // Noncompliant
var foo = new Object();        // Noncompliant
var foo = new Object;          // Noncompliant
var obj = {a:String, b:String} //Noncompliant

var obj:String = new String(); // OK
var obj = new String();        // OK
var obj;                       // OK
