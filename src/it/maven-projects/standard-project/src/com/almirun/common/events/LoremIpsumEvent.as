package com.almirun.common.events {
	import flash.events.Event;
	
	/**
	 * @author JoshM
	 */
	public class LoremIpsumEvent extends Event {
		
		public static const PARAGRAPHS_LOADED:String = "parasLoaded";
		public static const WORDS_LOADED:String = "parasLoaded";
		public static const BYTES_LOADED:String = "bytesLoaded";
		
		private var _howMany:int;
		public function get howMany():int {
			return _howMany;
		}
		
		private var _data:Array;
		public function get data():Array {
			return _data;
		}
		
		public function LoremIpsumEvent(type:String, data:Array, howMany:int) {
			super(type);
			_data = data;
			_howMany = howMany;
		}
	}
}
