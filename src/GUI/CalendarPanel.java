package GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class CalendarPanel extends JPanel {
	
	public static final int HOUR_HEIGHT = 40;
	public static final int DAY_WIDTH = 105;
	public static final int CALENDAR_X_START = 40;
	public static final int CALENDAR_Y_START = 30;
	
	private ArrayList<Component> existingAppointments = new ArrayList<Component>();
	private JPanel panel;

	public CalendarPanel(JFrame frame){
		
		JSeparator hSep, vSep;
		JLabel label = null;
		int hours = 24;
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(804, 1000));
		panel.setLayout(null);
		
		for (int i = 0; i < hours; i++) {
			label = new JLabel( i+":00");
			label.setBounds(5, 30+(i*40), 50, 40);
			panel.add(label);
		}
		
		String[] days = {"Monday", "Tuesday", "Wedensday", "Thursday", "Friday", "Saturday", "Sunday"};
		
		for (int i = 0; i < days.length; i++) {
			this.addDay(days[i], 50+(i*CalendarPanel.DAY_WIDTH), 0, 100, 40, panel);
		}
		
		for (int i = 0; i <= hours; i++) {
			hSep = new JSeparator(JSeparator.HORIZONTAL);
			hSep.setBounds(CalendarPanel.CALENDAR_X_START, 30+(i*CalendarPanel.HOUR_HEIGHT), 735, 5);			
			panel.add(hSep);
		}
		
		for (int i = 0; i <= days.length; i++) {
			vSep = new JSeparator(JSeparator.VERTICAL);
			vSep.setBounds(CalendarPanel.CALENDAR_X_START+(i*CalendarPanel.DAY_WIDTH), 30, 5, 960);
			panel.add(vSep);			
		}
		
		JScrollPane s = new JScrollPane(panel, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		s.setPreferredSize(new Dimension(804, 500));
		
		add(s);
		
	}
	
	public void addDay(String day, int x, int y, int width, int height, JPanel panel){
		JLabel label = new JLabel(day);
		label.setBounds(x, y, width, height);
		panel.add(label);
	}
	
	public void addAppointmentToCalendar(Component c, int x, int y, int width, int height, CalendarPanel panel){
		c.setBounds(x, y, width, height);
		if (checkBounds(this.existingAppointments, c.getBounds())) {
			panel.panel.add(c);
			this.existingAppointments.add(c);
		} else {
			addAppointmentWithCollision(this.existingAppointments, c, panel);
		}
	}
	
	private void addAppointmentWithCollision(ArrayList<Component> existingAppointments, Component c, CalendarPanel panel) {
		// TODO Adds component to the CalendarGUI if it is intersecting existing appointments.
		
		ArrayList<Boolean> intersect = new ArrayList<Boolean>();
		ArrayList<Component> intersectingComponents = new ArrayList<Component>();
		
		//Gets the intersecting components
		for (int i = 0; i < this.existingAppointments.size(); i++) {
			intersect.add(c.getBounds().intersects(existingAppointments.get(i).getBounds()));
		}
		
		//Adds the intersecting components to an array
		for (int i = 0; i < intersect.size(); i++) {
			if (intersect.get(i)) {
				intersectingComponents.add(existingAppointments.get(i));
			}
		}
		
		//Removes the conflicting appointments from the existing appointments
		for (int i = 0; i < intersectingComponents.size(); i++) {
			for (int j = 0; j < existingAppointments.size(); j++) {
				if (intersectingComponents.get(i) == existingAppointments.get(j)) {
					this.removeAppointmentFromCalendar(existingAppointments.get(j), panel);
				}
			}
		}
		
		intersectingComponents.add(c);
		System.out.println(intersectingComponents.size());
		
		//Adds the intersecting component and the other components that collide with it back to the calendar
		
		//Need to find a way to determine the new X-coordinate for each of the components 

		//Need to find out how many collisions there is on the x-pane, and how many there is on the y-pane
		
		

	}

	public void removeAppointmentFromCalendar(Component c, CalendarPanel panel){
		if (this.existingAppointments.contains(c)) {
			this.existingAppointments.remove(c);
			panel.panel.remove(c);
		}
	}
	
	/*
	 * Checks if component to be added is within bounds of any other appointments in the view
	 */
	
	public boolean checkBounds(ArrayList<Component> existingAppointments, Rectangle incomingAppointment){
	
	ArrayList<Boolean> intersect = new ArrayList<Boolean>();
	
	for (int i = 0; i < this.existingAppointments.size(); i++) {
		intersect.add(incomingAppointment.getBounds().intersects(existingAppointments.get(i).getBounds()));
	}
	
	for (int i = 0; i < intersect.size(); i++) {
		if (intersect.get(i)) {
			return false;
		}
	}
	
	return true;
	}
	
	public static void main(String[] args) {
				
		JFrame frame = new JFrame();
		
		CalendarPanel cp = new CalendarPanel(frame);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(cp);
		frame.setSize(804, 500);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		for (int i = 0; i < 12; i++) {
			cp.addAppointmentToCalendar(new JTextArea("test"), CalendarPanel.CALENDAR_X_START, 30+(i*CalendarPanel.HOUR_HEIGHT), CalendarPanel.DAY_WIDTH, CalendarPanel.HOUR_HEIGHT, cp);
		}
		
//		cp.addAppointmentToCalendar(new JTextArea("Skal ikke bli lagt til"), CalendarPanel.CALENDAR_X_START, 150, CalendarPanel.DAY_WIDTH, CalendarPanel.HOUR_HEIGHT, cp);
		cp.addAppointmentToCalendar(new JTextArea("Skal bli lagt til"), 145, 150, CalendarPanel.DAY_WIDTH, CalendarPanel.HOUR_HEIGHT, cp);
		
		cp.removeAppointmentFromCalendar(cp.existingAppointments.get(4), cp);
		
		cp.addAppointmentToCalendar(new JTextArea("ny 4'er"), CalendarPanel.CALENDAR_X_START, 190, CalendarPanel.DAY_WIDTH, 40, cp);
		cp.addAppointmentToCalendar(new JTextArea("4444"), CalendarPanel.CALENDAR_X_START, 30, CalendarPanel.DAY_WIDTH, 80, cp);
		
		System.out.println(cp.existingAppointments.size());
	}

}
