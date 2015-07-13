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
	
	import org.as3commons.reflect.testclasses.ProxiedClass;
	
	import flexunit.framework.TestCase;
	
	/**
	 * Class description
	 *
	 * @author Christophe Herreman
	 */
	public class MethodInvokerTest extends TestCase {
	
		/**
		 * Creates a new MethodInvokerTest.
		 */
		
		public function MethodInvokerTest(methodName:String=null) {
			super(methodName);
		}
		
		public function testInvoke_forInstanceMethod():void	{
			var array:Array = ["a", "b", "c"];
			var m:MethodInvoker = new MethodInvoker();
			m.target = array;
			m.method = "indexOf";
			m.arguments = ["b"];
			var result:int = m.invoke();
			assertEquals(1, result);
		}
		
		public function testInvoke_forStaticMethod():void	{
			var m:MethodInvoker = new MethodInvoker();
			m.target = Math;
			m.method = "sqrt";
			m.arguments = [64];
			var result:Number = m.invoke();
			assertEquals(8, result);
		}
		
		public function testInvoke_shouldInvokePublicMethodOnProxy():void {
			var proxy:ProxiedClass = new ProxiedClass();
			var m:MethodInvoker = new MethodInvoker();
			m.target = proxy;
			m.method = "getNumber";
			var result:Number = m.invoke();
			assertNotNull(result);
			assertEquals(7, result);
		}
		
		public function testInvoke_shouldInvokeCallPropertyOnProxyWithoutArguments():void {
			var proxy:ProxiedClass = new ProxiedClass();
			var m:MethodInvoker = new MethodInvoker();
			m.target = proxy;
			m.method = "nonExistingMethod";
			var result:Object = m.invoke();
			assertNotNull(result);
			assertEquals("nonExistingMethod", result.methodName);
		}
		
		public function testInvoke_shouldInvokeCallPropertyOnProxyWithArguments():void {
			var proxy:ProxiedClass = new ProxiedClass();
			var m:MethodInvoker = new MethodInvoker();
			m.target = proxy;
			m.method = "nonExistingMethod";
			m.arguments = [1, "a", true];
			var result:Object = m.invoke();
			assertNotNull(result);
			assertEquals("nonExistingMethod", result.methodName);
			assertNotNull(result.args);
			assertEquals(3, result.args.length);
		}
		
	}
}