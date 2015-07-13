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
	 * Provides information about a single method of a class or interface.
	 *
	 * @author Christophe Herreman
	 */
	public class Method extends MetaDataContainer {

		/**
		 * Creates a new <code>Method</code> instance.
		 */
		public function Method(declaringType:Type, name:String, isStatic:Boolean, parameters:Array, returnType:*, metaData:Array = null) {
			super(metaData);
			_declaringType = declaringType;
			_name = name;
			_isStatic = isStatic;
			_parameters = parameters;
			_returnType = returnType;
		}

		/**
		 * Invokes (calls) the method represented by this <code>Method</code>
		 * instance of the given <code>target</code> object with the passed in
		 * arguments.
		 *
		 * @param target the object on which to invoke the method
		 * @param args the arguments that will be passed along the method call
		 * @return the result of the method invocation, if any
		 */
		public function invoke(target:*, args:Array):* {
			var invoker:MethodInvoker = new MethodInvoker();
			invoker.target = target;
			invoker.method = name;
			invoker.arguments = args;
			return invoker.invoke();
		}

		public function get declaringType():Type { return _declaringType; };
		public function get name():String { return _name; };
		public function get isStatic():Boolean { return _isStatic; };
		public function get parameters():Array { return _parameters; };
		public function get returnType():Type { return _returnType; };

		public function get fullName():String {
			var result:String = "public ";
			if (isStatic) result += "static ";
			result += name + "(";
			for (var i:int=0; i<parameters.length; i++) {
				var p:Parameter = parameters[i] as Parameter;
				result += p.type.name;
				result += (i < (parameters.length-1)) ? ", " : "";
			}
			result += "):" + returnType.name;
			return result;
		}

		public function toString():String {
			return "[Method(name:'" + name + "', isStatic:" + isStatic + ")]";
		}

		private var _declaringType:Type;
		private var _name:String;
		private var _isStatic:Boolean;
		private var _parameters:Array;
		private var _returnType:Type;

	}
}
