package org.as3commons.reflect.testclasses {
	
	
	/**
	 * Class description
	 *
	 * @author Christophe
	 * @since 13-jan-2009
	 */
	[ClassMetaData]
	[ClassMetaData(key="value", key1="value1")]
	[ClassMetaData(key="value2", key1="value3")]
	public class MetaDataClass {
		
		[VarMetaData]
		[VarMetaData(key="value", key1="value1")]
		[VarMetaData(key="value2", key1="value3")]
		public var aVariable:String = "";
		
		/**
		 * Creates a new MetaDataClass object.
		 */
		public function MetaDataClass()
		{
		}
		
	}
}