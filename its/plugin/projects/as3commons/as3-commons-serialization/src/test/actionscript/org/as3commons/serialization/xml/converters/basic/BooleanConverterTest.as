package org.as3commons.serialization.xml.converters.basic {

	import flexunit.framework.TestCase;

	import org.as3commons.serialization.xml.converters.IConverter;

	/**
	 * @author Christophe Herreman
	 */
	public class BooleanConverterTest extends TestCase {

		private var converter:IConverter;

		public function BooleanConverterTest() {
			super();
		}

		// --------------------------------------------------------------------
		//
		// Setup
		//
		// --------------------------------------------------------------------

		override public function setUp():void {
			converter = new BooleanConverter();
		}

		// --------------------------------------------------------------------
		//
		// Tests: canConvert
		//
		// --------------------------------------------------------------------

		public function testCanConvert_shouldReturnTrueWhenGivenBooleanClass():void {
			assertTrue(converter.canConvert(Boolean));
		}

		public function testCanConvert_shouldReturnFalseWhenGivenNull():void {
			assertFalse(converter.canConvert(null));
		}

		public function testCanConvert_shouldReturnFalseWhenGivenNonBooleanClass():void {
			assertFalse(converter.canConvert(String));
			assertFalse(converter.canConvert(int));
			assertFalse(converter.canConvert(uint));
			assertFalse(converter.canConvert(Number));
			assertFalse(converter.canConvert(Array));
			assertFalse(converter.canConvert(Date));
		}

		// --------------------------------------------------------------------
		//
		// Tests: fromXML
		//
		// --------------------------------------------------------------------

		public function testFromXML_shouldReturnTrueWhenGivenTrueAsString():void {
			var value:Boolean = Boolean(converter.fromXML(<node>true</node>, null));
			assertTrue(value);
		}

		public function testFromXML_shouldReturnFalseWhenGivenFalseAsString():void {
			var value:Boolean = Boolean(converter.fromXML(<node>false</node>, null));
			assertFalse(value);
		}

		public function testFromXML_shouldReturnFalseWhenGivenNonBooleanString():void {
			var value:Boolean = Boolean(converter.fromXML(<node>aStringValue</node>, null));
			assertFalse(value);
		}
	}
}