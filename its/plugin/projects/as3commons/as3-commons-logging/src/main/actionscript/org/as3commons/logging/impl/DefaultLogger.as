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
	
	import org.as3commons.logging.LogLevel;
	import org.as3commons.logging.util.MessageUtil;
	
	/**
	 * Default AS3Commons logging implementation of the ILogger interface that writes messages to the console using
	 * the trace() method. If no ILoggerFactory is set on the LoggerFactory, then this is the logger that will be used.
	 *
	 * @author Christophe Herreman
	 */
	public class DefaultLogger extends AbstractLogger {
		
		private var _level:int;
		
		/**
		 * Creates a new DefaultLogger.
		 */
		public function DefaultLogger(name:String) {
			super(name);
		}
		
		public function set level(value:int):void {
			_level = value;
		}
		
		override protected function log(level:uint, message:String, params:Array):void {
			if (level >= this._level) {
				//var message:String = "";
				
				var msg:String = "";
				
				// add datetime
				msg += (new Date()).toString() + " " + LogLevel.toString(level) + " - ";
				
				// add name and params
				msg += name + " - " + MessageUtil.toString(message, params);
				
				// trace the message
				trace(msg);
			}
		}
		
		/**
		 * @inheritDoc
		 */
		override public function get debugEnabled():Boolean {
			return (_level <= LogLevel.DEBUG);
		}
		
		/**
		 * @inheritDoc
		 */
		override public function get infoEnabled():Boolean {
			return (_level <= LogLevel.INFO);
		}
		
		/**
		 * @inheritDoc
		 */
		override public function get warnEnabled():Boolean {
			return (_level <= LogLevel.WARN);
		}
		
		/**
		 * @inheritDoc
		 */
		override public function get errorEnabled():Boolean {
			return (_level <= LogLevel.ERROR);
		}
		
		/**
		 * @inheritDoc
		 */
		override public function get fatalEnabled():Boolean {
			return (_level <= LogLevel.FATAL);
		}
	}
}