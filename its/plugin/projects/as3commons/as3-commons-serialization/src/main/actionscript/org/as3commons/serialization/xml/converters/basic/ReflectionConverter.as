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
package org.as3commons.serialization.xml.converters.basic
{
	import flash.utils.getDefinitionByName;
	
	import org.as3commons.reflect.MetaData;
	import org.as3commons.reflect.MetaDataArgument;
	import org.as3commons.reflect.Type;
	import org.as3commons.reflect.Variable;
	import org.as3commons.serialization.xml.ConverterRegistery;
	import org.as3commons.serialization.xml.XMLAlias;
	import org.as3commons.serialization.xml.converters.IConverter;
	import org.as3commons.serialization.xml.core.XMLToAS;
	import org.as3commons.serialization.xml.mapper.Mapper;

	public class ReflectionConverter implements IConverter
	{
		public function canConvert(clazz:Class):Boolean
		{
			return clazz == Object 
			|| ( clazz != Array
			&& clazz != Boolean
			&& clazz != int
			&& clazz != Number
			&& clazz != String
			&& clazz != uint);
			
		}
		
		public function fromXML(typeXML:XML,contextXML:XML):Object
		{
			var returnType:Class;
			
			//First try to get Class from XMLAlias (meaning XMLConverter.getClassByAlias() was called for this type)
			returnType = XMLAlias.classForNodeName( typeXML.name() );
			
			if ( ! returnType ){
				//If no type was registered, fall back to String value interpretation
				var returnTypeString:String = Mapper.resolveNativeTypeFromStringValue( typeXML.valueOf() );
				returnType = getDefinitionByName(returnTypeString) as Class;
			}
			
			var obj:Object = new returnType();
			return map(typeXML,obj);
			
		}
		
		protected function map(xml:XML,obj:Object):Object{
			
			//Map all atributes					
			var propertyType:String;
			var converter:IConverter;
			
			for each ( var attribute:XML in xml.@* ){
				
				var attributeName:String = attribute.localName();
				var attributeStringValue:String = attribute.toString();
				
				propertyType = Mapper.resolveTypeByReflection(obj,attributeName);				
				if ( ! propertyType ) propertyType = Mapper.resolveNativeTypeFromStringValue(attributeStringValue);					
				
				converter = ConverterRegistery.getConverterForType(propertyType);				
				
				var attributeNativeValue:Object = converter.fromXML( attribute, xml );
				setValueOnObject( obj, attributeName, attributeNativeValue );
				
				
				//Since this value was populated from an attirbute, inject metadata to make it persistent in output XML
				var metadata:MetaDataArgument = new MetaDataArgument("attribute","true");
				addMetadataToProperty(obj, attributeName,metadata);
				
			}
			
			//Map all children
			for each ( var childXML:XML in xml.* ){

				if ( childXML.name() ){
				
					graphXMLToObject(childXML,obj,xml);
				
				} else {
					
					//This happens when an object is represented by a single string value
					//Map terminal node to default "value" property if possible
					var stringValue:String = childXML.toString();
					
					try {
						
						obj = stringValue;
						
					} catch (e:Error){
						
						if ( obj.hasOwnProperty("value") ){
							
							propertyType = Mapper.resolveTypeByReflection(obj,"value");				
							if ( ! propertyType ) propertyType = Mapper.resolveNativeTypeFromStringValue(childXML.toString());
							
							converter = ConverterRegistery.getConverterForType(propertyType);
	
							var defaultValue:Object = converter.fromXML( childXML, xml );
							setValueOnObject( obj, "value", defaultValue );
							
						}	
					}					
					
				}
			}				
			
			return obj;	
		}
		
		protected function graphXMLToObject(xml:XML,obj:Object,contextXML:XML):void{
			
			var propName:String = xml.name();
			var stringValue:String = xml.toString();
			
			var nativeType:String = nativeType = Mapper.resolveTypeFromAlias( propName );
			if ( ! nativeType ) nativeType = Mapper.resolveTypeByReflection(obj,propName);
			if ( ! nativeType ) nativeType = Mapper.resolveNativeTypeFromStringValue( stringValue );
			
			var nativeValue:Object;
			
			//There is a converter registered for this type, use it to convert XML to AS
			if ( ConverterRegistery.hasConverterForType(nativeType) ){
				
				var converter:IConverter  = ConverterRegistery.getConverterForType(nativeType);
				nativeValue = converter.fromXML(xml,contextXML);
				
				//For strings, check for CDATA tags. If they exist, add metadata so it is persistent
				if ( nativeType == "String") {
					
					var str:String = xml.toXMLString();
					if ( str.indexOf("<![CDATA[") != -1 ){
						
						var metadata:MetaDataArgument = new MetaDataArgument("cdata","true");
						addMetadataToProperty(obj,propName,metadata);
						
					}
					
				}
				
			} else {
				
				//Object			
				var returnType:Class;
				
				//Try to use reflection to get nativeType
				if ( obj.hasOwnProperty( propName ) ){
						
					var type:Type = Type.forInstance( obj );
					for each ( var variable:Variable in type.variables ){
						if ( variable.name == propName ){
							returnType = getDefinitionByName( variable.type.fullName ) as Class;
							break;
						}
					}
					
				}
				
				nativeValue = XMLToAS.objectFromXML( xml, contextXML, returnType );								
			
			}
			
			setValueOnObject(obj,propName,nativeValue);
			
		}
		
		//Attempts to set propName on object to value or issues a warning 
		protected function setValueOnObject(obj:Object,propertyName:String,value:Object):void{
			
			if ( obj.hasOwnProperty( propertyName ) ){
				
				//Fix for 0 length children to Array for now
				if ( obj[propertyName] is Array && ! value is Array ) value = null;
				
				//Has enumerated property, set it directly
				obj[propertyName] = value;
				
			} else {
				//Is Dynamic, create new dynamic property
				if ( obj is Object ){
					obj[propertyName] = value;
				} else {
					trace("XMLToAS:Could not map "+propertyName+" on "+obj);	
				}
			}
			
			return;
		}
		
		protected function addMetadataToProperty(obj:Object,propertyName:String,newArg:MetaDataArgument):void{
			
			var type:Type = Type.forInstance(obj);
			
			for each ( var variable:Variable in type.variables ){
				
				if ( variable.name == propertyName ){
					
					if ( variable.hasMetaData("X2A") ) {
						
						//If it already has some X2A metadata, see if it already has this particular MetaDataArgument
							
						var metadata:Array = variable.getMetaData("X2A");
						
						for each ( var arg:MetaDataArgument in MetaData( metadata[0] ).arguments ){						
													
							if ( arg.key == newArg.key ){
								//It already has this argument, so ignore new one						
								return;					
							}
						}
						
						MetaData( metadata[0] ).arguments.push( newArg );
						
					}
					
					variable.addMetaData( new MetaData("X2A", [newArg] ) );
					return;
				}
			}
			
		}	
		
		public function toString(obj:Object):String
		{
			//TODO
			return null;
		}
		
	}
}