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
package org.as3commons.serialization.xml.core
{
	import org.as3commons.lang.StringUtils;
	import org.as3commons.reflect.MetaData;
	import org.as3commons.reflect.Type;
	import org.as3commons.reflect.Variable;
	import org.as3commons.serialization.xml.ConverterRegistery;
	import org.as3commons.serialization.xml.XMLAlias;
	import org.as3commons.serialization.xml.converters.IConverter;
	import org.as3commons.serialization.xml.mapper.Mapper;
	
	public class ASToXML
	{
		
		/**
		 * Recursively generates an XML tree from a Actionscript instance
		 * @param obj Actionscript Object
		 * @return XML object representing Actionscript instance
		 * 
		 */		
		public static function xmlFromObject(obj:Object):XML{
			
			var type:Type = Type.forInstance( obj );
			var parentVars:Array;			
			
			if ( type.name == "Object" ){
				
				//Type doesn't work with non-enumerated properties, so build them up here
				
				parentVars = [];				
				for ( var key:String in obj ){
					var propertyType:String = Mapper.resolveTypeByReflection(obj,key);				
					if ( ! propertyType ){
						propertyType = Mapper.resolveNativeTypeFromStringValue(obj[key]);
					}
					parentVars.push( new Variable(key,propertyType,"",false) );
				}
				
			} else {
				
				parentVars = type.variables;
				
			}
			
			var xml:XML;
			var nodeName:String = XMLAlias.nodeNameForClass( type.clazz );
			if ( nodeName ){
				//substr off X2A to get to node name
				xml = new XML("<"+nodeName+"/>");
			} else {
				//TODO: Get original node name from metadata?
				xml = new XML("<"+StringUtils.uncapitalize(type.name)+"/>");
			}				
			
			for each ( var variable:Variable in parentVars ){
				
				var name:String = variable.name;
				var nativeValue:Object = obj[variable.name];
				
				if ( nativeValue == null || typeHasX2AMetadata(type,name,"exclude","true") ){
					
					//Ignore
					
				} else if ( nativeValue is Array ){
					
					xml[name] = xmlFromArray( nativeValue as Array, name );
					
				} else {
					
					var childType:Type = Type.forInstance( nativeValue );
					if ( childType.variables.length > 0 ){
					
						xml[name] = xmlFromObject(nativeValue);
						
					} else {
						
						var converter:IConverter = ConverterRegistery.getConverterForType(variable.type.fullName);
						var stringValue:String = converter.toString( obj[variable.name] );
						
						//Node or attribute
						if ( typeHasX2AMetadata(type,name,"attribute","true") ){
					
							xml.@[name] = stringValue;
					
						} else {
							
							//Check for cdata metadata
							//TODO: For performance, exclude types which are not likely to have CDATA like int
							if ( typeHasX2AMetadata(type,name,"cdata","true") ){
																	
								xml[name] = new XML();
								XML( xml[name] ).appendChild( new XML("<![CDATA[" + stringValue + "]]>") );
							
							} else {
								
								//String value without CDATA
								xml[name] = stringValue;
							
							}
							
						}
						
					}
				}
				
			}
			
			return xml;
			
		}
		
		private static function typeHasX2AMetadata(parentType:Type,propertyName:String,metadataName:String,metadataValue:String):Boolean{
			
			for each ( var variable:Variable in parentType.variables ){
				
				if ( variable.name == propertyName && variable.hasMetaData("X2A") ){	
					
					var metadata:Array = variable.getMetaData("X2A");
					return MetaData( metadata[0] ).hasArgumentWithKey( metadataName );
				
				}
			
			}
			
			return false;
			
		}
		
		/**
		 * Itereates over an array of Objects, returning populated XML object
		 * @param array Array of Actionscript Object instances
		 * @param nodeName Name of node, this corresponds to the property name on parent Object
		 * @return XML structure which corresponds to Actionscript Array
		 * 
		 */		
		public static function xmlFromArray(array:Array,nodeName:String="data"):XML{
			
			var xml:XML = new XML("<"+nodeName+"/>");
			
			for each( var obj:Object in array ){
				xml.appendChild( xmlFromObject(obj) );
			}
			
			return xml;
		}
			
	}
}