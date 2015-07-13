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
	
	/**
	 * An IRunnable is to be used with PseudoThread which
	 * will call an IRunnable's process() method repeatedly
	 * until a timeout is reached or the isComplete() method returns
	 * true. An IRunnable should reflect its current progress
	 * by returning appropriate values from the getTotal()
	 * and getProgress() methods, which will be consulted
	 * by PseudoThread when it dispatches the thread's
	 * progress events. Finally an IRunnable should do any necessary
	 * cleanup operations by implementing the cleanup() method.
	 * 
	 * @see org.as3commons.concurrency.thread.PseudoThread	 
	 * @author bitsofinfo
	 * 
	 * */
	public interface IRunnable {
		
		/**
		 * Called repeatedly by PseudoThread until
		 * a timeout is reached or isComplete() returns true.
		 * Implementors should implement their functioning
		 * code that does actual work within this method
		 * 
		 * */
		function process():void;
		
		/**
		 * Called by PseudoThread when the thread is destroyed
		 * either as a result from a call to the PseudoThread.stop() 
		 * method or from the completion of the thread's processing after
		 * IRunnable.isComplete() returns true.
		 * 
		 * */
		function cleanup():void;
		
		/**
		 * Called by PseudoThread after each successful call
		 * to process(). Once this returns true, the thread will
		 * stop.
		 *
		 * @return	boolean	true/false if the work is done and no further
		 * 			calls to process() should be made
		 * */
		function isComplete():Boolean;
		
		/**
		 * Returns an int which represents the total
		 * amount of "work" to be done. This is consulted
		 * by PseudoThread after each process() invocation
		 * when PseudoThread dispatches progress events.
		 * 
		 * @return	int	total amount of work to be done
		 * */
		function getTotal():int;
		
		/**
		 * Returns an int which represents the total amount
		 * of work processed so far out of the overall total
		 * returned by getTotal().This is consulted
		 * by PseudoThread after each process() invocation
		 * when PseudoThread dispatches progress events.
		 * 
		 * @return	int	total amount of work processed so far
		 * */
		function getProgress():int;
		
		
	}
	
}