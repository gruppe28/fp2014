package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import fp2014.Ansatt;

public class User{

	public String feedback;
	
	// Checks the existence of a username with the database
	public boolean userExists(String inputUsername){
		try {
			Database db = new Database();
		    ResultSet rs = db.query("SELECT brukernavn FROM Ansatt WHERE brukernavn ='"+inputUsername+"'");
			
			if(rs.next()) { db.close(); return true; } //If there were any results, the user exists.
		} catch (Exception e) { }
		return false;
	}
	
	// Check if a username/password combination is valid. Proper feedback is written to the feedback field
	public boolean checkLogin(String inputUsername, String inputPassword){
		
		String password = null;
		
		try {
			Database db = new Database();
		    ResultSet rs = db.query("SELECT * FROM Ansatt WHERE brukernavn ='"+inputUsername+"'");
		    if(rs.next()) { password = rs.getString("passord"); }
		} catch (Exception e){ feedback = "Could not connect to the database."; return false; }
		
		if (userExists(inputUsername) && (inputPassword.equals(password))) { return true; }
		else if (userExists(inputUsername)) { feedback = "Incorrect password."; }
		else { feedback = "Username does not exist."; }
		return false;
	}

	// Fetches an Ansatt from the database based on username and returns it as an Ansatt object
	public Ansatt getAnsatt(String brukernavn) {
		
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

}
