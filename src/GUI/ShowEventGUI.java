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
import fp2014.Ansatt;
import fp2014.Appointment;

@SuppressWarnings("serial")
public class ShowEventGUI extends JPanel implements ActionListener{

	private JLabel name, time, place, attending, alert, hide;
	private JTextArea description;
	private DefaultListModel<String> participantListModel;
	private JList<String> participants;
	private JScrollPane scrollDescription, scrollParticipants;
	private JRadioButton yes, no;
	private JComboBox<String> alertBox;
	private JButton edit, delete, save, cancel;
	private JCheckBox hideBox;
	private Appointment appointment;
	private KalenderView parent;
	private Ansatt user;
	private boolean isOwner;
	
	public ShowEventGUI(KalenderView parent, Ansatt user, Appointment appointment){
		
		this.parent = parent;
		this.appointment = appointment;
		this.user = user;
		
		isOwner = (appointment.getMadeBy().getBrukernavn().equals(user.getBrukernavn()));
		
		this.setPreferredSize(new Dimension(220, 500));
		
		if (appointment.getName().length() > 13){
			name = new JLabel(appointment.getName().substring(0, 10) + "...");
		}else{
			name = new JLabel(appointment.getName());
		}
		
		name.setFont(new Font(Font.MONOSPACED, Font.BOLD, 26));
		
		time = new JLabel(appointment.getDate() + " kl. " + appointment.getStartTime() + "-" + appointment.getEndTime());
		
		if (appointment.getPlace() != null){
			place = new JLabel("Place: " + appointment.getPlace());
		}else{
			place = new JLabel("Room: " + appointment.getRom().getSted());
		}
		attending = new JLabel("Attending:");
		alert = new JLabel("Alert:");
		hide = new JLabel("Hide:");
		
		description = new JTextArea(appointment.getDescription());
		description.setEditable(false);
		description.setLineWrap(true);
		
		participantListModel = new DefaultListModel<String>();
		// TODO Appointment trenger liste over deltagere
		
		for (Ansatt a: appointment.getParticipants().keySet()){
			String tmp = a.getBrukernavn() + " - ";
			if (appointment.getParticipantStatus(a) == 1){
				tmp += "attending";
			}else if (appointment.getParticipantStatus(a) == 1){
				tmp += "not attending";
			}
			participantListModel.addElement(tmp);
		}
		/*
		participantListModel.addElement("Participant 1 - attending");
		participantListModel.addElement("Participant 2 - not attending");
		participantListModel.addElement("Participant 3 - ");
		participantListModel.addElement("Participant 4 - attending");
		participantListModel.addElement("Participant 5 - ");
		*/
		
		participants = new JList<String>(participantListModel);
		
		scrollDescription = new JScrollPane(description);
		scrollDescription.setPreferredSize(new Dimension(200, 80));
		
		scrollParticipants = new JScrollPane(participants);
		scrollParticipants.setPreferredSize(new Dimension(200, 120));
		
		yes = new JRadioButton("yes");
		yes.addActionListener(this);
		no = new JRadioButton("no");
		no.addActionListener(this);
		hideBox = new JCheckBox("yes");
		
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
		
		alertBox.setSelectedIndex(DBHandler.getAlarmType(user.getBrukernavn(), appointment.getAppointmentNr())); // Set index of alert box to previously chosen option
		
		edit = new JButton("Edit");
		delete = new JButton("Delete");
		save = new JButton("Save");
		cancel = new JButton("Cancel");
		
		edit.addActionListener(this);
		delete.addActionListener(this);
		save.addActionListener(this);
		cancel.addActionListener(this);
		
		
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
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridwidth = 2;
		c.gridx--;
		c.gridy++;
		add(edit, c);
		
		c.gridx+=3;
		add(delete, c);
		
		if (!isOwner){
			edit.setEnabled(false);
			edit.setVisible(false);
		}
			
		c.insets = new Insets(30, 0, 0, 0);
		c.gridx-=3;
		c.gridy++;
		add(cancel, c);
		
		c.gridx+=3;
		add(save, c);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		
		if (s == yes){
			if (no.isSelected()){
				no.setSelected(false);
			}
		}else if(s == no){
			if (yes.isSelected()){
				yes.setSelected(false);
			}
		}else if(s == cancel){
			parent.addNewPanel("avtale", new AvtaleGUI(parent, user));
			
			// Oppdaterer kalenderen til aa vise ingen valgt avtale
			((CalendarPanel) parent.kalender).unSelectAllAppointments();
			
		}else if(s == edit){
			parent.addNewPanel("avtale", new newEventGUI(parent, user, appointment));
			
		}else if(s == save){
			//save attending status
			
			
			
			// Save alarm
			DBHandler.deleteAlarm(user.getBrukernavn(), appointment.getAppointmentNr()); // Deletes existing alarm before creating a new one.
			
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
				
				System.out.println(alertBox.getSelectedIndex());
				
				DBHandler.createAlarm(parts[0], parts[1], user.getBrukernavn(), appointment.getAppointmentNr(), alertBox.getSelectedIndex());
			}
			
			
			
			
			parent.addNewPanel("avtale", new AvtaleGUI(parent, user));
			
			// Oppdaterer kalenderen til aa vise ingen valgt avtale
			((CalendarPanel) parent.kalender).unSelectAllAppointments();
			
		}else if(s == delete){
			//appointment.getParticipants().put(user, 0);------------ Endrer lokalt
			//appointment.changeStatus(user, false, appointment);---- Endrer database
			
			//Hvis user er admin for avtalen, slett avtalen for alle
			
			parent.addNewPanel("avtale", new AvtaleGUI(parent, user));
			
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
	
}
