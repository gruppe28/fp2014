package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Set;

import fp2014.User;
import fp2014.Appointment;

@SuppressWarnings("unchecked")
public class Server{
	
	ServerSocket providerSocket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	
	Server(){}
	
	void run()
	{
		try{
			providerSocket = new ServerSocket(2828, 1000);
			System.out.println("Waiting for connection");
			connection = providerSocket.accept();
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			sendMessage("Connection successful");
			do{
				try{
					message = (String)in.readObject();
					
					if(message.equals("bye")) { close(); }
					
					if(!message.equals("bye")){
						if(message.equals("numberOfUnseenNotifications")){
							String username = (String)in.readObject();
							int number = DBMethods.numberOfUnseenNotifications(username);
							sendObject(number);
						}
						else if(message.equals("getUnseenNotifications")){
							String username = (String)in.readObject();
							out.writeObject(DBMethods.getUnseenNotifications(username));
							out.flush();
						}
						else if(message.equals("getSeenNotifications")){
							String username = (String)in.readObject();
							out.writeObject(DBMethods.getSeenNotifications(username));
							out.flush();
						}
						else if(message.equals("getAvailableRooms")){
							String date = (String)in.readObject();
							String from = (String)in.readObject();
							String to = (String)in.readObject();
							out.writeObject(DBMethods.getAvailableRooms(date, from, to));
							out.flush();
						}
						else if(message.equals("getRoomsWithCapacity")){
							int cap = (int)in.readObject();
							out.writeObject(DBMethods.getRoomsWithCapacity(cap));
							out.flush();
						}
						else if(message.equals("getAppointmentsInInterval")){
							ArrayList<User> users = (ArrayList<User>)in.readObject();
							ArrayList<String> days = (ArrayList<String>)in.readObject();
							out.writeObject(DBMethods.getAppointmentsInInterval(users, days));
							out.flush();
						}
						else if(message.equals("getAnsatt")){
							String username = (String)in.readObject();
							out.writeObject(DBMethods.getAnsatt(username));
							out.flush();
						}
						else if(message.equals("getRom")){
							int romNr = (int)in.readObject();
							out.writeObject(DBMethods.getRom(romNr));
							out.flush();
						}
						else if(message.equals("getAttendants")){
							int appNum = (int)in.readObject();
							out.writeObject(DBMethods.getAttendants(appNum));
							out.flush();
						}
						else if(message.equals("getAllUsers")){
							out.writeObject(DBMethods.getAllUsers());
							out.flush();
						}
						else if(message.equals("getAlarms")){
							User user = (User)in.readObject();
							out.writeObject(DBMethods.getAlarms(user));
							out.flush();
						}
						else if(message.equals("createAlarm")){
							String time = (String)in.readObject();
							String date = (String)in.readObject();
							String username = (String)in.readObject();
							int appNum = (int)in.readObject();
							int type = (int)in.readObject();
							DBMethods.createAlarm(time, date, username, appNum, type);
						}
						else if(message.equals("createNotification")){
							String text = (String)in.readObject();
							Set<User> rec = (Set<User>)in.readObject();
							int appNum = (int)in.readObject();
							DBMethods.createNotification(text, rec, appNum);
						}
						else if(message.equals("deleteAlarm")){
							String username = (String)in.readObject();
							int appNum = (int)in.readObject();
							DBMethods.deleteAlarm(username, appNum);
						}
						else if(message.equals("getAlarmType")){
							String username = (String)in.readObject();
							int appNum = (int)in.readObject();
							out.writeObject(DBMethods.getAlarmType(username, appNum));
							out.flush();
						}
						else if(message.equals("updateAttendance")){
							String username = (String)in.readObject();
							int appNum = (int)in.readObject();
							int attendance = (int)in.readObject();
							DBMethods.updateAttendance(username, appNum, attendance);
						}
						else if(message.equals("updateHidden")){
							String username = (String)in.readObject();
							int appNum = (int)in.readObject();
							int status = (int)in.readObject();
							DBMethods.updateHidden(username, appNum, status);
						}
						else if(message.equals("updateChanged")){
							String username = (String)in.readObject();
							int appNum = (int)in.readObject();
							DBMethods.updateChanged(username, appNum);
						}
						else if(message.equals("createAttendance")){
							String username = (String)in.readObject();
							int appNum = (int)in.readObject();
							int attendance = (int)in.readObject();
							int change = (int)in.readObject();
							DBMethods.createAttendance(username, appNum, attendance, change);
						}
						else if(message.equals("getCountOfAppointments")){
							out.writeObject(DBMethods.getCountOfAppointments());
							out.flush();
						}
						else if(message.equals("deleteAttendances")){
							int appNum = (int)in.readObject();
							DBMethods.deleteAttendances(appNum);
						}
						else if(message.equals("deleteAttendance")){
							String username = (String)in.readObject();
							int appNum = (int)in.readObject();
							DBMethods.deleteAttendance(username, appNum);
						}
						else if(message.equals("deleteAppointment")){
							int appNum = (int)in.readObject();
							DBMethods.deleteAppointment(appNum);
						}
						else if(message.equals("updateAppointment")){
							Appointment app = (Appointment)in.readObject();
							DBMethods.updateAppointment(app);
						}
						else if(message.equals("isChanged")){
							String username = (String)in.readObject();
							int appNum = (int)in.readObject();
							out.writeObject(DBMethods.isChanged(username, appNum));
							out.flush();
						}
						else if(message.equals("getGroups")){
							out.writeObject(DBMethods.getGroups());
							out.flush();
						}
						else if(message.equals("findPassword")){
							String username = (String)in.readObject();
							out.writeObject(DBMethods.findPassword(username));
							out.flush();
						}
						else if(message.equals("userExists")){
							String username = (String)in.readObject();
							out.writeObject(DBMethods.userExists(username));
							out.flush();
						}
						
						
					}

				}
				catch(ClassNotFoundException classnot){
					System.err.println("Bad data!");
				}
			}while(!message.equals("bye"));
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{  }
	}
	
	void close(){
		System.out.println("Server stenger");
		try{
			in.close();
			out.close();
			providerSocket.close();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	void sendObject(Object o)
	{
		try{
			out.writeObject(o);
			out.flush();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		Server server = new Server();
		while(true){
			server.run();
		}
	}
}