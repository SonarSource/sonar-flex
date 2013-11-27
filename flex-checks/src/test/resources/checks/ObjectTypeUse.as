var obj:Object = new String(); // NOK
var obj:Object = "Hello";      // NOK
var foo = new Object();        // NOK
var foo = new Object;          // NOK

var obj:String = new String(); // OK
var obj = new String();       // OK
