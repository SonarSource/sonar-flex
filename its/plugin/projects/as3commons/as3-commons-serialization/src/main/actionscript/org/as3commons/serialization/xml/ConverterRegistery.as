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
package org.as3commons.serialization.xml
{
	import flash.utils.getDefinitionByName;
	
	import org.as3commons.lang.ClassUtils;
	import org.as3commons.serialization.xml.converters.IConverter;
	
	public class ConverterRegistery
	{
		private static var _converters:Array=[];
		
		public static function registerConverter(converter:Class,type:String,priority:Number=.5):void{
			
			if ( hasConverterForTypeName(type) ){
				trace("Converter already registered with this type: "+type);
				return;
			}
			
			var converterInstance:IConverter = new converter();
			var entry:Entry = new Entry(type,converterInstance,priority);		
			
			_converters.push( entry );
			_converters.sortOn("priority",Array.DESCENDING);
			
		}
		
		public static function getConverterForType(type:String):IConverter{
			
			var entry:Entry;
			
			for each ( entry in _converters ){
				if ( entry.type == type ){
					return entry.converter;
				}
			}
			
			var clazz:Class = getDefinitionByName( type ) as Class;
			if ( ! clazz ) return null;
			
			for each ( entry in _converters ){
				if ( entry.converter.canConvert(clazz) ){
					return entry.converter;
				}
			}
			
			
			return null;
		}
		
		public static function hasConverterForType(type:String):Boolean{
			
			if ( hasConverterForTypeName(type) ){
				return true;
			}
			
			var clazz:Class = ClassUtils.forName( type );
			if ( ! clazz ) return false;
			
			return hasConverterForClass( clazz );
			
		}
		
		protected static function hasConverterForTypeName(type:String):Boolean{
			
			for each ( var entry:Entry in _converters ){
				if ( entry.type == type ){
					return true;
				}
			}
			
			return false;
			
		}
		
		protected static function hasConverterForClass(clazz:Class):Boolean{
			
			for each ( var entry:Entry in _converters ){
				if ( entry.converter.canConvert(clazz) ){
					return true;
				}
			}
			
			return false;
			
		}
		
	}
}

import org.as3commons.serialization.xml.converters.IConverter;	

class Entry {
	
	public function Entry(type:String,converter:IConverter,priority:Number){
		this.type=type;
		this.priority=priority;
		this.converter=converter;
	}
	
	public var type:String;
	public var converter:IConverter;
	public var priority:Number;

}