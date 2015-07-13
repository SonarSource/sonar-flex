/*
 * Copyright (c) 2007-2009 the original author or authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.as3commons.reflect {

	/**
	 * A MetaData object contains information about a metadata element placed above a member of a class or instance.
	 *
	 * @author Christophe Herreman
	 */
	public class MetaData {

		public static const TRANSIENT:String = "Transient";
		public static const BINDABLE:String = "Bindable";

		private var _name:String;
		private var _arguments:Array;

		/**
		 * Creates a new MetaData object.
		 *
		 * @param name the name of the metadata
		 * @param arguments an array of MetaDataArgument object that describe this metadata object
		 */
		public function MetaData(name:String, arguments:Array = null) {
			_name = name;
			_arguments = (arguments == null) ? [] : arguments;
		}

		/**
		 * Returns the name of this metadata.
		 */
		public function get name():String {
			return _name;
		}

		/**
		 * Returns the MetaDataArgument objects that describe this metadata.
		 */
		public function get arguments():Array {
			return _arguments;
		}

		/**
		 * Returns whether this metadata contains a MetaDataArgument with the given key as its name.
		 *
		 * @return true if a MetaDataArgument was found; false otherwise
		 */
		public function hasArgumentWithKey(key:String):Boolean {
			return (getArgument(key) != null);
		}

		/**
		 * Returns the MetaDataArgument for the given key. If none was found, null is returned.
		 */
		public function getArgument(key:String):MetaDataArgument {
			var result:MetaDataArgument;

			for (var i:int = 0; i<_arguments.length; i++) {
				if (_arguments[i].key == key) {
					result = _arguments[i];
					break;
				}
			}

			return result;
		}

		public function toString():String {
			return "[MetaData(" + name + ", " + arguments +")]";
		}

	}
}