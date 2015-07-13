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
	
	import flash.utils.getQualifiedClassName;
	
	import org.as3commons.logging.impl.DefaultLoggerFactory;
	import org.as3commons.logging.impl.NullLogger;
	
	/**
	 * Use the LoggerFactory to obtain a logger. This is the main class used when working with the as3commons-logging
	 * library.
	 *
	 * <p>You can either request a logger via the LoggerFactory.getClassLogger() or LoggerFactory.getLogger() methods.</p>
	 *
	 * <p>When configuring a custom logger factory, make sure the logger factory is set before a logger is created.
	 * Here is an example (for your main application file):</p>
	 *
	 * <listing version="3.0">// force the FlexLoggerFactory to be set before any loggers are created
	 * private static var loggerSetup:&#42; = (LoggerFactory.loggerFactory = new FlexLoggerFactory());
	 * private static var logger:ILogger = LoggerFactory.getLogger("com.domain.MyClass");</listing>
	 *
	 * <p>By default, the DefaultLoggerFactory will be used that will write all log statements to the console using
	 * trace(). If you don't want any logging, set the logger factory to <code>null</code>.</p>
	 *
	 * <listing version="3.0">// set the logger factory to null because we don't want to see any logging
	 * private static var loggerSetup:&#42; = (LoggerFactory.loggerFactory = null);
	 * private static var logger:ILogger = LoggerFactory.getLogger("com.domain.MyClass");</listing>
	 *
	 * @author Christophe Herreman
	 * @author Martin Heidegger
	 */
	public class LoggerFactory implements ILoggerFactory {
		
		/** The singleton instance, eagerly instantiated. */
		private static var _instance:LoggerFactory = LoggerFactory.getInstance();
		
		/** The logger factory that creates loggers */
		private var _loggerFactory:ILoggerFactory = new DefaultLoggerFactory();
		
		/** A cache of loggers */
		private var _loggers:Object /* <String, ILogger> */  = {};
		
		/**
		 * Constructs a new LoggerFactory.
		 */
		public function LoggerFactory() {
		}
		
		/**
		 * Returns a logger for the given name.
		 */
		public static function getLogger(name:String):ILogger {
			return _instance.getLogger(name);
		}
		
		/**
		 * Returns a logger for the given class, using the fully qualified name of the class as the name of the
		 * logger.
		 */
		public static function getClassLogger(clazz:Class):ILogger {
			// replace the colons (::) in the name since this is not allowed in the Flex logging API
			var name:String = getQualifiedClassName(clazz);
			name = name.replace("::", ".");
			return _instance.getLogger(name);
		}
		
		/**
		 * Sets the logger factory for the logging system.
		 */
		public static function set loggerFactory(value:ILoggerFactory):void {
			_instance.loggerFactory = value;
		}
		
		/**
		 * Returns the singleton instance of the logger factory.
		 */
		private static function getInstance():LoggerFactory {
			if (!_instance) {
				_instance = new LoggerFactory();
			}
			return _instance;
		}
		
		/**
		 * Returns a logger for the given name.
		 *
		 * @param name the name of the logger
		 * @return a logger with the given name
		 */
		public function getLogger(name:String):ILogger {
			var result:ILogger = _loggers[name];
			
			if (!result) {
				if (_loggerFactory) {
					result = new LoggerProxy(name, _loggerFactory.getLogger(name));
				} else {
					result = new LoggerProxy(name);
				}
				
				// cache the logger
				_loggers[name] = result;
			}
			
			return result;
		}
		
		/**
		 * Sets the factory used to generate loggers. A new logger, created by the given factory, will
		 * be set on each LoggerProxy in the cache.
		 *
		 * @param value the new logger factory or null
		 */
		private function set loggerFactory(value:ILoggerFactory):void {
			_loggerFactory = value;
			
			for (var name:String in _loggers) {
				LoggerProxy(_loggers[name]).logger = (_loggerFactory ? _loggerFactory.getLogger(name) : null);
			}
		}
	
	}
}