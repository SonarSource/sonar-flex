package com.almirun.common.data {
	import flash.data.SQLConnection;
	import flash.data.SQLStatement;
	import flash.filesystem.File;
	import flash.filesystem.FileMode;
	import flash.filesystem.FileStream;
	
	import com.almirun.common.util.StringManipulator;

	/**
	 * Encapsulates the command to run a batch of SQL statements
	 * 
	 * <p>Typically this will be a set of INSERT statements, perhaps with
	 * a CREATE TABLE statement.</p>
	 * @author JoshM
	 */
	public class BatchedQuery {
		
		private var uri:String;
		private var conn:SQLConnection;
		
		/**
		 * Easy way to execute a batch of SQL statements from a file
		 * 
		 * @param	fileUri	The location of the file containing the SQL
		 * statements
		 * @param	connection	The connection to the Sqlite database. Should
		 * be an open connection in SQLMode.UPDATE mode. Will probably work
		 * in asynchronous mode, although I've only tested in synchronous mode.   
		 */
		public function BatchedQuery(fileUri:String, connection:SQLConnection) {
			uri = fileUri;
			conn = connection;
		}
		
		public function execute():void {
			var stream:FileStream = new FileStream();
			stream.open(new File(uri), FileMode.READ);
			runMultipleQueryString(stream.readUTFBytes(stream.bytesAvailable));
		}
		
		private function runMultipleQueryString(sql:String):void {
			var queries:Array = sql.split(";");
			var query:String;
			
			for (var i:Number = 0; i < queries.length; i++) {
				if ("" == StringManipulator.trim(queries[i])) {
					continue;
				}
				
				query = queries[i] + ";";
				runQueryString(query);
			}
		}
		
		private function runQueryString(sql:String):SQLStatement {
			var statement:SQLStatement = new SQLStatement();
			statement.sqlConnection = conn;
			statement.text = sql;
			statement.execute();
			return statement;
		}
	}
}
