class A {

  [ArrayElementType("Number")]
  public var newNumberProperty:Array;               // OK

  public var  customArray:CustomArray;              // OK
  public var newNumberProperty:Array = [0, 1, 2];   // OK

  public var newNumberProperty:Array;               // NOK

}
