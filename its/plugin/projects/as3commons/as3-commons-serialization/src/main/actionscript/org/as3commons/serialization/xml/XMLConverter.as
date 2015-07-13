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
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	
	import org.as3commons.serialization.xml.converters.basic.*;
	import org.as3commons.serialization.xml.converters.extended.ByteArrayConverter;
	import org.as3commons.serialization.xml.core.*;
	
	public class XMLConverter
	{
		
		/**
		 * 
		 * @param nodeName Name of XML node. For example, for <book/> node, use "book". Case sensitive.
		 * @param type Class to map XML values to. Be sure to register Aliases for all child types.
		 * 
		 */		
		public function register(nodeName:String,type:Class):void{
			XMLAlias.registerClassForNodeName(nodeName,type);
		}
		
		public function XMLConverter()
		{	
			
			//Register native type converters
			ConverterRegistery.registerConverter(ArrayConverter,"Array",.5);
			ConverterRegistery.registerConverter(BooleanConverter,"Boolean",.5);
			ConverterRegistery.registerConverter(IntConverter,"int",.5);
			ConverterRegistery.registerConverter(NumberConverter,"Number",.5);
			ConverterRegistery.registerConverter(StringConverter,"String",.5);
			ConverterRegistery.registerConverter(UintConverter,"uint",.5);
			
			//Register reflective converters
			ConverterRegistery.registerConverter(ReflectionConverter,"*",.2);
			
			//Register extended types
			ConverterRegistery.registerConverter(ByteArrayConverter,"flash.utils.ByteArray",.5);
			
		}
		
		public function fromXML(xml:XML,returnType:Class=null):*{		
			return XMLToAS.objectFromXML(xml,xml,returnType);		
		}
		
		public function toXML(obj:Object):XML{
			return ASToXML.xmlFromObject(obj);
		}
		
		public function arrayToXML(array:Array,nodeName:String):XML{
			return ASToXML.xmlFromArray(array,nodeName);
		}
		
		
		//Load and parse convenience functions		
		private static var _urlLoader:URLLoader;
		private static var _request:URLRequest;
		private static var _callbackFunction:Function;
		
		public static function loadAndParse(url:String,callbackFunction:Function):void{
			
			_callbackFunction = callbackFunction;
			
			_urlLoader = new URLLoader();			
			_urlLoader.addEventListener(Event.COMPLETE, loadSuccessHandler);
			_urlLoader.addEventListener(IOErrorEvent.IO_ERROR, loadFailHandler);
			_urlLoader.addEventListener(SecurityErrorEvent.SECURITY_ERROR, loadFailHandler);
			
			_urlLoader.load( new URLRequest(url) );
			
		}
		
		private static function loadSuccessHandler(event:Event):void{
			
			var xml:XML = new XML( event.target.data );
			var converter:XMLConverter = new XMLConverter();		
			var obj:Object = converter.fromXML(xml);	
			_callbackFunction( obj );
			
			cleanUpLoad();
			
		}
		
		private static function loadFailHandler():void{
			cleanUpLoad();
			trace("XMLToAS could not load data from "+_request.url)
		}
		
		private static function cleanUpLoad():void{
			
			_urlLoader.removeEventListener(Event.COMPLETE, loadSuccessHandler);
			_urlLoader.removeEventListener(IOErrorEvent.IO_ERROR, loadFailHandler);
			_urlLoader.removeEventListener(SecurityErrorEvent.SECURITY_ERROR, loadFailHandler);
			
			_request = null;
			_callbackFunction = null;
			_urlLoader = null;
			
		}	

	}
}