package fp2014;

import java.util.ArrayList;

import database.DBHandler;

public class User {

	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private ArrayList<Appointment> appointments;
	

	public User(String username, String password, String firstname, String lastname, String email) {
		this.setBrukernavn(username);
		this.setPassord(password);
		this.setFornavn(firstname);
		this.setEtternavn(lastname);
		this.setEmail(email);
	}
	
	/*
	 * Husk listeners ved endring av egenskaper til ansatt, viktig for � holde
	 * databasen oppdatert og synkronisert med kalenderklienten.
	 */
	
	public void setBrukernavn(String username) {
		this.username = username;
	}

	public void setPassord(String password) {
		this.password = password;
	}

	public void setFornavn(String firstname) {
		this.firstname = firstname;
	}

	public void setEtternavn(String lastname) {
		this.lastname = lastname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}


	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getEmail() {
		return email;
	}

	public ArrayList<Appointment> getAppointments() {
		return appointments;
	}
	
	public int getNumberOfUnseenNotifications(){
		return DBHandler.numberOfUnseenNotifications(username);
	}
}
