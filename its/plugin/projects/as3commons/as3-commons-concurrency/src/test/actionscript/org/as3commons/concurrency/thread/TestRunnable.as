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
package org.as3commons.concurrency.thread  {

	import org.as3commons.concurrency.thread.IRunnable;
	

	public class TestRunnable implements IRunnable {
		
		// the number we are incrementing
		private var counter:int = 0;
		
		// the total we are trying to achive
		private var totalToReach:int = 100;
		
		// constructor, we don't need to do anything here
		public function TestRunnable() {
			// nothing to do here
		}
		
		/* this is called when the PseudoThread
		   completes, encounters an error, stops
		   or reaches its timeout.  */
		public function cleanup():void {
			counter = 0;
		}

		/* This is called by PseudoThread 
		   repeatedly N times, until the isComplete() method
		   below returns true, PseudoThread stops, an 
		   error occurs, or the thread timeout is reached */
		public function process():void {
			// our implemention's "work" is just to 
			// increment our counter
			counter++;
		}
		
		/* We are "complete" when our counter = total
		   If your IRunnable is just a permanent background
		   job and never has a "finish" point, you can just
		   return false to this method to ensure your process()
		   method runs forever. If you take that route, keep in mind that you must
		   make sure that the PseudoThread you create to process
		   this IRunnable has no timeout!
		*/
		public function isComplete():Boolean {
			return counter == totalToReach;
		}
		
		/* PseudoThread consults this method
		   after each call to process(). In order
		   to dispatch ProgressEvents. The value 
		   here is set as the bytesTotal propery of
		   the ProgressEvent that PseudoThread dispatches */
		public function getTotal():int {
			return this.totalToReach;
		}
		
		/* PseudoThread consults this method
		   after each call to process(). The value
		   returned here is set as the bytesLoaded
		   value in the ProgressEvent that is dispatched
		   by PseudoThread. */
		public function getProgress():int {
			return this.counter;
		}
		
	}
}