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
	
	import flash.utils.Proxy;
	import flash.utils.flash_proxy;
	
	/**
	 * A MethodInvoker is a representation of a method call or invocation. Use this to dynamically invoke methods
	 * on an object.
	 *
	 * <p><code>var invoker:MethodInvoker = new MethodInvoker();
	 * invoker.target = myInstance;
	 * invoker.method = "aMethod";
	 * invoker.arguments = ["arg1", 123, true];
	 * var result:* = invoker.invoke();</code></p>
	 *
	 * @author Christophe Herreman
	 */
	public class MethodInvoker {
		
		private var _target:*;
		
		private var _method:String = "";
		
		private var _arguments:Array;
		
		/**
		 * Creates a new MethodInvoker object.
		 */
		public function MethodInvoker() {
			_arguments = [];
		}
		
		/**
		 * Returns the target of this MethodInvoker.
		 */
		public function get target():* {
			return _target;
		}
		
		/**
		 * Sets the target of this MethodInvoker.
		 *
		 * @param value the target, either a class or instance
		 */
		public function set target(value:*):void {
			_target = value;
		}
		
		/**
		 * Returns the method this MethodInvoker is dealing with.
		 */
		public function get method():String {
			return _method;
		}
		
		/**
		 * Sets the method this MethodInvoker is dealing with.
		 */
		public function set method(value:String):void {
			_method = value;
		}
		
		/**
		 * Returns the arguments used by this MethodInvoker.
		 */
		public function get arguments():Array {
			return _arguments;
		}
		
		/**
		 * Sets the arguments used by this MethodInvoker.
		 */
		public function set arguments(value:Array):void {
			_arguments = value;
		}
		
		/**
		 * Executes this MethodInvoker.
		 */
		public function invoke():* {
			var result:*;
			var f:Function = target[method];
			
			if (f != null) {
				result = f.apply(target, this.arguments);
			} else {
				// we don't have a valid function, this might be a proxied method call
				if (target is Proxy) {
					var args:Array = [method].concat(this.arguments);
					result = Proxy(target).flash_proxy::callProperty.apply(target, args);
				}
			}
			
			return result;
		}
	}
}
