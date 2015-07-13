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
package org.as3commons.reflect.testclasses {
	
	import flash.utils.Proxy;
	import flash.utils.flash_proxy;
	
	/**
	 * Class description
	 *
	 * @author Christophe
	 * @since 26-jan-2009
	 */
	public class ProxiedClass extends Proxy {
	
		/**
		 * Creates a new ProxiedClass object.
		 */
		public function ProxiedClass() {
			super();
		}

		flash_proxy override function getProperty(name:*):* {
			return null;
		}

		flash_proxy override function callProperty(methodName:*, ... args):* {
			return {methodName:methodName, args:args};
		}
		
		public function getNumber():Number {
			return 7;
		}
	}
}