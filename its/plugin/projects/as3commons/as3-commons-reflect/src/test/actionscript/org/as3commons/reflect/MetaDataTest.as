package org.as3commons.reflect {
	
	import flexunit.framework.TestCase;
	
	/**
	 * @author Christophe Herreman
	 * @since 13-jan-2009
	 */
	public class MetaDataTest extends TestCase {
	
		public function MetaDataTest(methodName:String=null) {
			super(methodName);
		}
		
		public function testNew():void {
			var metaData:MetaData = new MetaData("key", [new MetaDataArgument("key", "value")]);
			assertEquals("key", metaData.name);
			assertNotNull(metaData.arguments);
			assertEquals(1, metaData.arguments.length);
		}
		
		public function testNew_shouldHaveEmptyArgumentsArrayIfNullArgumentsAreGiven():void {
			var metaData:MetaData = new MetaData("key", null);
			assertEquals("key", metaData.name);
			assertNotNull(metaData.arguments);
			assertEquals(0, metaData.arguments.length);
		}
		
	}
}