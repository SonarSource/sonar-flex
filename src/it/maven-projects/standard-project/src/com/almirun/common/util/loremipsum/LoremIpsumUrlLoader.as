package com.almirun.common.util.loremipsum {
	import com.almirun.common.net.SmartUrlLoader;
	
	import flash.net.URLRequest;

	/**
	 * @author JoshM
	 */
	public class LoremIpsumUrlLoader extends SmartUrlLoader {
		
		private var _loremType:String;
		public function get loremType():String {
			return _loremType;
		}
		
		private var _loremQty:int;
		public function get loremQty():int {
			return _loremQty;
		}
		
		public function LoremIpsumUrlLoader(req:URLRequest, loremType:String,
				loremQty:int) {
			super(req);
			_loremType = loremType;
			_loremQty = loremQty;
		}
	}
}
