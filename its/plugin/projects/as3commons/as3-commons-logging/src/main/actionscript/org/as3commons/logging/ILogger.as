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
	 * The main logging interface to abstract logger implementations.
	 *
	 * @author Christophe Herreman
	 */
	public interface ILogger {
		
		/**
		 * Returns the name of this logger.
		 */
		//function get name():String;
		
		/**
		 * Logs a message with a "debug" level.
		 */
		function debug(message:String, ... params):void;
		
		/**
		 * Logs a message with a "info" level.
		 */
		function info(message:String, ... params):void;
		
		/**
		 * Logs a message with a "warn" level.
		 */
		function warn(message:String, ... params):void;
		
		/**
		 * Logs a message with a "error" level.
		 */
		function error(message:String, ... params):void;
		
		/**
		 * Logs a message with a "fatal" level.
		 */
		function fatal(message:String, ... params):void;
		
		/**
		 * Is debug logging currently enabled?
		 */
		function get debugEnabled():Boolean;
		
		/**
		 * Is info logging currently enabled?
		 */
		function get infoEnabled():Boolean;
		
		/**
		 * Is warn logging currently enabled?
		 */
		function get warnEnabled():Boolean;
		
		/**
		 * Is error logging currently enabled?
		 */
		function get errorEnabled():Boolean;
		
		/**
		 * Is fatal logging currently enabled?
		 */
		function get fatalEnabled():Boolean;
	
	}
}