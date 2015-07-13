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
	public class XMLAlias
	{
		private static var _aliases:Array=[];
		
		public static function registerClassForNodeName(nodeName:String,clazz:Class):void{
			
			if ( hasClassForNode(nodeName) ){
				trace("XMLAlias:class already registered for node "+nodeName+", this one will be ignored");
				return;
			}
			
			_aliases.push( new Entry(nodeName,clazz) );
			
		}
		
		public static function hasClassForNode(nodeName:String):Boolean{
			
			for each ( var entry:Entry in _aliases ){
				if ( entry.nodeName==nodeName ){
					return true;
				}
			}
			
			return false;
			
		}
		
		public static function classForNodeName(nodeName:String):Class{
			
			for each ( var entry:Entry in _aliases ){
				if ( entry.nodeName==nodeName ){
					return entry.clazz;
				}
			}
			
			return null;
		}
		
		public static function hasNodeForClass(clazz:Class):Boolean{
			
			for each ( var entry:Entry in _aliases ){
				if ( entry.clazz==clazz ){
					return true;
				}
			}
			
			return false;
			
		}
		
		public static function nodeNameForClass(clazz:Class):String{
			
			for each ( var entry:Entry in _aliases ){
				if ( entry.clazz==clazz ){
					return entry.nodeName;
				}
			}
			
			return null;
		}

	}
}

class Entry {
	
	public function Entry(nodeName:String,clazz:Class){
		this.nodeName=nodeName;
		this.clazz=clazz;
	}
	
	public var nodeName:String;
	public var clazz:Class;

}