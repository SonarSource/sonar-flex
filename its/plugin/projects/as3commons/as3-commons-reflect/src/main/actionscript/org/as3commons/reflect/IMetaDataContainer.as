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
	 * Desribes an object that holds MetaData objects.
	 *
	 * @author Christophe Herreman
	 */
	public interface IMetaDataContainer {

		/**
		 * Adds a MetaData object to this container.
		 */
		function addMetaData(metaData:MetaData):void;

		/**
		 * Returns whether this object has meta data for the given key.
		 */
		function hasMetaData(key:String):Boolean;

		/**
		 * Returns the array of MetaData object that corresponds to the given key. If no MetaData object was found,
		 * an empty array is returned.
		 */
		function getMetaData(key:String):Array;

		/**
		 * Returns an array of all metadata objects in this container.
		 */
		function get metaData():Array;
	}
}