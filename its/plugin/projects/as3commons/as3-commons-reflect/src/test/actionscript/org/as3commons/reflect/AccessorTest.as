package org.as3commons.reflect {
	
	import flexunit.framework.TestCase;
	
	/**
	 * @author Christophe Herreman
	 */
	public class AccessorTest extends TestCase {
		
		public function AccessorTest(methodName:String = null) {
			super(methodName);
		}
		
		override public function setUp():void {
			super.setUp();
		}
		
		override public function tearDown():void {
			super.tearDown();
		}
		
		// --------------------------------------------------------------------
		//
		// readable
		//
		// --------------------------------------------------------------------
		
		public function testReadable_shouldReturnTrueForReadOnlyAccess():void {
			assertTrue(newAccessor(AccessorAccess.READ_ONLY).readable);
		}
		
		public function testReadable_shouldReturnTrueForReadWriteAccess():void {
			assertTrue(newAccessor(AccessorAccess.READ_WRITE).readable);
		}
		
		public function testReadable_shouldReturnFalseForWriteOnlyAccess():void {
			assertFalse(newAccessor(AccessorAccess.WRITE_ONLY).readable);
		}
		
		// --------------------------------------------------------------------
		//
		// writeable
		//
		// --------------------------------------------------------------------
		
		public function testWriteable_shouldReturnTrueForWriteOnlyAccess():void {
			assertTrue(newAccessor(AccessorAccess.WRITE_ONLY).writeable);
		}
		
		public function testWriteable_shouldReturnTrueForReadWriteAccess():void {
			assertTrue(newAccessor(AccessorAccess.READ_WRITE).writeable);
		}
		
		public function testWriteable_shouldReturnFalseForReadOnlyAccess():void {
			assertFalse(newAccessor(AccessorAccess.READ_ONLY).writeable);
		}
		
		// --------------------------------------------------------------------
		//
		// isReadable()
		//
		// --------------------------------------------------------------------
		
		public function testIsReadable_shouldReturnTrueForReadOnlyAccess():void {
			assertTrue(newAccessor(AccessorAccess.READ_ONLY).isReadable());
		}
		
		public function testIsReadable_shouldReturnTrueForReadWriteAccess():void {
			assertTrue(newAccessor(AccessorAccess.READ_WRITE).isReadable());
		}
		
		public function testIsReadable_shouldReturnFalseForWriteOnlyAccess():void {
			assertFalse(newAccessor(AccessorAccess.WRITE_ONLY).isReadable());
		}
		
		// --------------------------------------------------------------------
		//
		// isWriteable()
		//
		// --------------------------------------------------------------------
		
		public function testIsWriteable_shouldReturnTrueForWriteOnlyAccess():void {
			assertTrue(newAccessor(AccessorAccess.WRITE_ONLY).isWriteable());
		}
		
		public function testIsWriteable_shouldReturnTrueForReadWriteAccess():void {
			assertTrue(newAccessor(AccessorAccess.READ_WRITE).isWriteable());
		}
		
		public function testIsWriteable_shouldReturnFalseForReadOnlyAccess():void {
			assertFalse(newAccessor(AccessorAccess.READ_ONLY).isWriteable());
		}
		
		// --------------------------------------------------------------------
		//
		// Private Methods
		//
		// --------------------------------------------------------------------
		
		private function newAccessor(access:AccessorAccess):Accessor {
			return new Accessor("test", access, "test", "test", false);
		}
	}
}