package org.as3commons.logging.impl {
	
	import flash.errors.IllegalOperationError;
	
	import org.as3commons.logging.ILogger;
	import org.as3commons.logging.LogLevel;
	
	/**
	 * Abstract base class for ILogger implementations.
	 *
	 * @author Christophe Herreman
	 */
	public class AbstractLogger implements ILogger {
		
		protected var name:String;
		
		/**
		 * Creates a new AbstractLogger
		 */
		public function AbstractLogger(name:String = "") {
			this.name = name;
		}
		
		/**
		 * @inheritDoc
		 */
		public function debug(message:String, ... params):void {
			log(LogLevel.DEBUG, message, params);
		}
		
		/**
		 * @inheritDoc
		 */
		public function info(message:String, ... params):void {
			log(LogLevel.INFO, message, params);
		}
		
		/**
		 * @inheritDoc
		 */
		public function warn(message:String, ... params):void {
			log(LogLevel.WARN, message, params);
		}
		
		/**
		 * @inheritDoc
		 */
		public function error(message:String, ... params):void {
			log(LogLevel.ERROR, message, params);
		}
		
		/**
		 * @inheritDoc
		 */
		public function fatal(message:String, ... params):void {
			log(LogLevel.FATAL, message, params);
		}
		
		/**
		 * @inheritDoc
		 */
		public function get debugEnabled():Boolean {
			return true;
		}
		
		/**
		 * @inheritDoc
		 */
		public function get infoEnabled():Boolean {
			return true;
		}
		
		/**
		 * @inheritDoc
		 */
		public function get warnEnabled():Boolean {
			return true;
		}
		
		/**
		 * @inheritDoc
		 */
		public function get errorEnabled():Boolean {
			return true;
		}
		
		/**
		 * @inheritDoc
		 */
		public function get fatalEnabled():Boolean {
			return true;
		}
		
		/**
		 * Subclasses must override this method and provide a concrete log implementation.
		 */
		protected function log(level:uint, message:String, params:Array):void {
			throw new IllegalOperationError("The 'log' method is abstract and must be overridden in '" + this + "'");
		}
	}
}