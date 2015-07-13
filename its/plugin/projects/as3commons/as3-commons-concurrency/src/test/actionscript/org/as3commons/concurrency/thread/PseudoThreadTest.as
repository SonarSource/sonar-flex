/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.as3commons.concurrency.thread {
	
	import flexunit.framework.TestCase;
	import flash.events.ErrorEvent;
	import flash.events.Event;
	import flash.utils.setTimeout;
	
	/**
	 * @author bitsofinfo
	 */
	public class PseudoThreadTest extends TestCase {
		
		public function PseudoThreadTest(methodName:String = null) {
			super(methodName);
		}
		
		public function testRunnable():void {
			var runnable:TestRunnable = new TestRunnable();
			var thread:PseudoThread = new PseudoThread(runnable,"testRunnable",10);
			thread.addEventListener(Event.COMPLETE,addAsync(function():void {
				assertEquals(runnable.isComplete(),true);
				assertEquals(100,runnable.getProgress());
			},10000));
			
			// register for error even though we should not get it
			thread.addEventListener(ErrorEvent.ERROR,function(e:ErrorEvent):void {
				assertTrue(false);// we should NOT get an error event...
			});
			
			assertEquals(0,runnable.getProgress());
			thread.start();
		}
		
		public function testStopAndCleanup():void {
			var runnable:TestRunnable = new TestRunnable();
			
			// should take ~10 seconds, one counter increment every 100ms
			var thread:PseudoThread = new PseudoThread(runnable,"testRunnable",100); 
			
			thread.addEventListener(Event.COMPLETE,addAsync(function():void {
				// since we are dealing with varying cpu speeds and 
				// timing issues .. the progress should be somewhere between 3 and 7
				// and the runnable should not be complete, as we stop() it after 500ms (below)
				assertEquals(false,runnable.isComplete());
				var progress:int = runnable.getProgress();
				assertEquals(true,(progress > 3));
				assertEquals(true,(progress < 7));
			},10000));
			
			// register for error even though we should not get it
			thread.addEventListener(ErrorEvent.ERROR,function(e:ErrorEvent):void {
				assertTrue(false);// we should NOT get an error event...
			});
			
			thread.start();
			
			// stop after 500ms
			flash.utils.setTimeout(function():void {
				thread.stop();
			},500);
			
		}
		
		
		public function testTimeout():void {
			var runnable:TestRunnable = new TestRunnable();
			
			// should take ~10 seconds, one counter increment every 100ms, timeout throws error @ 2 seconds
			var thread:PseudoThread = new PseudoThread(runnable,"testRunnable",100,2000); 
			
			thread.addEventListener(ErrorEvent.ERROR,addAsync(function():void {
				// sometime after 2 seconds this code should run
				// progress should be somewhere between 17 and 23, due to 
				// same variable timing reasons as the above test
				assertEquals(false,runnable.isComplete());
				var progress:int = runnable.getProgress();
				assertEquals(true,(progress > 17));
				assertEquals(true,(progress < 23));
			},10000));
			
			// register for COMPLETE even though we should not get it
			thread.addEventListener(Event.COMPLETE,function(e:Event):void {
				assertTrue(false);// we should NOT get an complete event...the timeout will occur first
			});
			
			thread.start();
			
			
		}
	}
}