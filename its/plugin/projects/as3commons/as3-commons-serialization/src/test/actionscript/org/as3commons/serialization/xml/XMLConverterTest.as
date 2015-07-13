package org.as3commons.serialization.xml {

	import flexunit.framework.TestCase;

	import org.as3commons.serialization.testclasses.PersonWithPublicVariables;

	/**
	 * @author Christophe Herreman
	 */
	public class XMLConverterTest extends TestCase {

		private var converter:XMLConverter;

		public function XMLConverterTest() {
			super();
		}

		// --------------------------------------------------------------------
		//
		// Setup
		//
		// --------------------------------------------------------------------

		override public function setUp():void {
			converter = new XMLConverter();
		}

		// --------------------------------------------------------------------
		//
		// Tests: xmlFromObject
		//
		// --------------------------------------------------------------------

		public function testXmlFromObject():void {
			var person:PersonWithPublicVariables = new PersonWithPublicVariables();
			var xml:XML = converter.toXML(person);
			assertNotNull(xml);
			//assertEquals("org.as3commons.serialization.testclasses.PersonWithPublicVariables", xml.localName());
			assertTrue(xml.firstName.length() == 0);
			assertTrue(xml.lastName.length() == 0);
			assertTrue(xml.age.length() == 1);
			assertTrue(xml.isMarried.length() == 1);
		}
	}
}