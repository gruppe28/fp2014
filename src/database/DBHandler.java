package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import server.Database;

import fp2014.Alarm;
import fp2014.Group;
import fp2014.Notification;
import fp2014.User;
import fp2014.Appointment;
import fp2014.Room;

@SuppressWarnings("unchecked")
public final class DBHandler {
	
	
	private static Client client;
	
	public static void setClient(Client c){
		client = c;
	}
	
	// All communication with the database should go through this class.
	
	private DBHandler(){ } // Ensures that you cannot make instances of class.
	
	public static int numberOfUnseenNotifications(String username){

		
		client.sendMessage("numberOfUnseenNotifications");
		client.sendMessage(username);
		int number = (int)client.recieveObject();
		
		return number;
	}
	

	public static ArrayList<Notification> getUnseenNotifications(String username){
		
		
		client.sendMessage("getUnseenNotifications");
		client.sendMessage(username);
		ArrayList<Notification> list = (ArrayList<Notification>)client.recieveObject();
		
		return list;
	}
	
	public static ArrayList<Notification> getSeenNotifications(String username){
		
		
		client.sendMessage("getSeenNotifications");
		client.sendMessage(username);
		ArrayList<Notification> list = (ArrayList<Notification>)client.recieveObject();
		
		return list;
	}
	
	public static ArrayList<Room> getAvailableRooms(String date, String from, String to){
		
		
		client.sendMessage("getAvailableRooms");
		client.sendMessage(date);
		client.sendMessage(from);
		client.sendMessage(to);
		ArrayList<Room> list = (ArrayList<Room>)client.recieveObject();
		
		return list;
	}
	
	public static ArrayList<Room> getRoomsWithCapacity(int capacity){
		
		
		client.sendMessage("getRoomsWithCapacity");
		client.sendObject(capacity);
		ArrayList<Room> list = (ArrayList<Room>)client.recieveObject();
		
		return list;
	}
	
	public static ArrayList<Appointment> getAppointmentsInInterval(ArrayList<User> users, ArrayList<String> days){
		
		
		client.sendMessage("getAppointmentsInInterval");
		client.sendObject(users);
		client.sendObject(days);
		ArrayList<Appointment> list = (ArrayList<Appointment>)client.recieveObject();
		
		return list;
	}
	
	// Fetches an Ansatt from the database based on username and returns it as an Ansatt object
	public static User getAnsatt(String brukernavn) {
		
		client.sendMessage("getAnsatt");
		client.sendObject(brukernavn);
		User a = (User)client.recieveObject();
		
		return a;
	}
	
	public static Room getRom(int romNr) {
		
		
		client.sendMessage("getRom");
		client.sendObject(romNr);
		Room r = (Room)client.recieveObject();
		
		return r;
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
	
	public static ArrayList<User> getAllUsers(){
		
		
		client.sendMessage("getAllUsers");
		ArrayList<User> list = (ArrayList<User>)client.recieveObject();
		
		return list;
	}
	
	// Alarm queries
	public static ArrayList<Alarm> getAlarms(User user){
		
		
		client.sendMessage("getAlarms");
		client.sendObject(user);
		ArrayList<Alarm> list = (ArrayList<Alarm>)client.recieveObject();
		
		return list;
	}
	
	public static void createAlarm(String time, String date, String username, int appointmentNum, int type){
		
		
		client.sendMessage("createAlarm");
		client.sendMessage(time);
		client.sendMessage(date);
		client.sendMessage(username);
		client.sendObject(appointmentNum);
		client.sendObject(type);
		
	}
	
	public static void createNotification(String text, Set<User> recipients, int appointmentNum){
		
		
		client.sendMessage("createNotification");
		client.sendMessage(text);
		client.sendObject(recipients);
		client.sendObject(appointmentNum);
		
	}
	
	public static void deleteAlarm(String username, int appointmentNum){
		
		
		client.sendMessage("deleteAlarm");
		client.sendMessage(username);
		client.sendObject(appointmentNum);
		
	}
	
	public static int getAlarmType(String username, int appointmentNum){
		client.sendMessage("getAlarmType");
		client.sendMessage(username);;
		client.sendObject(appointmentNum);
		int type = (int)client.recieveObject();
		return type;
	}
	
	// Update attendance
	public static void updateAttendance(String username, int appointmentNum, int attendance){
		
		
		client.sendMessage("updateAttendance");
		client.sendMessage(username);
		client.sendObject(appointmentNum);
		client.sendObject(attendance);
		
	}
	
	// Update hidden
	public static void updateHidden(String username, int appointmentNum, int status){
		
		
		client.sendMessage("updateHidden");
		client.sendMessage(username);
		client.sendObject(appointmentNum);
		client.sendObject(status);
		
	}
	
	// Update changed
	public static void updateChanged(String username, int appointmentNum){
		
		
		client.sendMessage("updateChanged");
		client.sendMessage(username);
		client.sendObject(appointmentNum);
		
	}
	
	// Get attendants and their status
	public static HashMap<User, Integer> getAttendants(int appointmentNum){
		
		
		client.sendMessage("getAttendants");
		client.sendObject(appointmentNum);
		HashMap<User, Integer> map = (HashMap<User, Integer>)client.recieveObject();
		
		return map;
	}
	
	public static void createAttendance(String username, int appointmentNum, int attendance, int change) {
		
		
		client.sendMessage("createAttendance");
		client.sendMessage(username);
		client.sendObject(appointmentNum);
		client.sendObject(attendance);
		client.sendObject(change);
		
	}
	
	public static int getCountOfAppointments(){
		
		
		client.sendMessage("getCountOfAppointments");
		int count = (int)client.recieveObject();
		
		return count;
	}
	
	public static void deleteAttendances(int appointmentNum){
		
		
		client.sendMessage("deleteAttendances");
		client.sendObject(appointmentNum);
		
	}
	
	public static void deleteAttendance(String brukernavn, int appointmentNum){
		
		
		client.sendMessage("deleteAttendance");
		client.sendMessage(brukernavn);
		client.sendObject(appointmentNum);
		
	}
	
	public static void deleteAppointment(int appointmentNum){
		
		
		client.sendMessage("deleteAppointment");
		client.sendObject(appointmentNum);
		
	}
	
	public static void updateAppointment(Appointment appointment) {
		
		
		client.sendMessage("updateAppointment");
		client.sendObject(appointment);
		
	}
	
	public static boolean isChanged(String username, int appointmentNum){
		
		
		client.sendMessage("isChanged");
		client.sendMessage(username);
		client.sendObject(appointmentNum);
		boolean change = (boolean)client.recieveObject();
		
		return change;
	}
	
	// Get all groups
	public static ArrayList<Group> getGroups(){
		client.sendMessage("getGroups");
		ArrayList<Group> list = (ArrayList<Group>)client.recieveObject();
		return list;
	}
	
	public static void createAppointment(Appointment app){
		client.sendMessage("createAppointment");
		client.sendObject(app);
	}
	
	public static void deleteAnsattAvtale(Appointment app, User u){
		client.sendMessage("deleteAnsattAvtale");
		client.sendObject(app);
		client.sendObject(u);
	}
	
	public static void createAnsattAvtale(Appointment app, User u){
		client.sendMessage("createAppointment");
		client.sendObject(app);
		client.sendObject(u);
	}
	
	public static void updateAnsattAvtale(Appointment app, User u, int status){
		client.sendMessage("updateAnsattAvtale");
		client.sendObject(app);
		client.sendObject(u);
		client.sendObject(status);
	}
	
}
