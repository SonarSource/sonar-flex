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
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	
	import org.as3commons.reflect.MetaData;
	import org.as3commons.reflect.MetaDataArgument;
	import org.as3commons.reflect.Type;
	import org.as3commons.reflect.Variable;
	import org.as3commons.serialization.xml.ConverterRegistery;
	import org.as3commons.serialization.xml.XMLConverter;
	import org.as3commons.serialization.xml.converters.IConverter;
	import org.as3commons.serialization.xml.mapper.Mapper;

	public class XMLToAS
	{
		
		public static function objectFromXML(xml:XML,contextXML:XML,returnType:Class=null):*{
			
			var returnTypeString:String;
			
			//First try reflection
			returnTypeString = Mapper.resolveTypeFromAlias( xml.name() );
			
			if ( ! returnTypeString ){
				
				//Fall back to String value interpretation
				returnTypeString = Mapper.resolveNativeTypeFromStringValue( xml.valueOf() );
			
			}	
			
			//Find converter for return type
			var converter:IConverter = ConverterRegistery.getConverterForType( returnTypeString );
			
			//Use converter to convert XML
			var obj:Object = converter.fromXML(xml,contextXML);
							
			return obj;
				
		}
		
		private static function addMetadataToProperty(obj:Object,propertyName:String,newArg:MetaDataArgument):void{
			
			var type:Type = Type.forInstance(obj);
			
			for each ( var variable:Variable in type.variables ){
				
				if ( variable.name == propertyName && variable.hasMetaData("X2A") ){
					
					//If it already has some X2A metadata, see if it already has this particular MetaDataArgument			
					var metadata:MetaData = variable.getMetaData("X2A")[0];
					if ( metadata.hasArgumentWithKey(newArg.key) ) return;			
										
					variable.addMetaData( new MetaData("X2A", [newArg] ) );
					return;
					
				}
			}
			
		}
			
	}
}