package fp2014;

public class Notification {

	private String owner;
	private String text;
	private Appointment appointment;

	public Notification(String owner, String text, Appointment appointment) {
		this.owner = owner;
		this.text = text;
		this.appointment = appointment;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}


}
