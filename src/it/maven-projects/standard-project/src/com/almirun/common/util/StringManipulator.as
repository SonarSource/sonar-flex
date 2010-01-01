package com.almirun.common.util {

	/**
	 * Class containing static utility methods for dealing with strings.
	 * @author JoshM
	 */
	public class StringManipulator {
		
		public static function addSlashes(str:String):String {
			return str.replace(new RegExp("['\"\\\x00]", "g"), "\$&");
		}
		
		public static function chop(str:String):String {
			return rtrim(str);
		}
		
		/**
		 * Split a string into smaller chunks
		 * 
		 * <p>Can be used to split a string into smaller chunks which is useful
		 * for e.g. converting base64_encode() output to match RFC 2045
		 * semantics. It inserts <em>end</em> every <em>chunklen</em> 
		 * characters.</p>
		 * @param	body	The string to be chunked
		 * @param	chunklen	The chunk length. Default to 76.
		 * @param	end	The line ending sequence. Defaults to "\r\n". 	
		 */
		public static function chunkSplit(body:String, chunkLength:int = 76,
				end:String = "\r\n"):String {
			var chunked:String;
			
			for (var i:int = 0; i < body.length; i += chunkLength) {
				chunked += body.substr(i, chunkLength) + end;
			}
			
			return chunked;				
		}
		
		/**
		 * Return an array containing totals of each character used in a string
		 * 
		 * <p>Counts the number of occurrences of every character, and returns
		 * them as an array. Note that this is the same as the PHP function
		 * count_chars() with the default mode 0 - modes are not
		 * implemented.</p>
		 * @param	str	The string to inspect for character counting
		 * @return	An associative array in which the keys are the characters
		 * found, and the values are the number of times in which each character
		 * was used.
		 */
		public static function countChars(str:String):Array {
			var chars:Array = [];
			var char:String;
			
			for (var i:int = 0; i < str.length; i++) {
				char = str.substr(i, 1);
				
				if (null == chars[char]) {
					chars[char] = 0;
				}
				
				chars[char]++;
			}
			
			return chars;
		}
	
		/**
		 * Return an array containing totals of each word used in a string
		 * 
		 * <p>Counts the number of occurrences of every word, and returns
		 * them as an array. Inspired by PHP's count_chars(), except for words
		 * rather than characters. I think this could be useful for writers
		 * to make sure they don't use the same words over and over.</p>
		 * 
		 * <p>Note: a word is defined as any sequence of characters bounded by
		 * regexp's \w.</p>
		 * @param	str	The string to inspect for word counting
		 * @return	An associative array in which the keys are the words
		 * found, and the values are the number of times in which each word
		 * was used.
		 */
		public static function countWords(str:String):Array {
			var words:Array = [];
			var word:String;
			var allWords:Array = str.split(/\w/g);
			
			for (var i:int = 0; i < allWords.length; i++) {
				word = allWords[i];
				
				if (null == words[word]) {
					words[word] = 0;
				}
				
				words[word]++;
			}
			
			return words;
		}
		
		public static function sprintf(format:String, ...args):String {
			return vsprintf(format, args);
		}
		
		public static function isSpace(chr:String):Boolean {
			return " " == chr
				|| "\t" == chr
				|| "\n" == chr
				|| "\r" == chr;
		}

		public static function ltrim(str:String):String {
			while (str.length > 0 && isSpace(str.charAt(0))) str = str.substr(1);
			return str; 
		}

		public static function rtrim(str:String):String {
			while (str.length > 0 && isSpace(str.charAt(str.length - 1)))
				str = str.substr(0, str.length - 1);
			return str;
		}
		
		public static function trim(str:String):String {
			return ltrim(rtrim(str));
		}
		
		public static function vsprintf(format:String, args:Array):String {
			args;
			return format;
		}
		
		/**
		 * Adds zeroes in front of a number 
		 * @param	num	The number to be zero-filled.
		 * @param	decimalPlaces	The minimum number of characters in the 
		 * returned number-String.
		 * @return	A string representation of the number, with zeroes added
		 * to the front as padding.
		 */
		public static function zerofill(num:int,
				decimalPlaces:int):String {
			var numStr:String = num.toString();
			
			while (numStr.length < decimalPlaces) {
				numStr = "0" + numStr;
			}
			
			return numStr;
		}
	}
}
