package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fp2014.Ansatt;
import fp2014.Appointment;
import fp2014.Rom;

public final class DBHandler {

	// All communication with the database should go through this class.
	
	private DBHandler(){ } // Ensures that you cannot make instances of class.
	
	public static int numberOfUnseenNotifications(String username){
	    try {
	    	Database db = new Database();
	    	ResultSet rs = db.query("SELECT * FROM Varsel WHERE brukernavn ='"+username+"' AND sett='0'");
			rs.last();
			return rs.getRow();
		} catch (SQLException e) { e.printStackTrace(); }
		return -1; // Only to satisfy try/catch. Should not actually happen.
	}
	
	public static ArrayList<String> getUnseenNotifications(String username){
		ArrayList<String> notificationList = new ArrayList<String>();
	    try {
	    	Database db = new Database();
	    	ResultSet rs = db.query("SELECT * FROM Varsel WHERE brukernavn ='"+username+"' AND sett='0' ORDER BY id DESC");
			while(rs.next()){
				notificationList.add(rs.getString("tekst"));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return notificationList;
	}
	
	public static ArrayList<String> getSeenNotifications(String username){
		ArrayList<String> notificationList = new ArrayList<String>();
	    try {
	    	Database db = new Database();
	    	ResultSet rs = db.query("SELECT * FROM Varsel WHERE brukernavn ='"+username+"' AND sett='1' ORDER BY id DESC");
			while(rs.next()){
				notificationList.add(rs.getString("tekst"));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return notificationList;
	}
	
	public static ArrayList<Rom> getAvailableRooms(String date, String from, String to){
		ArrayList<Rom> availableRooms = new ArrayList<Rom>();
		try {
			Database db = new Database();
			boolean validRoom;

			ResultSet rs = db.query("select * from Rom");
		    while (rs.next()) {
		    	validRoom = true;
		    	ResultSet avtaleRes = db.query("select * from Avtale WHERE romNr = " + rs.getInt("romNr") + " and dato = '" + date + "'");
		    	
		    	while (avtaleRes.next()) {
		    		if(checkOverlap(from, to, avtaleRes.getString("starttidspunkt"), avtaleRes.getString("sluttidspunkt"))) { validRoom = false; }
		    	}

		    	if(validRoom) { availableRooms.add(new Rom(rs.getInt("romNr"), rs.getString("sted"), rs.getInt("antPlasser"), rs.getString("Beskrivelse"))); }
		    	}
	    	db.close();
		}
		catch (Exception e) { e.printStackTrace(); }
		return availableRooms;
	}
	
	private static boolean checkOverlap(String from1, String to1, String from2, String to2){
		// Convert strings to floats
		float from1float = Float.parseFloat(from1.replace(":", "."));
		float from2float = Float.parseFloat(from2.replace(":", "."));
		float to1float = Float.parseFloat(to1.replace(":", "."));
		float to2float = Float.parseFloat(to2.replace(":", "."));
		
		// Check for overlap
		return (from2float > from1float && from2float < to1float) || (to2float > from1float && to2float < to1float) || (from2float <= from1float && to2float >= to1float);
	}
	
	public static ArrayList<Rom> getRoomsWithCapacity(int capacity){
		ArrayList<Rom> satisfyingRooms = new ArrayList<Rom>();
		
			try {
				Database db = new Database();

				ResultSet rs = db.query("select * from Rom WHERE antPlasser >= " + capacity + "");
			    while (rs.next()) {
			    	satisfyingRooms.add(new Rom(rs.getInt("romNr"), rs.getString("sted"), rs.getInt("antPlasser"), rs.getString("beskrivelse")));
			    }
			}
			catch (Exception e) { e.printStackTrace(); }
	
		return satisfyingRooms;
	}
	
	public static ArrayList<Appointment> getAppointmentsInInterval(ArrayList<Ansatt> users, ArrayList<String> days){
		ArrayList<Appointment> appointments = new ArrayList<>();
		String daysSQL;
		String usersSQL;
		daysSQL = "dato = '" + days.get(0) + "' ";
		usersSQL = "brukernavn = '" + users.get(0).getBrukernavn() + "' ";
		
		for (int i = 1; i < days.size(); i++) {
			daysSQL += " or dato = '" + days.get(i) + "'";
		}
		
		for (int i = 1; i < users.size(); i++) {
			usersSQL += " or brukernavn = '" + users.get(i).getBrukernavn() + "'";
		}
		
		try {
			Database db = new Database();

			ResultSet rs = db.query("select * from Avtale, AnsattAvtale WHERE Avtale.avtaleNr = AnsattAvtale.avtaleNr and (" + usersSQL + ") and (" + daysSQL + ")");
		    while (rs.next()) {
		    	appointments.add(new Appointment(rs.getString("navn"), rs.getString("starttidspunkt"), rs.getString("sluttidspunkt"), rs.getString("beskrivelse"), rs.getString("sted"), getRom(rs.getInt("romNr")), rs.getString("dato"), getAnsatt(rs.getString("opprettetAv"))));
		    }
		}
		catch (Exception e) { e.printStackTrace(); }
		
		return appointments;
	}
	
	// Fetches an Ansatt from the database based on username and returns it as an Ansatt object
	public static Ansatt getAnsatt(String brukernavn) {
		
		Ansatt user = new Ansatt(null, null, null, null, null);
		
		Database db = new Database();
		
		ResultSet rs = db.query("Select * from Ansatt Where brukernavn = " + "'" + brukernavn + "'" + "");
		
		try {
			if (rs.next()) {
				user = new Ansatt(rs.getString("brukernavn"), rs.getString("passord"), rs.getString("fornavn"), rs.getString("etternavn"), rs.getString("email"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return user;
	}
	
	public static Rom getRom(int romNr) {
		
		Rom room = new Rom(0, null, 0, null);
		
		Database db = new Database();
		
		ResultSet rs = db.query("Select * from Rom Where romNr = " + "'" +romNr + "'" + "");
		
		try {
			if (rs.next()) {
				room = new Rom(rs.getInt("romNr"), rs.getString("sted"), rs.getInt("antPlasser"), rs.getString("beskrivelse"));
				return room;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return room;
	}
	
}
