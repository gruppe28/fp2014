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

	public Database() throws Exception {
		//Load driver and connect
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/audunlib_calendar", "audunlib_group28", "gruppe28allerallerbest");
		System.out.println("jippi");
	}
  
	public ArrayList<Rom> loadRooms() throws Exception{
		Database db = new Database();
        ResultSet r = db.query("select * from Rom");
        ArrayList<Rom> rooms = new ArrayList<Rom>();

        while (r.next()) {
        	Rom newRoom = new Rom(rs.getInt("romNr"), rs.getString("sted"), rs.getInt("antPlasser"), rs.getString("Beskrivelse"));
        	rooms.add(newRoom);
        }
        
        db.close();
        
        return rooms;
  	}
	
	public void close() {
		try {
			if (rs != null) { rs.close(); }

			if (statement != null) { statement.close(); }

			if (connect != null) { connect.close(); }
		} catch (Exception e) { }
	}
	
	public void update(String sql) throws SQLException {
		Statement st = connect.createStatement();
		st.executeUpdate(sql);
	}
	
	public ResultSet query(String sql) throws SQLException {
		Statement st = connect.createStatement();
		return st.executeQuery(sql);
	}
	
	public static void main(String[] args) throws Exception {
	    //Database db = new Database();
	    //db.singleQuery("insert into Rom(sted, antPlasser, beskrivelse) values ('stedet', 88, 'fint sted as')");
	    //ResultSet r = db.query("select * from Rom");
	    //while (r.next()) { System.out.println(r.getInt("romNr")); }
    	//System.out.println("waaaa");
    	//db.close();
	}

}