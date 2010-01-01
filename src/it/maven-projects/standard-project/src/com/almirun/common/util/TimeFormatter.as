package com.almirun.common.util {
	
	import com.almirun.common.util.StringManipulator;
	
	/**
	 * A thin OO wrapper for an AS3 version of the C function strftime().
	 */
	public class TimeFormatter {
		
		/**
		 * Convert date and time to a string
		 * 
		 * <p>The formats allowed follow the C-style syntax of strftime(). See
		 * http://www.opengroup.org/onlinepubs/007908799/xsh/strftime.html
		 * for details. Note: not all formats are currently implemented, I have
		 * only done those ones that were easy to implement using the Date
		 * object.</p>
		 * 
		 * <p>Each conversion specification in the format string is represented 
		 * by a percent character "%" followed by a specific character as per 
		 * the following:</p>
		 * 
		 * <p> %a is replaced by the abbreviated weekday name (Mon, Tue, etc)</p>
		 * 
		 * <p> %A is replaced by the full weekday name.</p>
		 * 
		 * <p> %b is replaced by the abbreviated month name.</p>
		 * 
		 * <p> %B is replaced by the full month name.</p>
		 * 
		 * <p> %c is replaced by the default Date.toString() value.</p> 
		 * 
		 * <p> %C is replaced by the century number (the year divided by 100 and
		 * truncated to an integer) as a decimal number [00-99].</p>
		 * 
		 * <p> %d is replaced by the day of the month as a decimal number
		 * [01-31].</p>
		 * 
		 * <p> %D is the same as %d/%m/%y (deviation from standard).</p>
		 * 
		 * <p> %e is replaced by the day of the month as a decimal number [1-31];
		 * a single digit is preceded by a space.</p>
		 * 
		 * <p> %e is replaced by the day of the month as a decimal number [1-31];
		 * no leading zero or space character (non-standard addition).</p>
		 * 
		 * <p> %f gives the ordinal suffix (st, nd, rd, th) appropriate to the
		 * day of the month (non-standard addition).</p>
		 * 
		 * <p> %h is the same as %b. 
		 * 
		 * <p> %H is replaced by the hour (24-hour clock) as a decimal number
		 * [00-23].</p>
		 * 
		 * <p> %I is replaced by the hour (12-hour clock) as a decimal number
		 * [1-12].</p>
		 * 
		 * <p> %j is replaced by the day of the year as a decimal number
		 * [001-366].</p>
		 * 
		 * <p> %m is replaced by the month as a decimal number [01-12].</p>
		 * 
		 * <p> %M is replaced by the minute as a decimal number [00-59].</p> 
		 * 
		 * <p> %n is replaced by a newline character.</p> 
		 * 
		 * <p> %p is replaced by either a.m. or p.m.</p>
		 * 
		 * <p> %P is replaced by either am or pm (non-standard addition)</p>
		 * 
		 * <p> %r is equivalent to %I:%M:%S %p.</p>
		 * 
		 * <p> %R is replaced by the time in 24 hour notation (%H:%M).</p>
		 * 
		 * <p> %S is replaced by the second as a decimal number [00-61].</p>
		 * 
		 * <p> %t is replaced by a tab character.</p>
		 * 
		 * <p> %T is replaced by the time (%H:%M:%S).</p>
		 * 
		 * <p> %u is replaced by the weekday as a decimal number [1-7],
		 * with 1 representing Monday.</p>
		 * 
		 * <p> %U is not implemented.</p>
		 * 
		 * <p> %V is not implemented.</p>
		 * 
		 * <p> %w is replaced by the weekday as a decimal number [0-6],
		 * with 0 representing Sunday.</p>
		 * 
		 * <p> %W is not implemented.</p>
		 * 
		 * <p> %x is not implemented.</p>
		 * 
		 * <p> %X is not implemented.</p>
		 * 
		 * <p> %y is replaced by the year without century as a decimal number
		 * [00-99].</p>
		 * 
		 * <p> %Y is replaced by the year with century as a decimal number.</p>
		 * 
		 * <p> %Z is replaced by the timezone name or abbreviation, or by no bytes
		 * if no timezone information exists.</p>
		 * 
		 * <p> %% is replaced by literal %.</p>
		 * 
		 * @param format The format to use.
		 * @param date The date to format.
		 */
		public static function formatTime(format:String, date:Date):String {
			var output:String = "";
			var char:String;
			var i:int = 0;
			
			while (i < format.length) {
				char = format.charAt(i++);
				
				if ("%" == char) {
					output += formatChar(format.charAt(i++), date); 
				}
				else {
					output += char;
				}
			}
			
			return output;
		}
		
		private static function formatChar(char:String, date:Date):String {
			var output:String;
			
			if (char.length != 1) {
				throw new ArgumentError("formatChar() must be supplied with" +
					" a string exactly one character in length");
			}
			
			if ("%" == char) {
				output = "%";
			}
			else if (TimeFormatter[char]) {
				output = TimeFormatter[char](date);
			}
			else {
				throw new ArgumentError("An invalid conversion specification, "
					+ "%" + char + ", was given");
			}
			
			return output;
		}
		
		private static var abbrDays:Array = ["Mon", "Tue", "Wed", "Thur", "Fri",
			"Sat", "Sun"];
	
		private static var fullDays:Array = ["Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday"];
	
		private static var abbrMonths:Array = ["Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
	
		private static var fullMonths:Array = ["January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December"];
		
		private static function a (date:Date):String {
			return abbrDays[date.getDay() - 1];
		}
		
		private static function A (date:Date):String {
			return TimeFormatter.fullDays[date.getDay() - 1];
		}
		
		private static function b (date:Date):String {
			return TimeFormatter.abbrMonths[date.getMonth()];
		}
		
		private static function B (date:Date):String {
			return TimeFormatter.fullMonths[date.getMonth()];
		}
		
		private static function c (date:Date):String {
			return date.toString();
		}
		
		private static function C (date:Date):String {
			return Math.floor(date.getFullYear() / 100).toString();
		}
		
		private static function d (date:Date):String {
			return StringManipulator.zerofill(date.getDate(), 2);
		}
					
		private static function D (date:Date):String {
			return formatTime("%d/%m/%y", date);
		}
						
		private static function e (date:Date):String {
			return d(date).split("0").join(" ");
		}
		
		private static function E (date:Date):String {
			return date.getDate().toString();
		}
		
		private static function f (date:Date):String {
			var ordinalSuffix:String;
			var lastDigit:int = date.getDate() % 10;
			
			switch(lastDigit) {
			case 1:
				ordinalSuffix = "st";
				break;
			
			case 2:
				ordinalSuffix = "nd";
				break;
			
			case 3:
				ordinalSuffix = "rd";
				break;
			
			default:
				ordinalSuffix = "th";
				break;
			}
			
			// special case for 11 and 12
			if (date.getDate() == 11 || date.getDate() == 12) {
				ordinalSuffix = "th";
			}
			
			return ordinalSuffix;
		}
	
		private static function h (date:Date):String {
			return b(date);
		}
				
		private static function H (date:Date):String {
			return StringManipulator.zerofill(date.getHours(), 2);
		}
				
		private static function I (date:Date):String {
			var hours:int = date.getHours() % 12;
			return (hours == 0 ? 12 : hours).toString();
		}
		
		private static function j (...ignore):String {
			return null;		// not implemented
		}
				
		private static function m (date:Date):String {
			return StringManipulator.zerofill(date.getMonth() + 1, 2);
		}
				
		private static function M (date:Date):String {
			return StringManipulator.zerofill(date.getMinutes(), 2);
		}
				
		private static function n (...ignore):String {
			return "\n";
		}
				
		private static function p (date:Date):String {
			return date.getHours() >= 12 ? "p.m." : "a.m.";
		}
				
		private static function P (date:Date):String {
			return date.getHours() >= 12 ? "pm" : "am";
		}
				
		private static function r (date:Date):String {
			return formatTime("%I:%M:%S %p", date);
		}
				
		private static function R (date:Date):String {
			return formatTime("%H:%M", date);
		}
				
		private static function S (date:Date):String {
			return StringManipulator.zerofill(date.getSeconds(), 2);
		}
				
		private static function t (...ignore):String {
			return "\t";
		}
				
		private static function T (date:Date):String {
			return formatTime("%H:%M:%S", date);
		}
				
		private static function u (date:Date):String {
			return (date.getDay() == 0 ? 7 : date.getDay()).toString();
		}
		
		private static function U (...ignore):String {
			return null;		// not implemented
		}
		
		private static function V (...ignore):String {
			return null;		// not implemented
		}
				
		private static function w (date:Date):String {
			return date.getDay().toString();
		}
		
		private static function W (...ignore):String {
			return null;		// not implemented
		}
		
		private static function x (...ignore):String {
			return null;		// not implemented
		}
		
		private static function X (...ignore):String {
			return null;		// not implemented
		}
				
		private static function y (date:Date):String {
			return StringManipulator.zerofill(date.getFullYear() % 100, 2);
		}
				
		private static function Y (date:Date):String {
			return date.getFullYear().toString();
		}
				
		private static function Z (date:Date):String {
			var tz:Number = date.getTimezoneOffset();
			return "GMT" + (tz > 1 ? "+" : "") + tz.toString();
		}
	}
}
