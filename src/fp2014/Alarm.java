package fp2014;

import java.io.Serializable;

public class Alarm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4105824525081789056L;
	String time;
	String date;
	Appointment appointment;
	
	public Alarm(String time, String date, Appointment appointment){
		this.time = time;
		this.date = date;
		this.appointment = appointment;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

}
