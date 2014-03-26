package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import client.ClientDBCalls;
import fp2014.Appointment;
import fp2014.User;

@SuppressWarnings("serial")
public class CalendarPanel extends JPanel implements FocusListener {
	
	public static final int HOUR_HEIGHT = 40;
	public static final int DAY_WIDTH = 105;
	public static final int CALENDAR_X_START = 60;
	public static final int CALENDAR_Y_START = 10;
	public static final int HOURS = 24;
	public static final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	
	private ArrayList<Appointment> appointments;
	private ArrayList<appointmentBubble> existingAppointments = new ArrayList<appointmentBubble>();
	private ArrayList<String> daySpan;
	private JLayeredPane layeredPane;
	private JSeparator hSep, vSep;
	private MainPanel parent;
	private int year, week;
	private JScrollPane s;
	private User user;
	
	public CalendarPanel(MainPanel parent, User user, ArrayList<User> users, int week, int year){
		this.parent = parent;
		this.week = week;
		this.year = year;
		this.user = user;

		daySpan = new ArrayList<>();
		setBackground(Color.LIGHT_GRAY);
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		setDaySpan();
		
		appointments = ClientDBCalls.getAppointmentsInInterval(users, daySpan);
	
		for (Appointment a: appointments){
			a.setParticipants(ClientDBCalls.getAttendants(a.getAppointmentNr()));
		}
		
		JLabel label = null;
		
		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(804, 1000));
		layeredPane.setLayout(null);
		
		for (int i = 0; i < HOURS; i++) {
			label = new JLabel( String.format("%02d", i)+":00");
			label.setFont(new Font("Lucida Grande", Font.BOLD, 12));
			label.setBounds(20, CALENDAR_Y_START - 17 +(i*40), 50, 40);
			layeredPane.add(label, 0);
		}
		
		JLabel spacer = new JLabel("");	
		spacer.setPreferredSize(new Dimension(60, 5));
		this.add(spacer);
		for (int i = 0; i < days.length; i++) {
			this.addDay(days[i], i, CALENDAR_X_START + 30 +(i*CalendarPanel.DAY_WIDTH), CALENDAR_Y_START, 100, 40, layeredPane);
		}
		
		for (int i = 0; i <= HOURS; i++) {
			hSep = new JSeparator(JSeparator.HORIZONTAL);
			hSep.setBounds(CalendarPanel.CALENDAR_X_START, CALENDAR_Y_START+(i*CalendarPanel.HOUR_HEIGHT), 735, 5);			
			layeredPane.add(hSep, 0);
		}
		
		for (int i = 0; i <= days.length; i++) {
			vSep = new JSeparator(JSeparator.VERTICAL);
			vSep.setBounds(CalendarPanel.CALENDAR_X_START+(i*CalendarPanel.DAY_WIDTH), CALENDAR_Y_START, 5, 960);
			layeredPane.add(vSep, 0);			
		}
		
