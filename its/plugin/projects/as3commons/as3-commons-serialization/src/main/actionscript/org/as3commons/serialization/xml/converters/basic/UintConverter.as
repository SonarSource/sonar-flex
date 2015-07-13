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
	import org.as3commons.lang.StringUtils;
	import org.as3commons.serialization.xml.converters.IConverter;
	
	public class UintConverter implements IConverter
	{
		public function canConvert(clazz:Class):Boolean{
			return clazz == uint;	
		}
		
		public function fromXML(xml:XML,contextXML:XML):Object
		{
			var stringValue:String = xml.toString();
			if ( stringValue.indexOf("#") == 0 ) {
				stringValue = "0x"+stringValue.substr(1,stringValue.length);
			}
			
			return uint( stringValue );
			
		}
		
		public function toString(obj:Object):String{
			return "0x"+StringUtils.rightPadChar(uint(obj).toString(16).toUpperCase(),6,"0");
		}
		
	}
}