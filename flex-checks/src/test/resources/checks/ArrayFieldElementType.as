class A {

  [ArrayElementType("Number")]
  public var newNumberProperty:Array;               // OK

  [ArrayElementType("Number")]
  [Transient]
  public var newNumberProperty:Array;               // OK

  public var  customArray:CustomArray;              // OK
  public var newNumberProperty:Array = [0, 1, 2];   // OK

  public var newNumberProperty:Array;               // Noncompliant{{Define the element type for this 'newNumberProperty' array}}

}
