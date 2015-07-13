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
	import org.as3commons.serialization.xml.converters.IConverter;
	import org.as3commons.serialization.xml.core.XMLToAS;

	public class ArrayConverter implements IConverter
	{
		public function canConvert(clazz:Class):Boolean
		{
			return clazz == Array;
		}
		
		public function fromXML(typeXML:XML, contextXML:XML):Object
		{
			var array:Array=[];
			if ( typeXML.children().length() == 0 ) return array;
			
			for each ( var childXML:XML in typeXML.children() ){
				array.push( XMLToAS.objectFromXML( childXML, contextXML ) );
			}
			
			return array;
			
		}
		
		public function toString(obj:Object):String
		{
			//TODO
			return null;
		}
		
	}
}