package GUI;

import java.awt.Color;

import javax.swing.JTextArea;

import client.ClientDBCalls;
import fp2014.Appointment;
import fp2014.User;

@SuppressWarnings("serial")
public class appointmentBubble extends JTextArea {
	
	public static final Color STATUS_RED = Color.RED;
	public static final Color STATUS_GREEN = Color.GREEN;
	public static final Color STATUS_YELLOW = Color.YELLOW;
	
	private Appointment appointment;
	private Color color;
	private boolean focused;
	
	
	public appointmentBubble(CalendarPanel parent, User user, Appointment appointment){
		this.appointment = appointment;
		fillText(user);
		setEditable(false);
		addFocusListener(parent);
		setBackground(Color.GRAY);
		setForeground(Color.WHITE);
		
	}
	
	private void fillText(User user){
		setText(appointment.getName());
		if (user.getUsername().equals(appointment.getMadeBy().getUsername())){
			append(" - eier");
		}else if(ClientDBCalls.isChanged(user.getUsername(), appointment.getAppointmentNr())){
			append(" - endret");
		}
		append("\n" + appointment.getLocation());
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getAppointmentNr(){
		return appointment.getAppointmentNr();
	}

	public boolean isFocused() {
		return focused;
	}

	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
	
}
