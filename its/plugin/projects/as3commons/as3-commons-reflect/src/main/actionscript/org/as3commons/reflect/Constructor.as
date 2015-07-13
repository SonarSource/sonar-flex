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
	 * Provides information about a class constructor.
	 * 
	 * @author Martino Piccinato
	 */
	public class Constructor {
		
		private var _parameters:Array = [];
		
		private var _declaringType:Type;
		
		/**
		 * Creates a new Constructor object.
		 * 
		 * @param declaringType The Type declaring the constrcutor.
		 * @param parameters an Array of Parameter objects being the parameters of the constructor.
		 */
		public function Constructor(declaringType:Type, parameters:Array = null) {
			if (parameters != null) {
				this._parameters = parameters;
			}
			this._declaringType = declaringType;
		}
		
		/**
		 * Returns the parameters of this Constructor.
		 */
		public function get parameters():Array {
			return this._parameters;
		}
		
		/**
		 * Returns the declaring type of this Constructor.
		 */
		public function get declaringType():Type {
			return this._declaringType;
		}
		
		/**
		 * @return <code>true</code> if the constructor has no arguments, <code>false</code>
		 * otherwise.
		 */
		public function hasNoArguments():Boolean {
			return (this._parameters.length == 0 ? true : false);
		}
		
	}
}
