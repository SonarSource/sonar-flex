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
package org.as3commons.reflect.errors {
	
	import org.as3commons.lang.ClassNotFoundError;
	import org.as3commons.logging.ILogger;
	import org.as3commons.logging.LoggerFactory;
	
	/**
	 * Thrown when an application tries to retrieve a class by its name and
	 * the corresponding class could not be found.
	 *
	 * @author Christophe Herreman
	 */
	public class ClassNotFoundError extends org.as3commons.lang.ClassNotFoundError {
		
		private static var logger:ILogger = LoggerFactory.getClassLogger(org.as3commons.reflect.errors.ClassNotFoundError);
		
		/**
		 * Creates a new <code>ClassNotFoundError</code> object.
		 */
		public function ClassNotFoundError(message:String = "") {
			super(message);
			logger.warn("The class ClassNotFoundError is deprecated in as3commons-reflect and is now available in as3commons-lang.");
		}
	}
}
