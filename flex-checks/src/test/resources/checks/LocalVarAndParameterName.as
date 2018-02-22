public function doSomething(PARAM:int, ...REST_PARAM):void  // Noncompliant 2
{
  var LOCAL:int;                             // Noncompliant
}


public function doSomething(param:int):void  // OK
{
  var local:int;                             // OK
  doSomething(local);
}

public function doSomething(param:int):void  // OK
{
}
