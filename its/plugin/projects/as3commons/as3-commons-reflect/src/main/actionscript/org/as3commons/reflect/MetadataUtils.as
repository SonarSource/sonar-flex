/*
 * Copyright (c) 2007-2009 the original author or authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.as3commons.reflect {

  import flash.events.TimerEvent;
  import flash.system.ApplicationDomain;
  import flash.utils.Timer;
  import flash.utils.describeType;
  import flash.utils.getQualifiedClassName;
  
  import org.as3commons.lang.ClassUtils;

  /**
   * This class provides utility methods concerning metadata. Retrieved metadata is cached and cleared with the
   * set interval.
   *
   * @author Erik Westra, Christophe Herreman
   *
   * @see #CLEAR_CACHE_INTERVAL
   */
  public class MetadataUtils {

    /**
     * The interval (in miliseconds) at which the cache will be cleared. Note that this value is only used
     * on the first call to getFromObject.
     *
     * @default 60000 (one minute)
     */
    static public var CLEAR_CACHE_INTERVAL:uint = 60000;

    static private var _cache:Object = new Object();
    static private var _timer:Timer;

    static private function _timerHandler(e:TimerEvent):void {
      clearCache();
    }

    /**
     * Will return the metadata for the given object or class. If metadata has already been requested for
     * this type, it will be retrieved from cache. Note that the metadata will allways be that of the class,
     * even if you pass an instance.
     * <p />
     * In order to get instance specific metadata, use the 'factory' property.
     * <p />
     * The reason we do not allow retrieval of instance metadata is because then we would need to cache the
     * metadata double. Metadata takes up a significant amount of memory.
     *
     * @param object  The object from which you want to grab the metadata
     *
     * @return The class metadata of the given object.
     */
    static public function getFromObject(object:Object):XML {
      var className:String = getQualifiedClassName(object);
      var metadata:XML;

      if (_cache.hasOwnProperty(className)) {
        metadata = _cache[className];
      }
      else {
        if (!_timer) {
          /*
            Only run the timer once to prevent unneeded overhead. This also prevents
            this class from falling for the bug described here:

            http://www.gskinner.com/blog/archives/2008/04/failure_to_unlo.html
          */
          _timer = new Timer(CLEAR_CACHE_INTERVAL, 1);
          _timer.addEventListener(TimerEvent.TIMER, _timerHandler);
        }

        if (!(object is Class)) {
          object = object.constructor;
        }

        metadata = describeType(object);

        _cache[className] = metadata;

        if (!_timer.running) {
          /*
            Only run the timer if it is not already running.
          */
          _timer.start();
        }
      }

      return metadata;
    }

    /**
     * Will retrieve the metadata for the given class. Note that in order to access properties and
     * methods you need to grab the 'factory' part of the metadata.
     *
     * @param className    The name of the class that you want to retrieve metadata from. The className
     *             may be in the following forms: package.Class or package::Class
     */
    static public function getFromString(className:String, applicationDomain:ApplicationDomain=null):XML {
		applicationDomain = (applicationDomain == null) ? ApplicationDomain.currentDomain : applicationDomain;
		var classDefinition:Class = org.as3commons.lang.ClassUtils.forName(className, applicationDomain);

      /*
        Calling getFromObject seems double, as it results in the getObjectMethod getting
        the class name using getQualifiedClassName. It however saves us a check on the
        given className which might be in two forms.

        getQualifiedClassName(getDefinitionByName(className)) is faster then converting the
        string using conventional methods.
      */
      return getFromObject(classDefinition);
    }

    /**
     * Allows you to clear the internal cache.
     */
    static public function clearCache():void {
      _cache = new Object();

      if (_timer && _timer.running) {
        _timer.stop();
      }
    }
  }
}
