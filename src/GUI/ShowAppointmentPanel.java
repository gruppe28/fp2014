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
import fp2014.User;
import fp2014.Appointment;

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
		
		name.setFont(new Font(Font.MONOSPACED, Font.BOLD, 26));
		
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
		
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 5;
		
		add(name, c);
		
		c.gridy++;
		add(time, c);
		
		c.insets = new Insets(0, 0, 10, 0);
		c.gridy++;
		add(place, c);
		
		c.gridy++;
		add(scrollDescription, c);
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy++;
		add(scrollParticipants, c);
		
		c.gridwidth = 1;
		c.gridy++;
		add(attending, c);
		
		c.gridwidth = 2;
		c.gridx++;
		add(yes, c);
		
		c.gridx+=2;
		add(no, c);
		
		c.insets = new Insets(0, 0, 80, 0);
		c.gridy++;
		c.gridx=1;
		c.gridwidth = 1;
		add(alert, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx++;
		c.gridwidth = 4;
		add(alertBox, c);
		
		c.insets = new Insets(-80, 0, 60, 0);
		c.gridy++;
		c.gridx=1;
		c.gridwidth = 1;
		add(hide, c);
		
		c.gridx++;
		c.gridwidth = 4;
		add(hideBox, c);
		hideBox.setEnabled(false);
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridwidth = 2;
		c.gridx--;
		c.gridy++;
		add(editBtn, c);
		
		c.gridx+=3;
		add(deleteBtn, c);
		
		if (!isOwner){
			editBtn.setEnabled(false);
			editBtn.setVisible(false);
		}
		
		c.insets = new Insets(30, 0, 0, 0);
		c.gridx-=3;
		c.gridy++;
		if(!isParticipant){
			c.insets = new Insets(220, 0, 0, 0);
			c.gridwidth = 5;
		}
		add(cancelBtn, c);
		
		c.gridwidth = 2;
		c.gridx+=3;
		add(saveBtn, c);
		
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
