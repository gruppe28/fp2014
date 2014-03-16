package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/* 
How to run a database query:
	1. Create a new Database object
	2. Run query through update() or query()
	3. Close connection with close()
*/

public class Database {
	private Connection connect = null;

	public Database(){
		try {
			//Load driver and connect
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/audunlib_calendar", "audunlib_group28", "gruppe28allerallerbest");
		}
		catch (ClassNotFoundException | SQLException e) {  } // Does not print exceptions. Error handling is taken care of at login
	}
 
	// Call this method to execute queries without output (insert, update, etc.)
	public void update(String sql){
		try {
			Statement st = connect.createStatement();
			st.executeUpdate(sql);
		}
		catch (SQLException e) { e.printStackTrace(); }
	}
	
	// Call this method to execute queries with output
	public ResultSet query(String sql){
		try {
			Statement st = connect.createStatement();
			return st.executeQuery(sql);
			}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// After a query, close connection by calling this
	public void close() {
		try { connect.close(); }
		catch (Exception e) { e.printStackTrace(); }
	}
}