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
	 * Basic implementation of the IMetaDataContainer interface.
	 *
	 * @author Christophe Herreman
	 */
	public class MetaDataContainer implements IMetaDataContainer {

		private var _metaData:Array;

		/**
		 * Constructor
		 */
		public function MetaDataContainer(metaData:Array = null) {
			_metaData = (metaData == null ? [] : metaData);
		}

		/**
		 * @inheritDoc
		 */
		public function get metaData():Array {
			return _metaData.concat();
		}

		/**
		 * @inheritDoc
		 */
		public function addMetaData(metaData:MetaData):void {
			_metaData.push(metaData);
		}

		/**
		 * @inheritDoc
		 */
		public function hasMetaData(key:String):Boolean {
			return (getMetaData(key).length > 0);
		}

		/**
		 * @inheritDoc
		 */
		public function getMetaData(key:String):Array {
			var result:Array = [];
			for (var i:int = 0; i<_metaData.length; i++) {
				if (MetaData(_metaData[i]).name == key) {
					result.push(_metaData[i]);
				}
			}
			return result;
		}

	}
}