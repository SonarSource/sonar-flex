/*
 * Copyright (c) 2008-2009 the original author or authors
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
package org.as3commons.logging.impl {
	
	import org.as3commons.logging.ILogger;
	import org.as3commons.logging.ILoggerFactory;
	
	/**
	 * Default AS3Commons logger factory. If no logger factory is set on LoggerFactory, then this is the factory
	 * that will be used.
	 *
	 * @author Christophe Herreman
	 */
	public class DefaultLoggerFactory implements ILoggerFactory {
		
		/**
		 * Creates a new DefaultLoggerFactory
		 */
		public function DefaultLoggerFactory() {
		}
		
		/**
		 * @inheritDoc
		 */
		public function getLogger(name:String):ILogger {
			return new DefaultLogger(name);
		}
	}
}