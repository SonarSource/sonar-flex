/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.as3commons.serialization.xml.mapper
{

	import flash.utils.getQualifiedClassName;
	
	import org.as3commons.reflect.Type;
	import org.as3commons.reflect.Variable;
	import org.as3commons.serialization.xml.XMLAlias;
	
	public class Mapper
	{
		
		//returns Actionscript type of property name on object
		public static function resolveTypeByReflection(obj:Object,propertyName:String):String{
			
			if ( obj is Array ) return "Array";
			
			var type:Type = Type.forInstance( obj );
			var parentVars:Array = type.variables;
			
			for each ( var variable:Variable in parentVars ){
				if ( variable.name == propertyName ){
					return(variable.type.fullName);
				}
			}
			
			return undefined;
			
		}
		
		//returns Actionscript type of property name on object
		public static function resolveTypeFromAlias(name:String):String{
			
			var clazz:Class = XMLAlias.classForNodeName( name );
			if ( clazz ) return getQualifiedClassName( clazz );	
			
			return undefined;
			
		}
		
		/**
		 * Resolves a string value to a native ActionScript type using a set of rules.
		 * @param stringValue
		 * @return Qualified class name for class represented by stringValue. ex: "Array" or "Object"
		 * 
		 */		
		
		public static function resolveNativeTypeFromStringValue(stringValue:String):String{
				
			stringValue = stringValue.toLowerCase();
			
			//Check for Boolean
			if ( stringValue == "true" || stringValue == "false" ){
				return "Boolean";
			}
					
			//Match #FF00FF or 0x1dc4fb to uint
			if ( ( stringValue.indexOf("#") == 0 && stringValue.length == 7 ) 
			|| ( stringValue.indexOf("x") == 1 && stringValue.length == 8 ) ){
				return "uint";
			}
			
			//Check for int and Number
			var regex:RegExp;		
			regex = /[^0-9]/;
			
			if ( stringValue.search(regex) == -1 ){
				
				if ( stringValue.indexOf(".") == -1 ){
					return "int";
				} else {
					return "Number";
				}
				
			}			
			
			//Check for not an Array or Object
			
			//TODO: This is a fairly loose check for xml, could result in RTE below if it is not actually XML
			//TODO: This cannot tell the difference between an Array of different objects and an Object with different properties
			if ( stringValue.indexOf("<") != 0 && stringValue.lastIndexOf(">") != stringValue.length-1 ){
				return "String";
			}
			
			//Convert to XML and decide between Object and Array
			var xml:XML = new XML(stringValue);		
			
			//Either another custom type or an array of objects			
			var firstChildName:String = xml.children()[0].name();
			for each ( var childXML:XML in xml.children() ){
				if ( childXML.name() != firstChildName ){
					//Treat as Object
					return "Object";
				}
				
			}
			
			//Since all children have the same name (Vector), treat as Array
			return "Array";					
			
		}

	}
}