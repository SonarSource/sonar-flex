package com.almirun.common.net {
	import flash.net.URLLoader;
	import flash.net.URLRequest;

	/**
	 * A simple addition to URLLoader that remembers the URL it last loaded.
	 * 
	 * This allows an easy (but probably memory-inefficient) way of using
	 * multiple URLLoader instances to load lots of data without getting the
	 * returns mixed up. 
	 * @author Joshua Mostafa
	 */
	public class SmartUrlLoader extends URLLoader {
		
		protected var _request:URLRequest;
		public function get request():URLRequest {
			return _request;
		}

		public function SmartUrlLoader(req : URLRequest = null) {
			super(req);
			_request = req;
		}
		
		override public function load(req:URLRequest):void {
			super.load(req);
			_request = req;
		} 
	}
}