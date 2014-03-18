package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.MaskFormatter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.toedter.calendar.JDateChooser;

import database.DBHandler;
import fp2014.Appointment;
import fp2014.Mail;
import fp2014.User;

@SuppressWarnings("serial")
public class EditAppointmentPanel extends JPanel implements ActionListener, FocusListener{

	private JTextField nameField;
	private JTextArea descriptionField;
	private JLabel startTimeLabel;
	private JLabel endTimeLabel;
	private JLabel feedback;
	private JFormattedTextField startTimeField;
	private JFormattedTextField endTimeField;
	private JDateChooser dateChooser;
	private JButton destinationBtn;
	public JTextField showLocationField;
	private JButton inviteViaEmailBtn;
	private JButton manageParticipantsBtn;
	private JButton saveBtn;
	private JButton cancelBtn;
	protected MainFrame parent;
	protected User user;
	protected Appointment appointment;
	private MaskFormatter formatter;
	public ArrayList<String> emailParticipants;
	private HashMap<User, Integer> participants;
	private JComboBox<String> duration;

	public EditAppointmentPanel(MainFrame parent, User ansatt, Appointment appointment) {

		this.appointment = appointment;
		this.parent = parent;
		this.user = ansatt;
		this.participants = appointment.getParticipants();

		setPreferredSize(new Dimension(220, 500));
		
		emailParticipants = new ArrayList<String>();
		
		nameField = new JTextField("Appointment name");
		descriptionField = new JTextArea("Description");
		try {
			formatter = new MaskFormatter("##:##");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		formatter.setPlaceholderCharacter('_');

		startTimeLabel = new JLabel("From:");
		endTimeLabel = new JLabel("To:");
		feedback = new JLabel(" ");
		startTimeField = new JFormattedTextField(formatter);
		endTimeField = new JFormattedTextField(formatter);
		dateChooser = new JDateChooser();
		destinationBtn = new JButton("Select location");
		showLocationField = new JTextField("Location not chosen");
		manageParticipantsBtn = new JButton("Manage participants");
		saveBtn = new JButton("Save");
		cancelBtn = new JButton("Cancel");
		inviteViaEmailBtn = new JButton("Invite participants via email");
		duration = new JComboBox<String>();
		duration.setPrototypeDisplayValue("xx:xx");
		addDurationsToBox();
		
		startTimeField.setName("EAPstartTimeField");
		endTimeField.setName("EAPendTimeField");
		dateChooser.setName("EAPdateChooser");
		destinationBtn.setName("EAPdestinationButton");
		showLocationField.setName("EAPshowLocationField");
		manageParticipantsBtn.setName("EAPmanageParticipantsButton");
		saveBtn.setName("EAPsaveButton");
		cancelBtn.setName("EAPcancelButton");
		inviteViaEmailBtn.setName("EAPinviteViaEmailButton");
		duration.setName("EAPduration");
		
		feedback.setForeground(Color.RED);
		
		if (appointment.getName() != null){
			loadExistingAppointment();
		}

		JScrollPane scroll = new JScrollPane(descriptionField);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    showLocationField.setEditable(false);
		
	    duration.addActionListener(this);
		inviteViaEmailBtn.addActionListener(this);
		saveBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
		destinationBtn.addActionListener(this);
		manageParticipantsBtn.addActionListener(this);
		descriptionField.addFocusListener(this);
		nameField.addFocusListener(this);
		startTimeField.addFocusListener(this);
		endTimeField.addFocusListener(this);
		
		this.setLayout(null);
		
		nameField.setBounds(5,5,210,25);
		descriptionField.setBounds(5, 35, 150, 100);
		scroll.setBounds(5, 35, 210, 100);
		startTimeLabel.setBounds(5, 137, 50, 25);
		endTimeLabel.setBounds(5, 163, 50, 25);
		startTimeField.setBounds(40, 140, 50, 20);
		endTimeField.setBounds(40, 165, 50, 20);
		duration.setBounds(100,140,115,20);
		dateChooser.setBounds(100,165,115,20);
		destinationBtn.setBounds(5,190,210,25);
		showLocationField.setBounds(5,220,210,25);
		manageParticipantsBtn.setBounds(5,253,210,25);
		inviteViaEmailBtn.setBounds(5,283,210,25);
		feedback.setBounds(5,310,210,25);
		saveBtn.setBounds(5,470,102,25);
		cancelBtn.setBounds(112,470,103,25);
		
		this.add(nameField);
		this.add(scroll);
		this.add(startTimeLabel);
		this.add(endTimeLabel);
		this.add(startTimeField);
		this.add(endTimeField);
		this.add(dateChooser);
		this.add(duration);
		this.add(destinationBtn);
		this.add(showLocationField);
		this.add(manageParticipantsBtn);
		this.add(inviteViaEmailBtn);
		this.add(feedback);
		this.add(saveBtn);
		this.add(cancelBtn);
		
		descriptionField.setPreferredSize(new Dimension(150, 100));
		descriptionField.setLineWrap(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == saveBtn) {
			if (checkDate() != null) {
				feedback.setText(checkDate());
			} 
			else if (showLocationField.getText().equals("Location not chosen")){
				feedback.setText("Choose a location first");
			}
			else {
				parent.addNewPanel("avtale", new DefaultRightPanel(parent, user)); // Exits the edit meny
				boolean nullAppointment = false;
				
				if (appointment.getName() == null){
					nullAppointment = true;
				}
				LocalDate appointmentDate = new DateTime(dateChooser.getDate()).toLocalDate();
				this.appointment.edit(nameField.getText(),startTimeField.getText(), endTimeField.getText(),descriptionField.getText(), toOtherDateFormat(appointmentDate), user, appointment.getParticipants());
				
				//Opprett/Endre AnsattAvtaler
				appointment.getParticipants().put(user, 1);
				if (nullAppointment) {
					appointment.sendAppoinmentToDatabase();					
					int appCount = DBHandler.getCountOfAppointments();
					
					for (User a : appointment.getParticipants().keySet()) {
						DBHandler.createAttendance(a.getUsername(), appCount, appointment.getParticipants().get(a), 0);
					}
				} else {
					DBHandler.updateAppointment(appointment);
					DBHandler.deleteAttendances(appointment.getAppointmentNr());
					
					for (User a : appointment.getParticipants().keySet()) {
						DBHandler.createAttendance(a.getUsername(), appointment.getAppointmentNr(), appointment.getParticipants().get(a), 1);
					}
				}
				
				//Sender mail til evt eksterne deltagere
				sendMailInvitations();
				
				// Update the calendar panel
				parent.addNewPanel("kalender", new CalendarPanel(parent, user, parent.getShowUsers(), parent.getWeek(), parent.getYear()));
				
				// Oppdaterer kalenderen til å vise ingen valgt avtale
				((CalendarPanel) parent.kalender).unSelectAllAppointments();
				
			}

		} else if (s == inviteViaEmailBtn){
			new SendEmailPanel(this, emailParticipants);
		}
		
		else if (s == cancelBtn) {
			parent.addNewPanel("avtale", new DefaultRightPanel(parent, user));
			
			// Oppdaterer kalenderen til å vise ingen valgt avtale
			((CalendarPanel) parent.kalender).unSelectAllAppointments();
			
		} else if (s == destinationBtn) {

			if (checkDate() == null) {
				new RoomPanel(this, toOtherDateFormat((LocalDate)new DateTime(dateChooser.getDate()).toLocalDate()),startTimeField.getText(), endTimeField.getText());
			} else {
				feedback.setText(checkDate());
			}

		} else if (s == manageParticipantsBtn) {
			new ManageParticipantsPanel(this, appointment.getParticipants());
		} else if (s == duration && !startTimeField.getText().equals("__:__") && duration.getSelectedIndex() != 0){
			int ihh = Integer.parseInt(startTimeField.getText().substring(0, 2));
			int imm = Integer.parseInt(startTimeField.getText().substring(3));
			
			int nhh = ihh + Integer.parseInt(((String) duration.getSelectedItem()).substring(0, 2));
			int nmm = imm + Integer.parseInt(((String) duration.getSelectedItem()).substring(3));
			
			if (nmm >= 60){
				nmm -= 60;
				nhh++;
			}
			
			if (nhh > 23){
				duration.setSelectedIndex(0);
				endTimeField.setText("__:__");
				return;
			}
			
			endTimeField.setText(nhh + ":" + String.format("%02d", nmm));
			endTimeField.setEditable(false);
		}else{
			endTimeField.setEditable(true);
		}
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	
	public void setEmailParticipants(ArrayList<String> participantsList){
		this.emailParticipants = participantsList;
	}
	
	public ArrayList<String> getEmailParticipants(){
		return emailParticipants;
	}
	
	public HashMap<User, Integer> getParticipants(){
		return this.participants;
	}
	
	public void setParticipants(HashMap<User, Integer> participants){
		this.participants = participants;
	}

	public String checkDate() {
		if (startTimeField.getText().contains("_") || endTimeField.getText().contains("_")) {
			return ("Enter a valid time");
		} else {
			String[] start = startTimeField.getText().split(":");
			int startH = Integer.parseInt(start[0]);
			int startM = Integer.parseInt(start[1]);

			String[] end = endTimeField.getText().split(":");
			int endH = Integer.parseInt(end[0]);
			int endM = Integer.parseInt(end[1]);
			
			if (new DateTime(dateChooser.getDate()).toLocalDate().compareTo(
					new LocalDate()) < 0) {
				return ("Choose a valid date");
			} else if (dateChooser.getDate() == null) {
				return ("Choose a date first");
			} else if ((startM > 59) || (endM > 59) || (startH > 23) || (endH > 23) || (startM < 0) || (endM < 0) || (startH < 0) || (endH < 0)) {
				return ("Enter a valid time");
			} else if (startH > endH || (startH == endH && startM > endM) ) {
				return ("Enter a valid time interval");
			}
		}
		return null;
	}
	
	public void sendMailInvitations(){
		String destination = "unknown";
		
		if (appointment.getPlace() != null){
			destination = appointment.getPlace();
		} else if (appointment.getRom() != null) {
			destination = appointment.getRom().getPlace();
		}
		
		String content = "You have been invited to the appointment "+ appointment.getName() 
				+ ". The meeting is about \"" + appointment.getDescription() 
				+ "\", and takes place " + appointment.getDate() + " " + appointment.getStartTime() + "-" + appointment.getEndTime()
				+ " at " + destination
				+ "\n\n Best Regards, \n\n" + user.getFirstname() + " " + user.getLastname() + "\n" + user.getEmail();
		
		for (String mail : emailParticipants){
			new Mail(mail,user.getEmail(),user.getFirstname()+" "+user.getLastname(), "Meeting invitation to "+ appointment.getName(), content);
		}
	}
	
	public void loadExistingAppointment(){
		nameField = new JTextField(appointment.getName());
		descriptionField = new JTextArea(appointment.getDescription());
		
		dateChooser = new JDateChooser(toDateFormat(appointment.getDate()));
		
		startTimeField.setText(appointment.getStartTime());
		endTimeField.setText(appointment.getEndTime());
		
		if (appointment.hasReservedRoom()){
			showLocationField = new JTextField(appointment.getRom().getPlace());
		} else {
			showLocationField = new JTextField(appointment.getPlace());
		}
	}
	
	public Date toDateFormat(String wrongFormatDate) {

		String[] dateParts = wrongFormatDate.split("\\.");
		String dd = dateParts[0];
		String mm = dateParts[1];
		String yyyy = dateParts[2];

		if (dd.length() == 1){
			dd = "0"+dd;
		} 
		if (mm.length() == 1){
			mm = "0"+mm;
		}
		
		String oldDate = yyyy+"-"+mm+"-"+dd;
		
		LocalDate localDate = new LocalDate(oldDate);
		Date date = localDate.toDateTimeAtStartOfDay().toDate();
		
		return date;
	}
	
	public String toOtherDateFormat (LocalDate wrongFormatDate) {
		
		String[] dateParts = wrongFormatDate.toString().split("\\-");
		int yyyy = Integer.parseInt(dateParts[0]);
		int mm = Integer.parseInt(dateParts[1]);
		int dd = Integer.parseInt(dateParts[2]);
		
		String date = dd+"."+mm+"."+yyyy;
		
		return date;
	}

	public void addDurationsToBox(){
		duration.addItem("Durance");
		duration.addItem("00:15");
		duration.addItem("00:30");
		duration.addItem("00:45");
		duration.addItem("01:00");
		duration.addItem("01:15");
		duration.addItem("01:30");
		duration.addItem("01:45");
		duration.addItem("02:00");
		duration.addItem("02:15");
		duration.addItem("02:30");
		duration.addItem("02:45");
		duration.addItem("03:00");
		duration.addItem("03:15");
		duration.addItem("03:30");
		duration.addItem("03:45");
		duration.addItem("04:00");
		duration.addItem("04:15");
		duration.addItem("04:30");
		duration.addItem("04:45");
		duration.addItem("05:00");
		duration.addItem("05:15");
		duration.addItem("05:30");
		duration.addItem("05:45");
		duration.addItem("06:00");
		duration.addItem("06:15");
		duration.addItem("06:30");
		duration.addItem("07:45");
		duration.addItem("07:00");
		duration.addItem("07:15");
		duration.addItem("07:30");
		duration.addItem("07:45");
		duration.addItem("08:00");
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (e.getSource() == descriptionField){
			descriptionField.selectAll();
		} else if (e.getSource() == nameField) {
			nameField.selectAll();
		} else if (e.getSource() == startTimeField){
			startTimeField.selectAll();
		} else if (e.getSource() == endTimeField){
			endTimeField.selectAll();
		}
		
	}
	
	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == descriptionField){
			descriptionField.select(0, 0);
		} else if (e.getSource() == nameField) {
			nameField.select(0, 0);
		} else if (e.getSource() == startTimeField){
			startTimeField.select(0, 0);
		} else if (e.getSource() == endTimeField){
			endTimeField.select(0, 0);
		}
		
	}
}
