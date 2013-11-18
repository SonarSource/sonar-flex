public function doSomething(PARAM:int, ...REST_PARAM):void  // NOK
{
  var LOCAL:int;                             // NOK
}


public function doSomething(param:int):void  // OK
{
  var local:int;                             // OK
  doSomething(local);
}

public function doSomething(param:int):void  // OK
{
}