		s = new JScrollPane(layeredPane, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		s.setPreferredSize(new Dimension(804, 500));
		
		add(s);
		
		doImportantStuff();
		// Set scroll-bar position 
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			   public void run() { s.getVerticalScrollBar().setValue(245); }
		});
		
	}
	
	public void addDay(String day, int i, int x, int y, int width, int height, JLayeredPane panel){
		JLabel label = new JLabel("<html>" + day + "<br>" + daySpan.get(i) + "</html>");
		label.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		label.setForeground(Color.WHITE);
		label.setBounds(x, y, width, height);
		label.setPreferredSize(new Dimension(106, 45));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		add(label);
	}
	
	public void addAppointmentToCalendar(appointmentBubble ab, int x, int y, int width, int height, Appointment appointment){
		//ab.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		ab.setLineWrap(true);
		ab.setBounds(x, y, width, height);
		ab.setForeground(Color.DARK_GRAY);
		ab.setBackground(appointment.getStatusColor());
		ab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		this.layeredPane.add(ab, 1);
	}
	
	public void doImportantStuff(){
		ArrayList<String[]> intervalWidth = new ArrayList<>();

		
		String aStart, aEnd;
		for (int i = 0; i < appointments.size(); i++){
			aStart = appointments.get(i).getStartTime();
			aEnd = appointments.get(i).getEndTime();
			
			boolean overlap = false;
			for (int j = 0; j < intervalWidth.size(); j++){
				
				String iStart = intervalWidth.get(j)[0];
				String iEnd = intervalWidth.get(j)[1];

				if(checkOverlap(aStart, aEnd, iStart, iEnd) && (Integer.parseInt(intervalWidth.get(j)[4]) == dateToDayNumber(appointments.get(i).getDate()))){
					overlap = true;
					intervalWidth.get(j)[2] = String.valueOf((Integer.parseInt(intervalWidth.get(j)[2])+1));
					if(isLater(aEnd, iEnd)) { intervalWidth.get(j)[1] = aEnd; }
					else if(isLater(iStart, aStart)) { intervalWidth.get(j)[0] = aStart; }
					break;
				}
			}
			
			if(!overlap) {
				String[] newInterval = {aStart, aEnd, "1", "0", String.valueOf(dateToDayNumber(appointments.get(i).getDate()))};
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
				if(checkOverlap(ioStart, ioEnd, iocStart, iocEnd) && (Integer.parseInt(intervalWidth.get(j)[4]) == dateToDayNumber(appointments.get(i).getDate()))){
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
		
		for (int i = 0; i < appointments.size(); i++){
			for (int j = 0; j < intervalWidth.size(); j++){
				if(checkOverlap(appointments.get(i).getStartTime(), appointments.get(i).getEndTime(), intervalWidth.get(j)[0], intervalWidth.get(j)[1]) && (Integer.parseInt(intervalWidth.get(j)[4]) == dateToDayNumber(appointments.get(i).getDate()))){
					xPos = Integer.parseInt(intervalWidth.get(j)[3]);	
					eventWidth = width/Integer.parseInt(intervalWidth.get(j)[2]);
					
					appointmentBubble currentEvent = new appointmentBubble(this, user, appointments.get(i));
					
					currentEvent.setFocused(false);
					existingAppointments.add(currentEvent);
					
					addAppointmentToCalendar(currentEvent, CALENDAR_X_START + dateToDayNumber(appointments.get(i).getDate()) * width + xPos * eventWidth, (int)(HOUR_HEIGHT * hourToX(appointments.get(i).getStartTime()) + CALENDAR_Y_START), eventWidth, (int)(CalendarPanel.HOUR_HEIGHT * durance(appointments.get(i).getStartTime(), appointments.get(i).getEndTime())), appointments.get(i));
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
	    c.set(Calendar.MONTH, Integer.parseInt(parts[1])-1);
	    c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[0]));
	    int weekDay = (c.get(Calendar.DAY_OF_WEEK));
	    if(weekDay > 7) { weekDay -= 7; }
	    if(weekDay == 1) { return 6; }
	    else { return weekDay - 2; }
	}
	
	private void setDaySpan(){
		//Locale.UK fikser at datoen vises riktig uansett maskin
		Calendar c = Calendar.getInstance(Locale.UK);
		c.set(Calendar.WEEK_OF_YEAR, week);
		c.set(Calendar.YEAR, year); 
		
		int[] days = {Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY};
		int day, month;
		
		for (int i = 0; i < days.length; i++) {
			c.set(Calendar.DAY_OF_WEEK, days[i]);
			day = c.get(Calendar.DAY_OF_MONTH);
		    month = c.get(Calendar.MONTH) + 1;
		    year = c.get(Calendar.YEAR);
		    String dateEnd = "." + month + "." + year;
			daySpan.add(day + dateEnd);
		}
	}
	
	public void unSelectAllAppointments(){
		int i = 0;
		for (JTextArea eta: existingAppointments){
			eta.setName("");
			eta.setBackground(appointments.get(i++).getStatusColor());
			eta.setForeground(Color.DARK_GRAY);
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		
		int index = existingAppointments.indexOf(e.getSource());
		int i = 0;
		
		for (appointmentBubble ab: existingAppointments){
			if (ab.isFocused() && existingAppointments.indexOf(ab) != index){
				ab.setName("");
				ab.setBackground(appointments.get(i).getStatusColor());
				ab.setForeground(Color.DARK_GRAY);
			}
			i++;
		}
		
		//appointments.get(x);
		existingAppointments.get(index).setFocused(true);
		existingAppointments.get(index).setBackground(Color.LIGHT_GRAY);
		existingAppointments.get(index).setForeground(Color.WHITE);
		
		parent.addNewPanel("avtale", new ShowAppointmentPanel(parent, user, appointments.get(index)));
	}

	@Override
	public void focusLost(FocusEvent e) {
	}

}
