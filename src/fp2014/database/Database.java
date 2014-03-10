package fp2014.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import fp2014.Rom;

public class Database {
  private Connection connect = null;
  private Statement statement = null;
  private ResultSet rs = null;

  public void readDataBase() throws Exception {
    try {
      // This will load the MySQL driver, each DB has its own driver
      Class.forName("com.mysql.jdbc.Driver");
      // Setup the connection with the DB	
      

      connect = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/audunlib_calendar", "audunlib_group28", "gruppe28allerallerbest");
              	 
      
      System.out.println("jippi");
      // Statements allow to issue SQL queries to the database
      statement = connect.createStatement();
      // Result set get the result of the SQL query
      rs = statement.executeQuery("select * from Ansatt");
      writeResultSet(rs);
      ArrayList<Rom> rooms = loadRooms();
      
      for(int i = 0; i < rooms.size(); i++){
    	  System.out.println(rooms.get(i).getSted());
      }
     
      
    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }

  }

  private void writeResultSet(ResultSet resultSet) throws SQLException {
    // ResultSet is initially before the first data set
    while (resultSet.next()) {
      // It is possible to get the columns via name
      // also possible to get the columns via the column number
      // which starts at 1
      // e.g. resultSet.getSTring(2);
      String user = resultSet.getString("fornavn");
      System.out.println("Bruker: " + user);
    }
  }
  
  	public ArrayList<Rom> loadRooms() throws SQLException{
        rs = statement.executeQuery("select * from Rom");
        ArrayList<Rom> rooms = new ArrayList<Rom>();

        while (rs.next()) {
        	Rom newRoom = new Rom(rs.getInt("romNr"), rs.getString("sted"), rs.getInt("antPlasser"), rs.getString("Beskrivelse"));
        	rooms.add(newRoom);
        }
        
        return rooms;
  	}

	private void close() {
		try {
			if (rs != null) { rs.close(); }

			if (statement != null) { statement.close(); }

			if (connect != null) { connect.close(); }
		} catch (Exception e) { }
	}
	
	public static void main(String[] args) throws Exception {
	    Database dao = new Database();
    	System.out.println("waaaa");
		dao.readDataBase();
	}

}