package com.almirun.common.util.loremipsum {
	import com.almirun.common.events.LoremIpsumEvent;

	/**
	 * @author JoshM
	 */
	public class LoremIpsumUsageExample {
		
		private var generator:LoremIpsumGenerator;
		
		public function LoremIpsumUsageExample() {
			
			// instantiate the generator
			generator = new LoremIpsumGenerator();
			
			// listen for load events
			generator.addEventListener(LoremIpsumEvent.PARAGRAPHS_LOADED,
				handleLoremParagraphsEvent);
			
			// ask for some data - this time it returns null, and starts
			// asynchronous loading.
			// subsequent calls with the same argument will return cached
			// paragraphs as an array.
			generator.getParagraphs(5);
		}
		
		private function handleLoremParagraphsEvent(
				event:LoremIpsumEvent):void {
			
			// you could check here to make sure it's the amount you asked for,
			// say if you were poulating multiple fields. just check
			// event.howMany to see the quantity of paragraphs requested.
			
			trace("LoremIpsumEvent handled. I got:");
			
			// show the data
			trace(event.data.join("\n"));
			
			// Now look - the data is cached so getParagraphs() with same
			// argument will return the data instantly, this time.
			trace("Another call to getParagraphs() with same argument");
			trace(generator.getParagraphs(5).join("\n"));
		}
	}
}
