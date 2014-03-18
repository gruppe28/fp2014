package database;

import java.sql.ResultSet;

public class UserDB{
	
	// hei

	public String feedback;
	
	// Checks the existence of a username with the database
	public boolean userExists(String inputUsername){
		try {
			SQL db = new SQL();
		    ResultSet rs = db.query("SELECT brukernavn FROM Ansatt WHERE brukernavn ='"+inputUsername+"'");
			
			if(rs.next()) { db.close(); return true; } //If there were any results, the user exists.
		} catch (Exception e) { }
		return false;
	}
	
	// Check if a username/password combination is valid. Proper feedback is written to the feedback field
	public boolean checkLogin(String inputUsername, String inputPassword){
		
		String password = null;
		
		try {
			SQL db = new SQL();
		    ResultSet rs = db.query("SELECT * FROM Ansatt WHERE brukernavn ='"+inputUsername+"'");
		    if(rs.next()) { password = rs.getString("passord"); }
		} catch (Exception e){ feedback = "Could not connect to the database."; return false; }
		
		if (userExists(inputUsername) && (inputPassword.equals(password))) { return true; }
		else if (userExists(inputUsername)) { feedback = "Incorrect password."; }
		else { feedback = "Username does not exist."; }
		return false;
	}



}
