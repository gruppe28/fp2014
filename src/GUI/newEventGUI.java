package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import fp2014.Ansatt;
import fp2014.Appointment;
import fp2014.Mail;

@SuppressWarnings("serial")
public class newEventGUI extends JPanel implements ActionListener, FocusListener{

	private JTextField avtaleNavn;
	private JTextArea avtaleBeskrivelse;
	private JLabel startTid;
	private JLabel sluttTid;
	private JLabel feedback;
	private JFormattedTextField startTidspunkt;
	private JFormattedTextField sluttTidspunkt;
	private JDateChooser datoVelgerFra;
	private JButton moterom;
	public JTextField visRom;
	private JButton inviteViaEmailBtn;
	private JButton deltakere;
	private JButton slettAvtale;
	private JButton lagre;
	private JButton avbryt;
	private KalenderView parent;
	protected Ansatt user;
	protected Appointment appointment;
	private MaskFormatter formatter;
	public ArrayList<String> emailParticipants;
	private HashMap<Ansatt, Integer> participants;
	private JComboBox<String> duration;

	public newEventGUI(KalenderView parent, Ansatt ansatt, Appointment appointment) {

		this.appointment = appointment;
		this.parent = parent;
		this.user = ansatt;
		this.participants = appointment.getParticipants();

		setPreferredSize(new Dimension(220, 500));
		
		emailParticipants = new ArrayList<String>();
		
		avtaleNavn = new JTextField("Navn paa avtale");
		avtaleBeskrivelse = new JTextArea("Beskrivelse av avtalen");
		try {
			formatter = new MaskFormatter("##:##");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		formatter.setPlaceholderCharacter('_');

		startTid = new JLabel("Fra:");
		sluttTid = new JLabel("Til:");
		feedback = new JLabel(" ");
		startTidspunkt = new JFormattedTextField(formatter);
		sluttTidspunkt = new JFormattedTextField(formatter);
		datoVelgerFra = new JDateChooser();
		
		duration = new JComboBox<String>();
		duration.addActionListener(this);
		duration.setPrototypeDisplayValue("xx:xx");
		addDurationsToBox();
		
		moterom = new JButton("Velg Moterom");
		visRom = new JTextField("Rom ikke valgt");
		deltakere = new JButton("Administrer deltakere");
		slettAvtale = new JButton("Slett avtale");
		lagre = new JButton("Lagre");
		avbryt = new JButton("Avbryt");
		inviteViaEmailBtn = new JButton("Invite participants via email");
		inviteViaEmailBtn.addActionListener(this);
		
		lagre.addActionListener(this);
		avbryt.addActionListener(this);
		slettAvtale.addActionListener(this);

		this.setLayout(new GridBagLayout());
		GridBagConstraints gbcA = new GridBagConstraints();

		feedback.setForeground(Color.RED);
		
		if (appointment.getName() != null){
			loadExistingAppointment();
		}

		JScrollPane scroll = new JScrollPane(avtaleBeskrivelse);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    visRom.setEditable(false);
		
		this.setLayout(null);
		
		avtaleBeskrivelse.addFocusListener(this);
		avtaleNavn.addFocusListener(this);
		startTidspunkt.addFocusListener(this);
		sluttTidspunkt.addFocusListener(this);
		
		avtaleNavn.setBounds(5,5,210,25);
		avtaleBeskrivelse.setBounds(8, 35, 204, 100);
		scroll.setBounds(8, 35, 204, 100);
		startTid.setBounds(10, 137, 50, 25);
		sluttTid.setBounds(10, 163, 50, 25);
		startTidspunkt.setBounds(40, 140, 50, 20);
		sluttTidspunkt.setBounds(40, 165, 50, 20);
		datoVelgerFra.setBounds(100,165,118,20);
		moterom.setBounds(1,190,218,25);
		visRom.setBounds(5,220,210,25);
		deltakere.setBounds(1,253,218,25);
		inviteViaEmailBtn.setBounds(1,283,218,25);
		slettAvtale.setBounds(1,420,218,25);
		lagre.setBounds(1,450,112,25);
		avbryt.setBounds(108,450,112,25);
		
		this.add(avtaleNavn);
		this.add(scroll);
		this.add(startTid);
		this.add(sluttTid);
		this.add(startTidspunkt);
		this.add(sluttTidspunkt);
		this.add(datoVelgerFra);
		this.add(moterom);
		this.add(visRom);
		this.add(deltakere);
		this.add(inviteViaEmailBtn);
		this.add(slettAvtale);
		this.add(lagre);
		this.add(avbryt);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == lagre) {
			if (checkDate() != null) {
				feedback.setText(checkDate());
			} 
			else if (visRom.getText().equals("Rom ikke valgt")){
				feedback.setText("Velg et rom først.");
			}
			else {
				boolean nullAppointment = false;
				
				if (appointment.getName() == null){
					nullAppointment = true;
				}
				LocalDate appointmentDate = new DateTime(datoVelgerFra.getDate()).toLocalDate();
				this.appointment.edit(avtaleNavn.getText(),startTidspunkt.getText(), sluttTidspunkt.getText(),avtaleBeskrivelse.getText(), toOtherDateFormat(appointmentDate), user, appointment.getParticipants());
				System.out.println(this.appointment.toString());
				parent.addNewPanel("avtale", new AvtaleGUI(parent, user));
				System.out.println("Appointment saved to database.");
				
				//Opprett/Endre AnsattAvtaler
				if (nullAppointment) {
					appointment.sendAppoinmentToDatabase();					
					int appCount = DBHandler.getCountOfAppointments();
					appointment.getParticipants().put(user, 1);
					
					for (Ansatt a : appointment.getParticipants().keySet()) {
						DBHandler.createAttendance(a.getBrukernavn(), appCount, appointment.getParticipants().get(a));
					}
				} else {
					System.out.println(appointment.getAppointmentNr());
					DBHandler.updateAppointment(appointment);
					DBHandler.deleteAttendances(appointment.getAppointmentNr());
					
					for (Ansatt a : appointment.getParticipants().keySet()) {
						DBHandler.createAttendance(a.getBrukernavn(), appointment.getAppointmentNr(), appointment.getParticipants().get(a));
					}
				}
				
				//Sender mail til evt eksterne deltagere
				sendMailInvitations();
				
				// Oppdaterer kalenderen til å vise ingen valgt avtale
				((CalendarPanel) parent.kalender).unSelectAllAppointments();
				
			}

		} else if (s == inviteViaEmailBtn){
			new SendEmailPanel(this, emailParticipants);
		}
		
		else if (s == avbryt) {
			parent.addNewPanel("avtale", new AvtaleGUI(parent, user));
			
			// Oppdaterer kalenderen til å vise ingen valgt avtale
			((CalendarPanel) parent.kalender).unSelectAllAppointments();
			
		} else if (s == moterom) {

			if (checkDate() == null) {
				new romValgGUI(this, toOtherDateFormat((LocalDate)new DateTime(datoVelgerFra.getDate()).toLocalDate()),startTidspunkt.getText(), sluttTidspunkt.getText());
			}

		} else if (s == deltakere) {
			new ManageParticipants(this, appointment.getParticipants());
		} else if (s == slettAvtale) {
			
			// her må vi slette gjeldende avtale fra databasen
			// TODO
			// lag slett-funksjon i Appointment
			
			parent.addNewPanel("avtale", new AvtaleGUI(parent, user));
			System.out.println("slettet");
		} else if (s == duration && !startTidspunkt.getText().equals("__:__") && duration.getSelectedIndex() != 0){
			int ihh = Integer.parseInt(startTidspunkt.getText().substring(0, 2));
			int imm = Integer.parseInt(startTidspunkt.getText().substring(3));
			
			int nhh = ihh + Integer.parseInt(((String) duration.getSelectedItem()).substring(0, 2));
			int nmm = imm + Integer.parseInt(((String) duration.getSelectedItem()).substring(3));
			
			if (nmm >= 60){
				nmm -= 60;
				nhh++;
			}
			
			if (nhh > 23){
				duration.setSelectedIndex(0);
				sluttTidspunkt.setText("__:__");
				return;
			}
			
			sluttTidspunkt.setText(nhh + ":" + String.format("%02d", nmm));
			sluttTidspunkt.setEditable(false);
		}else{
			sluttTidspunkt.setEditable(true);
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
	
	public HashMap<Ansatt, Integer> getParticipants(){
		return this.participants;
	}
	
	public void setParticipants(HashMap<Ansatt, Integer> participants){
		this.participants = participants;
	}

	public String checkDate() {
		if (startTidspunkt.getText().contains("_") || sluttTidspunkt.getText().contains("_")) {
			System.out.println("Skriv inn et gyldig tidspunkt.");
			return ("Skriv inn et gyldig tidspunkt.");
		} else {
			String[] start = startTidspunkt.getText().split(":");
			int startH = Integer.parseInt(start[0]);
			int startM = Integer.parseInt(start[1]);

			String[] end = sluttTidspunkt.getText().split(":");
			int endH = Integer.parseInt(end[0]);
			int endM = Integer.parseInt(end[1]);
			
			if (new DateTime(datoVelgerFra.getDate()).toLocalDate().compareTo(
					new LocalDate()) < 0) {
				System.out.println("Velg en gyldig dato");
				return ("Choose a valid date.");
			} else if (datoVelgerFra.getDate() == null) {
				System.out.println("Velg en dato først");
				return ("Choose a date first.");
			} else if ((startM > 59) || (endM > 59) || (startH > 23) || (endH > 23) || (startM < 0) || (endM < 0) || (startH < 0) || (endH < 0)) {
				System.out.println("Skriv inn et gyldig tidspunkt");
				return ("Skriv inn gydlig tidspunkt.");
			} else if (startH > endH || (startH == endH && startM > endM) ) {
				System.out.println("Sluttidspunkt er før starttidspunkt.");
				return ("Sluttidspunkt er før starttidspunkt.");
			}
		}
		return null;
	}
	
	public void sendMailInvitations(){
		String destination = "ukjent";
		
		if (appointment.getPlace() != null){
			destination = appointment.getPlace();
		} else if (appointment.getRom() != null) {
			destination = appointment.getRom().getSted();
		}
		
		String content = "You have been invited to the appointment "+ appointment.getName() 
				+ ". The meeting is about \"" + appointment.getDescription() 
				+ "\", and takes place " + appointment.getDate() + " " + appointment.getStartTime() + "-" + appointment.getEndTime()
				+ " at " + destination
				+ "\n\n Best Regards, \n\n" + user.getFornavn() + " " + user.getEtternavn() + "\n" + user.getEmail();
		
		for (String mail : emailParticipants){
			new Mail(mail,user.getEmail(),user.getFornavn()+" "+user.getEtternavn(), "Meeting invitation to "+ appointment.getName(), content);
		}
	}
	
	public void loadExistingAppointment(){
		avtaleNavn = new JTextField(appointment.getName());
		avtaleBeskrivelse = new JTextArea(appointment.getDescription());
		
		MaskFormatter formatterStart = null;
		MaskFormatter formatterEnd = null;
		
		try {
			formatterStart = new MaskFormatter(appointment.getStartTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		try {
			formatterEnd = new MaskFormatter(appointment.getEndTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		datoVelgerFra = new JDateChooser(toDateFormat(appointment.getDate()));
		
		startTidspunkt = new JFormattedTextField(formatterStart);
		sluttTidspunkt = new JFormattedTextField(formatterEnd);
		
		if (appointment.hasReservedRoom()){
			visRom = new JTextField(appointment.getRom().getSted());
		} else {
			visRom = new JTextField(appointment.getPlace());
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
		duration.addItem("--:--");
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
		if (e.getSource() == avtaleBeskrivelse){
			avtaleBeskrivelse.selectAll();
		} else if (e.getSource() == avtaleNavn) {
			avtaleNavn.selectAll();
		} else if (e.getSource() == startTidspunkt){
			startTidspunkt.selectAll();
		} else if (e.getSource() == sluttTidspunkt){
			sluttTidspunkt.selectAll();
		}
		
	}


	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == avtaleBeskrivelse){
			avtaleBeskrivelse.select(0, 0);
		} else if (e.getSource() == avtaleNavn) {
			avtaleNavn.select(0, 0);
		} else if (e.getSource() == startTidspunkt){
			startTidspunkt.select(0, 0);
		} else if (e.getSource() == sluttTidspunkt){
			sluttTidspunkt.select(0, 0);
		}
		
	}
}
