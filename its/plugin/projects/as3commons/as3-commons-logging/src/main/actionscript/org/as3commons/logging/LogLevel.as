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
package org.as3commons.logging {
	
	/**
	 * LogLevel enumeration
	 *
	 * @author Martin Heidegger
	 * @version 1.0
	 */
	public class LogLevel {
		
		public static const DEBUG:int = 1;
		
		public static const INFO:int = 2;
		
		public static const WARN:int = 4;
		
		public static const ERROR:int = 8;
		
		public static const FATAL:int = 16;
		
		public static const ALL:int = DEBUG | INFO | WARN | ERROR | FATAL;
		
		public static const NONE:int = 0;
		
		public static function match(level:int, toLevel:int):Boolean {
			return (level & toLevel) == level;
		}
		
		public static function toString(level:int):String {
			switch (level) {
				case LogLevel.DEBUG:
					return "DEBUG";
				
				case LogLevel.INFO:
					return "INFO";
				
				case LogLevel.WARN:
					return "WARN";
				
				case LogLevel.ERROR:
					return "ERROR";
				
				case LogLevel.FATAL:
					return "FATAL";
			}
			return "???";
		}
	}
}
