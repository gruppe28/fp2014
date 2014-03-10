package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends Database {

	public User() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public boolean userExists(String inputUsername) throws ClassNotFoundException, SQLException {
		
		try {
			
			Database db = new Database();
		    ResultSet rs = db.query("SELECT brukernavn FROM Ansatt WHERE brukernavn ='"+inputUsername+"'");
			
			if (rs.next()) {
				return true;
			}
			
		} catch (Exception e) {
			System.out.println(e);;
		}
		return false;
	}
	
	public boolean checkLogin(String inputUsername, String inputPassword) throws ClassNotFoundException, SQLException {
		
		String password = null;
		
		try {
			Database db = new Database();
		    ResultSet rs = db.query("SELECT * FROM Ansatt WHERE brukernavn ='"+inputUsername+"'");
		    rs.next();
		    password = rs.getString("passord");
			
		}catch (Exception e){
			System.out.println(e);
		}
		
		if (userExists(inputUsername) && (inputPassword.equals(password))) {
			return true;
		} else {
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

}
