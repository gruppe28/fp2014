package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import fp2014.Alarm;
import fp2014.Notification;
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
	
	public static ArrayList<Notification> getUnseenNotifications(String username){
		ArrayList<Notification> notificationList = new ArrayList<Notification>();
	    try {
	    	Database db = new Database();
	    	ResultSet rs = db.query("SELECT * FROM Varsel WHERE brukernavn ='"+username+"' AND sett='0' ORDER BY id DESC");
			while(rs.next()){
				notificationList.add(new Notification(rs.getString("brukernavn"), rs.getString("tekst"), getAppointment(rs.getInt("avtaleNr"))));
				System.out.println();
				db.update("UPDATE Varsel SET sett = '1' WHERE brukernavn = '" + username + "' AND id = " + rs.getString("id"));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return notificationList;
	}
	
	public static ArrayList<Notification> getSeenNotifications(String username){
		ArrayList<Notification> notificationList = new ArrayList<Notification>();
	    try {
	    	Database db = new Database();
	    	ResultSet rs = db.query("SELECT * FROM Varsel WHERE brukernavn ='"+username+"' AND sett='1' ORDER BY id DESC");
			while(rs.next()){
				notificationList.add(new Notification(rs.getString("brukernavn"), rs.getString("tekst"), getAppointment(rs.getInt("avtaleNr"))));
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
		    	ResultSet avtaleRes = db.query("select * from Avtale WHERE slett = 0 and romNr = " + rs.getInt("romNr") + " and dato = '" + date + "'");
		    	
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

			ResultSet rs = db.query("select distinct Avtale.* from Avtale, AnsattAvtale WHERE AnsattAvtale.skjult = 0 AND Avtale.slett = 0 AND Avtale.avtaleNr = AnsattAvtale.avtaleNr and (" + usersSQL + ") and (" + daysSQL + ")");
		    while (rs.next()) {
		    	appointments.add(new Appointment(rs.getInt("avtaleNr"), rs.getString("navn"), rs.getString("starttidspunkt"), rs.getString("sluttidspunkt"), rs.getString("beskrivelse"), rs.getString("sted"), getRom(rs.getInt("romNr")), rs.getString("dato"), getAnsatt(rs.getString("opprettetAv"))));
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
	
	// Get appointment from "avtaleNr"
	public static Appointment getAppointment(int avtaleNr) {
		
		Appointment appointment = new Appointment();
		
		Database db = new Database();
		
		ResultSet rs = db.query("Select * from Avtale where avtaleNr = " + "'" +avtaleNr + "'" + "");
		
		try {
			if (rs.next()) {
		    	appointment = new Appointment(rs.getInt("avtaleNr"), rs.getString("navn"), rs.getString("starttidspunkt"), rs.getString("sluttidspunkt"), rs.getString("beskrivelse"), rs.getString("sted"), getRom(rs.getInt("romNr")), rs.getString("dato"), getAnsatt(rs.getString("opprettetAv")));
				return appointment;
			}
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return appointment;
	}
	
	public static ArrayList<Ansatt> getAllUsers(){
		
		ArrayList<Ansatt> allUsers = new ArrayList<Ansatt>();
		
		Database db = new Database();
		
		ResultSet rs = db.query("Select * from Ansatt");
		
		try {
			while (rs.next()) {
				allUsers.add(new Ansatt(rs.getString("brukernavn"), rs.getString("passord"), rs.getString("fornavn"), rs.getString("etternavn"), rs.getString("email")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return allUsers;
	}
	
	// Alarm queries
	public static ArrayList<Alarm> getAlarms(Ansatt user){
		
		ArrayList<Alarm> alarms = new ArrayList<Alarm>();
		
		Database db = new Database();
		
		ResultSet rs = db.query("Select * from Alarm where brukernavn = '" + user.getBrukernavn() + "'");
		try {
			while (rs.next()) {
				alarms.add(new Alarm(rs.getString("tidspunkt"), rs.getString("dato"), getAppointment(rs.getInt("avtaleNr"))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		db.close();
		return alarms;
	}
	
	public static void createAlarm(String time, String date, String username, int appointmentNum, int type){
		Database db = new Database();
		db.update("INSERT INTO Alarm(tidspunkt, dato, avtaleNr, brukernavn, type) VALUES ('" + time + "', '" + date + "', " + appointmentNum + ", '" + username + "', " + type + ")");
		db.close();
	}
	
	public static void createNotification(String text, Set<Ansatt> recipients, int appointmentNum){
		Database db = new Database();
		
		for(Ansatt a : recipients){
			db.update("INSERT INTO Varsel(brukernavn, tekst, avtaleNr) VALUES ('" + a.getBrukernavn() + "', '" + text + "', " + appointmentNum + ")");
		}
		
		db.close();
	}
	
	public static void deleteAlarm(String username, int appointmentNum){
		Database db = new Database();
	    db.update("DELETE FROM Alarm WHERE brukernavn = '" + username + "' and avtaleNr = " + appointmentNum);
	    db.close();
	}
	
	public static int getAlarmType(String username, int appointmentNum){
		
		int type = 0;
		
		Database db = new Database();
		
		ResultSet rs = db.query("Select type from Alarm where brukernavn = '" + username + "' AND avtaleNr = " + appointmentNum);
		try {
			if(rs.next()){ type = rs.getInt("type"); }
		} catch (SQLException e) { e.printStackTrace(); }
		
		db.close();
		return type;
	}
	
	
	// Update attendance
	public static void updateAttendance(String username, int appointmentNum, int attendance){
		Database db = new Database();
		db.update("UPDATE AnsattAvtale SET deltar = '" + attendance + "' WHERE brukernavn = '" + username + "' AND avtaleNr =" + appointmentNum);
		db.close();
	}
	
	// Update hidden
	public static void updateHidden(String username, int appointmentNum, int status){
		Database db = new Database();
		db.update("UPDATE AnsattAvtale SET skjult = '" + status + "' WHERE brukernavn = '" + username + "' AND avtaleNr =" + appointmentNum);
		db.close();
	}
	
	
	// Get attendants and their status
	public static HashMap<Ansatt, Integer> getAttendants(int appointmentNum){
		
		HashMap<Ansatt, Integer> attendants = new HashMap<Ansatt, Integer>();
		
		Database db = new Database();
		
		ResultSet rs = db.query("Select * from AnsattAvtale where avtaleNr = " + appointmentNum);
		try {
			while (rs.next()) {
				attendants.put(getAnsatt(rs.getString("brukernavn")), Integer.parseInt(rs.getString("deltar")));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		
		db.close();
		
		return attendants;
	}
	
	public static void createAttendance(String username, int appointmentNum, int attendance) {		
		Database db = new Database();
		db.update("INSERT INTO AnsattAvtale(avtaleNr, brukernavn, deltar) VALUES ('"+ appointmentNum + "','" + username + "','" + attendance + "')");
		db.close();
	}
	
	public static int getCountOfAppointments(){
		
		int countOfAppointments = 0;
		
		Database db = new Database();
		ResultSet rs = db.query("SELECT COUNT(*) FROM Avtale");
		try {
			if (rs.next()) {
				countOfAppointments = rs.getInt("COUNT(*)");
			}
		} catch (SQLException e) { e.printStackTrace(); }
		
		db.close();
		
		return countOfAppointments;
	}
	
	public static void deleteAttendances(int appointmentNum){
		Database db = new Database();
	    db.update("DELETE FROM AnsattAvtale WHERE avtaleNr = " + appointmentNum);
	    db.close();
	}
	
	public static void deleteAttendance(String brukernavn, int appointmentNum){
		Database db = new Database();
	    db.update("DELETE FROM AnsattAvtale WHERE brukernavn = '" + brukernavn + "' and avtaleNr = " + appointmentNum);
	    db.close();
	}
	
	
	public static void deleteAppointment(int appointmentNum){
		Database db = new Database();
	    db.update("UPDATE Avtale SET slett = 1 WHERE avtaleNr = " + appointmentNum);
	    db.close();
	}
	
	public static void updateAppointment(Appointment appointment) {
		Database db = new Database();
		if (appointment.getRom() == null) {
			db.update("UPDATE Avtale SET navn = '" + appointment.getName() 
					+ "', starttidspunkt = '" + appointment.getStartTime() 
					+ "', sluttidspunkt = '" + appointment.getEndTime() 
					+ "', beskrivelse = '" + appointment.getDescription() 
					+ "', sted = '" + appointment.getPlace() 
					+ "', dato = '" + appointment.getDate() 
					+ "' WHERE avtaleNr = " + appointment.getAppointmentNr());
		} else {
			db.update("UPDATE Avtale SET navn = '" + appointment.getName() 
					+ "', starttidspunkt = '" + appointment.getStartTime() 
					+ "', sluttidspunkt = '" + appointment.getEndTime() 
					+ "', beskrivelse = '" + appointment.getDescription() 
					+ "', romNr = '" + appointment.getRom().getRomNr() 
					+ "', dato = '" + appointment.getDate() 
					+ "' WHERE avtaleNr = " + appointment.getAppointmentNr());
		}
		
		db.close();
	}
	
}
