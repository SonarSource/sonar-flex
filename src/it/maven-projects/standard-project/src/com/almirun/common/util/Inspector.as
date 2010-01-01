package com.almirun.common.util {
	import flash.display.DisplayObjectContainer;
	import flash.display.DisplayObject;
	import flash.utils.getQualifiedClassName;
	import flash.utils.describeType;

	/**
	 * @author JoshM
	 */
	public class Inspector {
		public static function getDescendantsOfType(clazz:Class,
				container:DisplayObjectContainer):Array {
			var children:Array = [];
			var child:DisplayObject;
			
			for (var i:int = 0; i < container.numChildren; i++) {
				child = container.getChildAt(i);
				
				if (child is DisplayObjectContainer) {
					children = children.concat(getDescendantsOfType(clazz,
						child as DisplayObjectContainer));
				}
				
				if (child is clazz) {
					children.push(child);
				}
			}
			
			return children;
		} 
		
		public static function deepTrace(targetObj:Object, level:Number = 0):String{
                var objNames:XMLList = describeType(targetObj)..variable;
                trace("I have " + objNames.length + " elements in objNames");
                trace(objNames);
                var str:String = "";
                var output:String = "";
                for (var i:Number = 0; i < objNames.length; i++) {
                        str = objNames[i].@name;
                        if (typeof (targetObj[str]) == "object") {
                                output += getSpace(level) + str + "    [" + getQualifiedClassName(targetObj[str]) + "]" + "\n";
                                output += deepTrace(targetObj[str], level + 1);
                        }else {
                                output += getSpace(level) + str + "    " + targetObj[str] + "\n";
                        } 
                }
                
                return output;
        }
        private static function getObjNames(targetObj:Object):Array {
                var result:Array = new Array();
                for (var i:String in targetObj) {
                        result.push(i);
                }
                result.sort();
                return result;
        }
        private static function getSpace(level:Number):String {
                var result:String = "";
                for (var i:int = 0; i < level; i++) {
                        result += "    ";
                }
                return result;
        }
		
	}
}
