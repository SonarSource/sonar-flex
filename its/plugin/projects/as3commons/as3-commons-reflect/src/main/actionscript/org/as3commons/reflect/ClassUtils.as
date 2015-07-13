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
	
	import flash.system.ApplicationDomain;
	
	import org.as3commons.lang.ClassUtils;
	import org.as3commons.logging.ILogger;
	import org.as3commons.logging.LoggerFactory;
	
	/**
	 * Provides utilities for working with <code>Class</code> objects.
	 *
	 * @author Christophe Herreman
	 */
	[Deprecated(replacement="org.as3commons.lang.ClassUtils", since="1.1")]
	public class ClassUtils {
		
		private static const PACKAGE_CLASS_SEPARATOR:String = "::";
		
		private static var logger:ILogger = LoggerFactory.getClassLogger(ClassUtils);
		
		/**
		 * Returns a <code>Class</code> object that corresponds with the given
		 * instance. If no corresponding class was found, a
		 * <code>ClassNotFoundError</code> will be thrown.
		 *
		 * @param instance the instance from which to return the class
		 * @param applicationDomain the optional applicationdomain where the instance's class resides
		 *
		 * @return the <code>Class</code> that corresponds with the given instance
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.forInstance()", since="1.1")]
		public static function forInstance(instance:*, applicationDomain:ApplicationDomain = null):Class {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.forInstance(instance, applicationDomain);
		}
		
		/**
		 * Returns a <code>Class</code> object that corresponds with the given
		 * name. If no correspoding class was found in the applicationdomain tree, a
		 * <code>ClassNotFoundError</code> will be thrown.
		 *
		 * @param name the name from which to return the class
		 * @param applicationDomain the optional applicationdomain where the instance's class resides
		 *
		 * @return the <code>Class</code> that corresponds with the given name
		 *
		 * @see org.springextensions.actionscript.errors.ClassNotFoundError
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.forName()", since="1.1")]
		public static function forName(name:String, applicationDomain:ApplicationDomain = null):Class {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.forName(name, applicationDomain);
		}
		
		/**
		 * Returns the name of the given class.
		 *
		 * @param clazz the class to get the name from
		 *
		 * @return the name of the class
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.getName()", since="1.1")]
		public static function getName(clazz:Class):String {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.getName(clazz);
		}
		
		/**
		 * Returns the name of the class or interface, based on the given fully
		 * qualified class or interface name.
		 *
		 * @param fullyQualifiedName the fully qualified name of the class or interface
		 *
		 * @return the name of the class or interface
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.getNameFromFullyQualifiedName()", since="1.1")]
		public static function getNameFromFullyQualifiedName(fullyQualifiedName:String):String {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.getNameFromFullyQualifiedName(fullyQualifiedName);
		}
		
		/**
		 * Returns the fully qualified name of the given class.
		 *
		 * @param clazz the class to get the name from
		 * @param replaceColons whether the double colons "::" should be replaced by a dot "."
		 *             the default is false
		 *
		 * @return the fully qualified name of the class
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.getFullyQualifiedName()", since="1.1")]
		public static function getFullyQualifiedName(clazz:Class, replaceColons:Boolean = false):String {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.getFullyQualifiedName(clazz, replaceColons);
		}
		
		/**
		 * Determines if the class or interface represented by the clazz1 parameter is either the same as, or is
		 * a superclass or superinterface of the clazz2 parameter. It returns true if so; otherwise it returns false.
		 *
		 * @return the boolean value indicating whether objects of the type clazz2 can be assigned to objects of clazz1
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.isAssignableFrom()", since="1.1")]
		public static function isAssignableFrom(clazz1:Class, clazz2:Class):Boolean {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.isAssignableFrom(clazz1, clazz2);
		}
		
		/**
		 * Returns whether the passed in Class object is a subclass of the
		 * passed in parent Class. To check if an interface extends another interface, use the isImplementationOf()
		 * method instead.
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.isSubclassOf()", since="1.1")]
		public static function isSubclassOf(clazz:Class, parentClass:Class):Boolean {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.isSubclassOf(clazz, parentClass);
		}
		
		/**
		 * Returns the class that the passed in clazz extends. If no super class
		 * was found, in case of Object, null is returned.
		 *
		 * @param clazz the class to get the super class from
		 *
		 * @returns the super class or null if no parent class was found
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.getSuperClass()", since="1.1")]
		public static function getSuperClass(clazz:Class):Class {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.getSuperClass(clazz);
		}
		
		/**
		 * Returns the name of the given class' superclass.
		 *
		 * @param clazz the class to get the name of its superclass' from
		 *
		 * @return the name of the class' superclass
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.getSuperClassName()", since="1.1")]
		public static function getSuperClassName(clazz:Class):String {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.getSuperClassName(clazz);
		}
		
		/**
		 * Returns the fully qualified name of the given class' superclass.
		 *
		 * @param clazz the class to get its superclass' name from
		 * @param replaceColons whether the double colons "::" should be replaced by a dot "."
		 *             the default is false
		 *
		 * @return the fully qualified name of the class' superclass
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.getFullyQualifiedSuperClassName()", since="1.1")]
		public static function getFullyQualifiedSuperClassName(clazz:Class, replaceColons:Boolean = false):String {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.getFullyQualifiedSuperClassName(clazz, replaceColons);
		}
		
		/**
		 * Returns whether the passed in <code>Class</code> object implements
		 * the given interface.
		 *
		 * @param clazz the class to check for an implemented interface
		 * @param interfaze the interface that the clazz argument should implement
		 *
		 * @return true if the clazz object implements the given interface; false if not
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.isImplementationOf()", since="1.1")]
		public static function isImplementationOf(clazz:Class, interfaze:Class):Boolean {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.isImplementationOf(clazz, interfaze);
		}
		
		/**
		 * Returns whether the passed in Class object is an interface.
		 *
		 * @param clazz the class to check
		 * @return true if the clazz is an interface; false if not
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.isInterface()", since="1.1")]
		public static function isInterface(clazz:Class):Boolean {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.isInterface(clazz);
		}
		
		/**
		 * Returns an array of all interface names that the given class implements.
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.getImplementedInterfaceNames()", since="1.1")]
		public static function getImplementedInterfaceNames(clazz:Class):Array {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.getImplementedInterfaceNames(clazz);
		}
		
		/**
		 * Returns an array of all fully qualified interface names that the
		 * given class implements.
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.getFullyQualifiedImplementedInterfaceNames()", since="1.1")]
		public static function getFullyQualifiedImplementedInterfaceNames(clazz:Class, replaceColons:Boolean = false):Array {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.getFullyQualifiedImplementedInterfaceNames(clazz, replaceColons);
		}
		
		/**
		 * Returns an array of all interface names that the given class implements.
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.getImplementedInterfaces()", since="1.1")]
		public static function getImplementedInterfaces(clazz:Class):Array {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.getImplementedInterfaces(clazz);
		}
		
		/**
		 * Creates an instance of the given class and passes the arguments to
		 * the constructor.
		 *
		 * TODO find a generic solution for this. Currently we support constructors
		 * with a maximum of 10 arguments.
		 *
		 * @param clazz the class from which an instance will be created
		 * @param args the arguments that need to be passed to the constructor
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.newInstance()", since="1.1")]
		public static function newInstance(clazz:Class, args:Array = null):* {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.newInstance(clazz, args);
		}
		
		/**
		 * Converts the double colon (::) in a fully qualified class name to a dot (.)
		 */
		[Deprecated(replacement="org.as3commons.lang.ClassUtils.convertFullyQualifiedName()", since="1.1")]
		public static function convertFullyQualifiedName(className:String):String {
			logDeprecation();
			return org.as3commons.lang.ClassUtils.convertFullyQualifiedName(className);
		}
		
		private static function logDeprecation():void {
			logger.warn("The class ClassUtils is deprecated in as3commons-reflect and is now available in as3commons-lang.");
		}
	}
}
