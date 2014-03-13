package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Calendar;

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
	public static final int CALENDAR_X_START = 60;
	public static final int CALENDAR_Y_START = 10;
	
	private ArrayList<Component> existingAppointments = new ArrayList<Component>();
	private JPanel panel;
	private KalenderView parent;
	
	public CalendarPanel(KalenderView parent){
		this.parent = parent;
		
		JSeparator hSep, vSep;
		JLabel label = null;
		int hours = 24;
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(804, 1000));
		panel.setLayout(null);
		
		for (int i = 0; i < hours; i++) {
			label = new JLabel( String.format("%02d", i)+":00");
			
			label.setBounds(20, CALENDAR_Y_START - 17 +(i*40), 50, 40);
			panel.add(label);
		}
		
		String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
		
		for (int i = 0; i < days.length; i++) {
			this.addDay(days[i], CALENDAR_X_START + 30 +(i*CalendarPanel.DAY_WIDTH), CALENDAR_Y_START, 100, 40, panel);
		}
		
		for (int i = 0; i <= hours; i++) {
			hSep = new JSeparator(JSeparator.HORIZONTAL);
			hSep.setBounds(CalendarPanel.CALENDAR_X_START, CALENDAR_Y_START+(i*CalendarPanel.HOUR_HEIGHT), 735, 5);			
			panel.add(hSep);
		}
		
		for (int i = 0; i <= days.length; i++) {
			vSep = new JSeparator(JSeparator.VERTICAL);
			vSep.setBounds(CalendarPanel.CALENDAR_X_START+(i*CalendarPanel.DAY_WIDTH), CALENDAR_Y_START, 5, 960);
			panel.add(vSep);			
		}
		
		JScrollPane s = new JScrollPane(panel, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		s.setPreferredSize(new Dimension(804, 500));
		
		add(s);
		
		doImportantStuff();
		
	}
	
	public void addDay(String day, int x, int y, int width, int height, JPanel panel){
		JLabel label = new JLabel(day);
		label.setForeground(Color.WHITE);
		label.setBounds(x, y, width, height);
		add(label);
	}
	
	public void addAppointmentToCalendar(Component c, int x, int y, int width, int height, CalendarPanel panel){
		c.setBounds(x, y, width, height);
		panel.panel.add(c);
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
	
	public void doImportantStuff(){
		
		
		ArrayList<String[]> intervalWidth = new ArrayList<>();
		class Avtale{
			public String start, end, date;
			
			
			public Avtale(String start, String end, String date){
				this.start = start;
				this.end = end;
				this.date = date;
			}
		}
		
		ArrayList<Avtale> avtaler = new ArrayList<>();
		avtaler.add(new Avtale("10:00", "12:00", "13.03.2014"));
		avtaler.add(new Avtale("13:00", "15:00", "13.03.2014"));
		avtaler.add(new Avtale("11:00", "14:00", "13.03.2014"));
		avtaler.add(new Avtale("16:00", "17:00", "14.03.2014"));
		avtaler.add(new Avtale("11:00", "12:00", "13.03.2014"));
		avtaler.add(new Avtale("11:00", "12:00", "16.03.2014"));

		
		
		String aStart, aEnd;
		for (int i = 0; i < avtaler.size(); i++){
			aStart = avtaler.get(i).start;
			aEnd = avtaler.get(i).end;
			
			boolean overlap = false;
			for (int j = 0; j < intervalWidth.size(); j++){
				
				String iStart = intervalWidth.get(j)[0];
				String iEnd = intervalWidth.get(j)[1];
				
				if(checkOverlap(aStart, aEnd, iStart, iEnd) && (Integer.parseInt(intervalWidth.get(j)[4]) == dateToDayNumber(avtaler.get(i).date))){
					overlap = true;
					intervalWidth.get(j)[2] = String.valueOf((Integer.parseInt(intervalWidth.get(j)[2])+1));
					if(isLater(aEnd, iEnd)) { intervalWidth.get(j)[1] = aEnd; }
					else if(isLater(iStart, aStart)) { intervalWidth.get(j)[0] = aStart; }
					break;
				}
			}
			
			if(!overlap) {
				String[] newInterval = {aStart, aEnd, "1", "0", String.valueOf(dateToDayNumber(avtaler.get(i).date))};
				intervalWidth.add(newInterval); }
		
		}
		
		ArrayList<Integer> deleteThese = new ArrayList<>();
		
		String ioStart, ioEnd, iocStart, iocEnd;
		for (int i = 0; i < intervalWidth.size(); i++){
			ioStart = intervalWidth.get(i)[0];
			ioEnd = intervalWidth.get(i)[1];
			for (int j = i+1; j < intervalWidth.size(); j++){
				iocStart = intervalWidth.get(j)[0];
				iocEnd = intervalWidth.get(j)[1];
				if(checkOverlap(ioStart, ioEnd, iocStart, iocEnd) && (Integer.parseInt(intervalWidth.get(j)[4]) == dateToDayNumber(avtaler.get(i).date))){
					deleteThese.add(j);
					if(isLater(iocEnd, ioEnd)) { intervalWidth.get(i)[1] = iocEnd; }
					else if(isLater(ioStart, iocStart)) { intervalWidth.get(i)[0] = iocStart; }
					intervalWidth.get(i)[2] = String.valueOf((Integer.parseInt(intervalWidth.get(i)[2]) + (Integer.parseInt(intervalWidth.get(j)[2]))));
				}
				
			}
		}
		
		for (int i = 0; i < deleteThese.size(); i++){
			intervalWidth.remove((int)deleteThese.get(i));
		}
		

		
	
		
		int width = CalendarPanel.DAY_WIDTH;
		int xPos, eventWidth;
		
		for (int i = 0; i < avtaler.size(); i++){
			for (int j = 0; j < intervalWidth.size(); j++){
				if(checkOverlap(avtaler.get(i).start, avtaler.get(i).end, intervalWidth.get(j)[0], intervalWidth.get(j)[1])){
					xPos = Integer.parseInt(intervalWidth.get(j)[3]);	
					eventWidth = width/Integer.parseInt(intervalWidth.get(j)[2]);
					
					JTextArea currentEvent = new JTextArea(avtaler.get(i).start + " - " + avtaler.get(i).end);
					currentEvent.setEditable(false);
					this.addAppointmentToCalendar(currentEvent, CALENDAR_X_START + dateToDayNumber(avtaler.get(i).date) * width + xPos * eventWidth, (int)(HOUR_HEIGHT * hourToX(avtaler.get(i).start) + CALENDAR_Y_START), eventWidth, CalendarPanel.HOUR_HEIGHT * (int)durance(avtaler.get(i).start, avtaler.get(i).end), this);
					intervalWidth.get(j)[3] = String.valueOf((Integer.parseInt(intervalWidth.get(j)[3])+1));
					break;
				}
			}
		}
	}
	
	private boolean checkOverlap(String from1, String to1, String from2, String to2){
		// Convert strings to floats
		float from1float = Float.parseFloat(from1.replace(":", "."));
		float from2float = Float.parseFloat(from2.replace(":", "."));
		float to1float = Float.parseFloat(to1.replace(":", "."));
		float to2float = Float.parseFloat(to2.replace(":", "."));
		
		// Check for overlap
		return (from2float > from1float && from2float < to1float) || (to2float > from1float && to2float < to1float) || (from2float <= from1float && to2float >= to1float);
	}
	
	private boolean isLater(String time1, String time2){
		float time1float = Float.parseFloat(time1.replace(":", "."));
		float time2float = Float.parseFloat(time2.replace(":", "."));
		
		return time1float > time2float;
	}
	
	private float durance(String from, String to){
		
		String[] fromPart = from.split(":");
		String[] toPart = to.split(":");
		
		float fromHFloat = Float.parseFloat(fromPart[0]);
		float fromMFloat = Float.parseFloat(fromPart[1]);
		float toHFloat = Float.parseFloat(toPart[0]);
		float toMFloat = Float.parseFloat(toPart[1]);
		
		return toHFloat - fromHFloat + (toMFloat - fromMFloat)/60;
	}
	
	private float hourToX(String hour){
		String[] parts = hour.split(":");
		float hours = Float.parseFloat(parts[0]);
		float minutes = Float.parseFloat(parts[1]);
		
		return hours + minutes/60;
	}
	
	private int dateToDayNumber(String date){
		String[] parts = date.split("\\.");
	    Calendar c = Calendar.getInstance();
	    c.set(Calendar.YEAR, Integer.parseInt(parts[2]));
	    c.set(Calendar.MONTH, Integer.parseInt(parts[1]));
	    c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[0]));
	    int weekDay = (c.get(Calendar.DAY_OF_WEEK) + 3);
	    if(weekDay > 7) { weekDay -= 7; }
	    return weekDay;

		
	}

}
