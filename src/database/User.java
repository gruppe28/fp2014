package database;

import java.sql.ResultSet;
import java.sql.SQLException;

import fp2014.Ansatt;

public class User{

	public User(){
		super();
		// TODO Auto-generated constructor stub
	}
	
	public boolean userExists(String inputUsername){
		try {
			Database db = new Database();
		    ResultSet rs = db.query("SELECT brukernavn FROM Ansatt WHERE brukernavn ='"+inputUsername+"'");
			
			if (rs.next()) {
				return true;
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	public boolean checkLogin(String inputUsername, String inputPassword){
		
		String password = null;
		
		try {
			Database db = new Database();
		    ResultSet rs = db.query("SELECT * FROM Ansatt WHERE brukernavn ='"+inputUsername+"'");
		    if(rs.next()) { password = rs.getString("passord"); }
			
		}catch (Exception e){
			System.out.println(e);
		}
		
		if (userExists(inputUsername) && (inputPassword.equals(password))) {
			System.out.println("truuu");
			return true;
		} else {
			System.out.println("fake ass gangsta");
			return false;
		}
	}

	public void logout() {
		/*
		 * Logger ut bruker, avslutter alle connections, og vise login vindu for
		 * en ny okt. Viktig a fjerne alle tidligere permissions.
		 */
	}
	
	public void loadAppointments(){
		// laster inn en brukers avtaler
	}
	
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
