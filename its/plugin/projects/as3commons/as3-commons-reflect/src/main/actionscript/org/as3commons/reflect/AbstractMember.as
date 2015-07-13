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
	import flash.system.ApplicationDomain;
	

	/**
	 * Abstract base class for members of a <code>class</object>.
	 *
	 * @author Christophe Herreman
	 */
	public class AbstractMember extends MetaDataContainer implements IMember {

		private var _name:String;
		private var _type:String;
		private var _declaringType:String;
		private var _isStatic:Boolean;
		private var _applicationDomain:ApplicationDomain;

		/**
		 * Creates a new AbstractMember object.
		 *
		 * @param name the name of the member
		 * @param type the type of the member
		 * @param declaringType the type that declares the member
		 * @param isStatic whether this member is static
		 * @param metadata an array of MetaData objects describing this member
		 */
		public function AbstractMember(name:String, type:String, declaringType:String, isStatic:Boolean, metaData:Array = null) {
			super(metaData);
			_name = name;
			_type = type;
			_declaringType = declaringType;
			_isStatic = isStatic;
		}

		public function get declaringType():Type {
			return Type.forName(_declaringType);
		}

		public function get name():String {
			return _name;
		}

		public function get type():Type {
			return Type.forName(_type, declaringType.applicationDomain);
		}

		public function get isStatic():Boolean {
			return _isStatic;
		}

		
	}
}
