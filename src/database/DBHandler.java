package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import fp2014.Alarm;
import fp2014.Group;
import fp2014.Notification;
import fp2014.User;
import fp2014.Appointment;
import fp2014.Room;

public final class DBHandler {

	// All communication with the database should go through this class.
	
	private DBHandler(){ } // Ensures that you cannot make instances of class.
	
	public static int numberOfUnseenNotifications(String username){
		SQL db = new SQL();
	    try {
	    	ResultSet rs = db.query("SELECT * FROM Varsel WHERE brukernavn ='"+username+"' AND sett='0'");
			rs.last();
			return rs.getRow();
		} catch (SQLException e) { e.printStackTrace(); }
	    db.close();
		return -1; // Only to satisfy try/catch. Should not actually happen.
	}
	
	public static ArrayList<Notification> getUnseenNotifications(String username){
		ArrayList<Notification> notificationList = new ArrayList<Notification>();
		
		SQL db = new SQL();
	    try {
	    	ResultSet rs = db.query("SELECT * FROM Varsel WHERE brukernavn ='"+username+"' AND sett='0' ORDER BY id DESC");
			while(rs.next()){
				notificationList.add(new Notification(rs.getString("brukernavn"), rs.getString("tekst"), getAppointment(rs.getInt("avtaleNr"))));
				System.out.println();
				db.update("UPDATE Varsel SET sett = '1' WHERE brukernavn = '" + username + "' AND id = " + rs.getString("id"));
			}
		} catch (SQLException e) { e.printStackTrace(); }
	    db.close();
		return notificationList;
	}
	
	public static ArrayList<Notification> getSeenNotifications(String username){
		ArrayList<Notification> notificationList = new ArrayList<Notification>();
		
		SQL db = new SQL();
	    try {
	    	ResultSet rs = db.query("SELECT * FROM Varsel WHERE brukernavn ='"+username+"' AND sett='1' ORDER BY id DESC");
			while(rs.next()){
				notificationList.add(new Notification(rs.getString("brukernavn"), rs.getString("tekst"), getAppointment(rs.getInt("avtaleNr"))));
			}
		} catch (SQLException e) { e.printStackTrace(); }
	    db.close();
		return notificationList;
	}
	
