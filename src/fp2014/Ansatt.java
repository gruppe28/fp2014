package fp2014;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Database;

/*
 * AUDUN!!
 */

public class Ansatt {

	private String brukernavn;
	private String passord;
	private String fornavn;
	private String etternavn;
	private String email;
	private ArrayList<Appointment> appointments;
	

	public Ansatt(String brukernavn, String passord, String fornavn, String etternavn, String email) {
		this.setBrukernavn(brukernavn);
		this.setPassord(passord);
		this.setFornavn(fornavn);
		this.setEtternavn(etternavn);
		this.setEmail(email);
	}
	
	/*
	 * Husk listeners ved endring av egenskaper til ansatt, viktig for å holde
	 * databasen oppdatert og synkronisert med kalenderklienten.
	 */
	
	public void setBrukernavn(String brukernavn) {
		this.brukernavn = brukernavn;
	}

	public void setPassord(String passord) {
		this.passord = passord;
	}

	public void setFornavn(String fornavn) {
		this.fornavn = fornavn;
	}

	public void setEtternavn(String etternavn) {
		this.etternavn = etternavn;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBrukernavn() {
		return brukernavn;
	}

	public String getPassord() {
		return passord;
	}


	public String getFornavn() {
		return fornavn;
	}

	public String getEtternavn() {
		return etternavn;
	}

	public String getEmail() {
		return email;
	}

	public ArrayList<Appointment> getAppointments() {
		return appointments;
	}
	
	public void visKalender(){
		/*
		 * Kalles som standardmetode for fremvisning av logget inn ansatt sine avtaler.
		 * 
		 */
		
		/*
		 * 
		 */
	}
	
	public int getNumberOfUnseenNotifications(){
	    try {		
	    	Database db = new Database();
	    	ResultSet rs = db.query("SELECT * FROM Varsel WHERE brukernavn ='"+brukernavn+"' AND sett='0'");
			rs.last();
			return rs.getRow();
		} catch (SQLException e) { e.printStackTrace(); }
		return -1; // Only to satisfy try/catch. Should not actually happen.
	}
}
