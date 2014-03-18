package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import database.DBHandler;
import fp2014.Appointment;
import fp2014.User;

@SuppressWarnings("serial")
public class ShowAppointmentPanel extends JPanel implements ActionListener{

	private JLabel name, time, place, attending, alert, hide;
	private JTextArea description;
	private DefaultListModel<String> participantListModel;
	private JList<String> participants;
	private JScrollPane scrollDescription, scrollParticipants;
	private JRadioButton yes, no;
	private JComboBox<String> alertBox;
	private JButton editBtn, deleteBtn, saveBtn, cancelBtn;
	private JCheckBox hideBox;
	private Appointment appointment;
	private MainFrame parent;
	private User user;
	private boolean isOwner, isParticipant = false;
	private int yourStatus;
	
	public ShowAppointmentPanel(MainFrame parent, User user, Appointment appointment){
		
		this.parent = parent;
		this.appointment = appointment;
		this.user = user;
		
		if(appointment.getParticipants().size() == 0){ appointment.setParticipants(DBHandler.getAttendants(appointment.getAppointmentNr())); }
		
		isOwner = (appointment.getMadeBy().getUsername().equals(user.getUsername()));
		
		DBHandler.updateChanged(user.getUsername(), appointment.getAppointmentNr());
		
		this.setPreferredSize(new Dimension(220, 500));
		
		if (appointment.getName().length() > 13){
			name = new JLabel(appointment.getName().substring(0, 10) + "...");
		}else{
			name = new JLabel(appointment.getName());
		}
		
		name.setFont(new Font("Arial", Font.PLAIN, 26));
		
		time = new JLabel(appointment.getDate() + " at " + appointment.getStartTime() + "-" + appointment.getEndTime());
		
		if (appointment.getPlace() != null){
			place = new JLabel("Place: " + appointment.getPlace());
		}else{
			place = new JLabel("Room: " + appointment.getRom().getPlace());
		}
		attending = new JLabel("Attending:");
		alert = new JLabel("Alert:");
		hide = new JLabel("Hide:");
		
		description = new JTextArea(appointment.getDescription());
		description.setEditable(false);
		description.setLineWrap(true);
		
		// Create Swing object

		yes = new JRadioButton("yes");
		yes.addActionListener(this);
		no = new JRadioButton("no");
		no.addActionListener(this);
		hideBox = new JCheckBox("yes");
		
		
		
		// Fetch attendants and their statuses from database
		
		
		// Fill participant list
		participantListModel = new DefaultListModel<String>();
		int status;
		
		for (User a : appointment.getParticipants().keySet()){
			
			if(a.getUsername().equals(user.getUsername())){
				yourStatus = appointment.getParticipants().get(a);
				isParticipant = true;
				this.user = a;
			}
			
			status = appointment.getParticipants().get(a);
			String tmp = a.getFirstname() + " " + a.getLastname() + " - ";
			if (status == 1){
				tmp += "attending";
			}else if (status == 0){
				tmp += "not attending";
			}
			participantListModel.addElement(tmp);
		}
		
		participants = new JList<String>(participantListModel);
		participants.setEnabled(false);
		
		scrollDescription = new JScrollPane(description);
		scrollDescription.setPreferredSize(new Dimension(200, 75));
		
		scrollParticipants = new JScrollPane(participants);
		scrollParticipants.setPreferredSize(new Dimension(200, 80));
		
		
		// Check if you are attending or not. Update checkboxes.
		
		setStatus(yourStatus);
		
		// Generate alart combobox
		alertBox = new JComboBox<String>();
		alertBox.setPrototypeDisplayValue("xx minutes "); // Set JComboBox size 
		alertBox.addItem("none");
		alertBox.addItem("5 minutes before");
		alertBox.addItem("10 minutes before");
		alertBox.addItem("15 minutes before");
		alertBox.addItem("30 minutes before");
		alertBox.addItem("1 hour before");
		alertBox.addItem("2 hours before");
		alertBox.addItem("1 day before");
		alertBox.addItem("2 days before");
		
		alertBox.setSelectedIndex(DBHandler.getAlarmType(user.getUsername(), appointment.getAppointmentNr())); // Set index of alert box to previously chosen option
		
		editBtn = new JButton("Edit");
		deleteBtn = new JButton("Delete");
		saveBtn = new JButton("Save");
		cancelBtn = new JButton("Cancel");
		
		editBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		saveBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
		
		// NYTT ------------------------------------------------------------------------------------------------------
		
		this.setLayout(null);
		
		name.setBounds(5,5,210,25);
		time.setBounds(5,30,210,25);
		place.setBounds(5,47,210,25);
		scrollDescription.setBounds(5,70,210,75);		
		attending.setBounds(5,145,70,25);
		yes.setBounds(80,147,50,20);
		no.setBounds(130,147,50,20);
		alert.setBounds(5,170,40,25);
		alertBox.setBounds(50,170,165,25);
		hide.setBounds(5,195,40,25);
		hideBox.setBounds(50,195,165,25);
		scrollParticipants.setBounds(5,225,210,75);
		
		deleteBtn.setBounds(5,438,103,25);
		editBtn.setBounds(113,438,102,25);
		
		cancelBtn.setBounds(5,468,103,25);
		saveBtn.setBounds(113,468,102,25);
		
		this.add(name);
		this.add(time);
		this.add(place);
		this.add(scrollDescription);
		this.add(scrollParticipants);
		this.add(attending);
		this.add(yes);
		this.add(no);
		this.add(alert);
		this.add(alertBox);
		this.add(hide);
		this.add(hideBox);
		this.hideBox.setEnabled(no.isSelected());
		this.add(editBtn);
		this.add(deleteBtn);
		this.add(cancelBtn);
		this.add(saveBtn);
		
		if (!isOwner){
			editBtn.setEnabled(false);
			editBtn.setVisible(false);
		}
		
		if(!isParticipant){
			attending.setVisible(false);
			yes.setVisible(false);
			no.setVisible(false);
			alert.setVisible(false);
			alertBox.setVisible(false);
			hide.setVisible(false);
			hideBox.setVisible(false);
			saveBtn.setVisible(false);
			deleteBtn.setVisible(false);
		}
		
		editBtn.setName("SAPeditButton");
		deleteBtn.setName("SAPdeleteButton");
		saveBtn.setName("SAPsaveButton");
		cancelBtn.setName("SAPcancelButton");
		participants.setName("SAPparticipants");
		description.setName("SAPdescription");
		yes.setName("SAPyes");
		no.setName("SAPno");
		alertBox.setName("SAPalertBox");
		hideBox.setName("SAPhideBox");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		
		if (s == yes){
			if (no.isSelected()){
				no.setSelected(false);
			}
			hideBox.setEnabled(false);
			hideBox.setSelected(false);
		}else if(s == no){
			if (yes.isSelected()){
				yes.setSelected(false);
			}
			if (no.isSelected()){
				hideBox.setEnabled(true);
			}else{
				hideBox.setEnabled(false);
				hideBox.setSelected(false);
			}
		}else if(s == cancelBtn){
			parent.addNewPanel("avtale", new DefaultRightPanel(parent, user));
			
			// Oppdaterer kalenderen til aa vise ingen valgt avtale
			((CalendarPanel) parent.kalender).unSelectAllAppointments();
			
		}else if(s == editBtn){
			parent.addNewPanel("avtale", new EditAppointmentPanel(parent, user, appointment));
			
		}else if(s == saveBtn){
			
			int newStatus = getStatus();
			// Save attending status
			if(yourStatus != newStatus){ // Only update if status has changed.
				DBHandler.updateAttendance(user.getUsername(), appointment.getAppointmentNr(), newStatus); // Updates database
			}
			
			//Save hidden
			if(hideBox.isSelected()){
				DBHandler.updateHidden(user.getUsername(), appointment.getAppointmentNr(), 1);
			}else{
				DBHandler.updateHidden(user.getUsername(), appointment.getAppointmentNr(), 0);
			}
			
			// Save alarm
			DBHandler.deleteAlarm(user.getUsername(), appointment.getAppointmentNr()); // Deletes existing alarm before creating a new one.
			
			if(!alertBox.getSelectedItem().equals("none")){
				int back = 0;
				if(alertBox.getSelectedItem().equals("5 minutes before")) { back = 5; }
				else if(alertBox.getSelectedItem().equals("10 minutes before")) { back = 10; }
				else if(alertBox.getSelectedItem().equals("15 minutes before")) { back = 15; }
				else if(alertBox.getSelectedItem().equals("30 minutes before")) { back = 30; }
				else if(alertBox.getSelectedItem().equals("1 hour before")) { back = 60; }
				else if(alertBox.getSelectedItem().equals("2 hours before")) { back = 60 * 2; }
				else if(alertBox.getSelectedItem().equals("1 day before")) { back = 60 * 24; }
				else if(alertBox.getSelectedItem().equals("2 days before")) { back = 60 * 24 * 2; }
				
				String[] parts = timeBefore(appointment.getDate(), appointment.getStartTime(), back).split(",");
				
				DBHandler.createAlarm(parts[0], parts[1], user.getUsername(), appointment.getAppointmentNr(), alertBox.getSelectedIndex());
			}
			
			parent.addNewPanel("avtale", new DefaultRightPanel(parent, user));
			parent.addNewPanel("kalender", new CalendarPanel(parent, user, parent.getShowUsers(), parent.getWeek(), parent.getYear()));
			
			// Oppdaterer kalenderen til aa vise ingen valgt avtale
			//((CalendarPanel) parent.kalender).unSelectAllAppointments();
			
		}else if(s == deleteBtn){
			//appointment.getParticipants().put(user, 0);------------ Endrer lokalt
			//appointment.changeStatus(user, false, appointment);---- Endrer database
			
			//Hvis user er admin for avtalen, slett avtalen for alle
			
			if (isOwner){
				DBHandler.deleteAppointment(appointment.getAppointmentNr());
				
				
			}else{
				DBHandler.deleteAttendance(user.getUsername(), appointment.getAppointmentNr());
				Set<User> tmp = appointment.getParticipants().keySet();
				tmp.remove(user);
				DBHandler.createNotification(user.getFirstname() + " " + user.getLastname() + " will not be attending " + appointment.getName() + ".", tmp, appointment.getAppointmentNr());
			}
			
			
			parent.addNewPanel("avtale", new DefaultRightPanel(parent, user));
			parent.addNewPanel("kalender", new CalendarPanel(parent, user, parent.getShowUsers(), parent.getWeek(), parent.getYear()));
			// Oppdaterer kalenderen til aa vise ingen valgt avtale
			((CalendarPanel) parent.kalender).unSelectAllAppointments();
		}
		
	}
	
	private String timeBefore(String date, String time, int minutesBack){
		SimpleDateFormat format = new SimpleDateFormat("d.MM.y HH:mm");
		Date oldDate;
		String value = "";
		try {
			oldDate = format.parse(date + " " + time);
			Date newDate = new Date(oldDate.getTime() - (minutesBack * 1000 * 60));
			Calendar c = Calendar.getInstance();
			c.setTime(newDate);
			return String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", c.get(Calendar.MINUTE))  + "," + c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH)+1) + "." + c.get(Calendar.YEAR);
			
		} catch (ParseException e) { e.printStackTrace(); }
		return value;
	}
	
	private int getStatus(){
		if(yes.isSelected()) { return 1; }
		else if(no.isSelected()) { return 0; }
		else { return 2; }
	}
	
	private void setStatus(int status){
		if(status == 1) { yes.setSelected(true); }
		else if(status == 0) { no.setSelected(true); }
	}
	
}