	public static ArrayList<Room> getAvailableRooms(String date, String from, String to){
		ArrayList<Room> availableRooms = new ArrayList<Room>();
		try {
			SQL db = new SQL();
			boolean validRoom;

			ResultSet rs = db.query("select * from Rom");
		    while (rs.next()) {
		    	validRoom = true;
		    	ResultSet avtaleRes = db.query("select * from Avtale WHERE slett = 0 and romNr = " + rs.getInt("romNr") + " and dato = '" + date + "'");
		    	
		    	while (avtaleRes.next()) {
		    		if(checkOverlap(from, to, avtaleRes.getString("starttidspunkt"), avtaleRes.getString("sluttidspunkt"))) { validRoom = false; }
		    	}

		    	if(validRoom) { availableRooms.add(new Room(rs.getInt("romNr"), rs.getString("sted"), rs.getInt("antPlasser"), rs.getString("Beskrivelse"))); }
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
	
	public static ArrayList<Room> getRoomsWithCapacity(int capacity){
		ArrayList<Room> satisfyingRooms = new ArrayList<Room>();
		
			try {
				SQL db = new SQL();

				ResultSet rs = db.query("select * from Rom WHERE antPlasser >= " + capacity + "");
			    while (rs.next()) {
			    	satisfyingRooms.add(new Room(rs.getInt("romNr"), rs.getString("sted"), rs.getInt("antPlasser"), rs.getString("beskrivelse")));
			    }
			}
			catch (Exception e) { e.printStackTrace(); }
	
		return satisfyingRooms;
	}
	
	public static ArrayList<Appointment> getAppointmentsInInterval(ArrayList<User> users, ArrayList<String> days){
		ArrayList<Appointment> appointments = new ArrayList<>();
		String daysSQL;
		String usersSQL;
		daysSQL = "dato = '" + days.get(0) + "' ";
		usersSQL = "brukernavn = '" + users.get(0).getUsername() + "' ";
		
		for (int i = 1; i < days.size(); i++) {
			daysSQL += " or dato = '" + days.get(i) + "'";
		}
		
		for (int i = 1; i < users.size(); i++) {
			usersSQL += " or brukernavn = '" + users.get(i).getUsername() + "'";
		}
		
		try {
			SQL db = new SQL();

			ResultSet rs = db.query("select distinct Avtale.* from Avtale, AnsattAvtale WHERE AnsattAvtale.skjult = 0 AND Avtale.slett = 0 AND Avtale.avtaleNr = AnsattAvtale.avtaleNr and (" + usersSQL + ") and (" + daysSQL + ")");
		    while (rs.next()) {
		    	appointments.add(new Appointment(rs.getInt("avtaleNr"), rs.getString("navn"), rs.getString("starttidspunkt"), rs.getString("sluttidspunkt"), rs.getString("beskrivelse"), rs.getString("sted"), getRom(rs.getInt("romNr")), rs.getString("dato"), getAnsatt(rs.getString("opprettetAv"))));
		    }
		}
		catch (Exception e) { e.printStackTrace(); }
	
		return appointments;
	}
	
	// Fetches an Ansatt from the database based on username and returns it as an Ansatt object
	public static User getAnsatt(String brukernavn) {
		
		User user = new User(null, null, null, null, null);
		
		SQL db = new SQL();
		
		ResultSet rs = db.query("Select * from Ansatt Where brukernavn = " + "'" + brukernavn + "'" + "");
		
		try {
			if (rs.next()) {
				user = new User(rs.getString("brukernavn"), rs.getString("passord"), rs.getString("fornavn"), rs.getString("etternavn"), rs.getString("email"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return user;
	}
	
	public static Room getRom(int romNr) {
		
		Room room = new Room(0, null, 0, null);
		
		
		SQL db = new SQL();
		
		ResultSet rs = db.query("Select * from Rom Where romNr = " + "'" +romNr + "'" + "");
		
		try {
			if (rs.next()) {
				room = new Room(rs.getInt("romNr"), rs.getString("sted"), rs.getInt("antPlasser"), rs.getString("beskrivelse"));
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
		
		SQL db = new SQL();
		
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
	
	public static ArrayList<User> getAllUsers(){
		
		ArrayList<User> allUsers = new ArrayList<User>();
		
		SQL db = new SQL();
		
		ResultSet rs = db.query("Select * from Ansatt");
		
		try {
			while (rs.next()) {
				allUsers.add(new User(rs.getString("brukernavn"), rs.getString("passord"), rs.getString("fornavn"), rs.getString("etternavn"), rs.getString("email")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return allUsers;
	}
	
	// Alarm queries
	public static ArrayList<Alarm> getAlarms(User user){
		
		ArrayList<Alarm> alarms = new ArrayList<Alarm>();
		
		SQL db = new SQL();
		
		ResultSet rs = db.query("Select * from Alarm where brukernavn = '" + user.getUsername() + "'");
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
		SQL db = new SQL();
		db.update("INSERT INTO Alarm(tidspunkt, dato, avtaleNr, brukernavn, type) VALUES ('" + time + "', '" + date + "', " + appointmentNum + ", '" + username + "', " + type + ")");
		db.close();
	}
	
	public static void createNotification(String text, Set<User> recipients, int appointmentNum){
		SQL db = new SQL();
		
		for(User a : recipients){
			db.update("INSERT INTO Varsel(brukernavn, tekst, avtaleNr) VALUES ('" + a.getUsername() + "', '" + text + "', " + appointmentNum + ")");
		}
		
		db.close();
	}
	
	public static void deleteAlarm(String username, int appointmentNum){
		SQL db = new SQL();
	    db.update("DELETE FROM Alarm WHERE brukernavn = '" + username + "' and avtaleNr = " + appointmentNum);
	    db.close();
	}
	
	public static int getAlarmType(String username, int appointmentNum){
		
		int type = 0;
		
		SQL db = new SQL();
		
		ResultSet rs = db.query("Select type from Alarm where brukernavn = '" + username + "' AND avtaleNr = " + appointmentNum);
		try {
			if(rs.next()){ type = rs.getInt("type"); }
		} catch (SQLException e) { e.printStackTrace(); }
		
		db.close();
		return type;
	}
	
	
	// Update attendance
	public static void updateAttendance(String username, int appointmentNum, int attendance){
		SQL db = new SQL();
		db.update("UPDATE AnsattAvtale SET deltar = '" + attendance + "' WHERE brukernavn = '" + username + "' AND avtaleNr =" + appointmentNum);
		db.close();
	}
	
	// Update hidden
	public static void updateHidden(String username, int appointmentNum, int status){
		SQL db = new SQL();
		db.update("UPDATE AnsattAvtale SET skjult = '" + status + "' WHERE brukernavn = '" + username + "' AND avtaleNr =" + appointmentNum);
		db.close();
	}
	
	// Update changed
	public static void updateChanged(String username, int appointmentNum){
		SQL db = new SQL();
		db.update("UPDATE AnsattAvtale SET endret = 0 WHERE brukernavn = '" + username + "' AND avtaleNr =" + appointmentNum);
		db.close();
	}
	
	// Get attendants and their status
	public static HashMap<User, Integer> getAttendants(int appointmentNum){
		
		HashMap<User, Integer> attendants = new HashMap<User, Integer>();
		
		SQL db = new SQL();
		
		ResultSet rs = db.query("Select * from AnsattAvtale where avtaleNr = " + appointmentNum);
		try {
			while (rs.next()) {
				attendants.put(getAnsatt(rs.getString("brukernavn")), Integer.parseInt(rs.getString("deltar")));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		
		db.close();
		
		return attendants;
	}
	
	public static void createAttendance(String username, int appointmentNum, int attendance, int change) {
		
		SQL db = new SQL();
		db.update("INSERT INTO AnsattAvtale(avtaleNr, brukernavn, deltar, endret) VALUES ('"+ appointmentNum + "','" + username + "','" + attendance + "', " + change + ")");
		db.close();
	}
	
	public static int getCountOfAppointments(){
		
		int countOfAppointments = 0;
		
		SQL db = new SQL();
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
		SQL db = new SQL();
	    db.update("DELETE FROM AnsattAvtale WHERE avtaleNr = " + appointmentNum);
	    db.close();
	}
	
	public static void deleteAttendance(String brukernavn, int appointmentNum){
		SQL db = new SQL();
	    db.update("DELETE FROM AnsattAvtale WHERE brukernavn = '" + brukernavn + "' and avtaleNr = " + appointmentNum);
	    db.close();
	}
	
	public static void deleteAppointment(int appointmentNum){
		SQL db = new SQL();
	    db.update("UPDATE Avtale SET slett = 1 WHERE avtaleNr = " + appointmentNum);
	    db.close();
	}
	
	public static void updateAppointment(Appointment appointment) {
		SQL db = new SQL();
		if (appointment.getRom() == null || appointment.getRom().getRoomNumber() == 0) {
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
					+ "', romNr = '" + appointment.getRom().getRoomNumber() 
					+ "', dato = '" + appointment.getDate() 
					+ "' WHERE avtaleNr = " + appointment.getAppointmentNr());
		}
		
		db.close();
	}
	
	public static boolean isChanged(String username, int appointmentNum){
		
		boolean changed = false;
		
		SQL db = new SQL();
		
		ResultSet rs = db.query("Select * from AnsattAvtale where brukernavn = '" + username + "' and avtaleNr = " + appointmentNum);
		try {
			if(rs.next()) {
				if(rs.getInt("endret") == 1) { changed = true; }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		db.close();
		return changed;
	}
	
	// Get all groups
	public static ArrayList<Group> getGroups(){
		
		ArrayList<Group> groups = new ArrayList<Group>();
		
		SQL db = new SQL();
		
		ResultSet rs = db.query("Select * from Gruppe, MedlemAv where ID = gruppeID ORDER BY navn ASC");
		try {
			String curName = "";
			Group curGroup = new Group("");
			while (rs.next()) {
				if(!curName.equals(rs.getString("navn"))){
					curName = rs.getString("navn");
					curGroup = new Group(rs.getString("navn"));
					groups.add(curGroup);
					System.out.println(rs.getString("navn"));
				}
				curGroup.addMember(rs.getString("brukernavn"));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		
		db.close();
		
		return groups;
	}
	
}
