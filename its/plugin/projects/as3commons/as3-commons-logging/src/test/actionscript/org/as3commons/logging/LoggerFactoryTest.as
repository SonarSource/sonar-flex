package org.as3commons.logging {
	
	import flexunit.framework.Assert;
	import flexunit.framework.TestCase;
	
	public class LoggerFactoryTest extends TestCase {
		
		public function LoggerFactoryTest() {
		}
		
		// --------------------------------------------------------------------
		//
		// LoggerFactory.getClassLogger()
		//
		// --------------------------------------------------------------------
		
		/**
		 *
		 */
		public function testGetClassLogger():void {
			var logger:ILogger = LoggerFactory.getClassLogger(LoggerFactoryTest);
			assertNotNull(logger);
		}
	}
}