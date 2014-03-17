package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import com.toedter.calendar.JDateChooser;

import database.DBHandler;
import fp2014.Ansatt;
import fp2014.Appointment;
import fp2014.Mail;

@SuppressWarnings("serial")
public class newEventGUI extends JPanel implements ActionListener {

	private JTextField avtaleNavn;
	private JTextField avtaleBeskrivelse;
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
	private Ansatt user;
	protected Appointment appointment;
	private MaskFormatter formatter;
	public ArrayList<String> emailParticipants;
	private HashMap<Ansatt, Integer> participants;

	public newEventGUI(KalenderView parent, Ansatt ansatt, Appointment appointment) {

		this.appointment = appointment;
		this.parent = parent;
		this.user = ansatt;
		this.participants = appointment.getParticipants();

		setPreferredSize(new Dimension(220, 500));
		
		emailParticipants = new ArrayList<String>();
		
		avtaleNavn = new JTextField("Navn paa avtale");
		avtaleBeskrivelse = new JTextField("Beskrivelse av avtalen");
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

		gbcA.insets = new Insets(4, 2, 4, 2);
		gbcA.anchor = GridBagConstraints.NORTH;
		gbcA.fill = GridBagConstraints.HORIZONTAL;
		gbcA.weightx = 1;
		gbcA.weighty = 0;

		gbcA.gridx = 0;
		gbcA.gridy = 0;
		gbcA.gridwidth = 3;
		this.add(avtaleNavn, gbcA);

		gbcA.gridy = 1;
		gbcA.gridwidth = 3;
		this.add(avtaleBeskrivelse, gbcA);

		gbcA.gridy = 2;
		gbcA.gridwidth = 1;
		this.add(startTid, gbcA);

		gbcA.gridx = 1;
		this.add(startTidspunkt, gbcA);

		gbcA.gridx = 2;
		this.add(datoVelgerFra, gbcA);

		gbcA.gridy = 3;
		gbcA.gridx = 0;
		this.add(sluttTid, gbcA);

		gbcA.gridx = 1;
		this.add(sluttTidspunkt, gbcA);

		gbcA.gridy = 4;
		gbcA.gridx = 0;
		gbcA.gridwidth = 3;
		moterom.addActionListener(this);
		this.add(moterom, gbcA);

		visRom.setEditable(false);
		gbcA.gridy = 5;
		gbcA.gridwidth = 3;
		this.add(visRom, gbcA);

		gbcA.gridy = 6;
		gbcA.gridwidth = 3;
		this.add(deltakere, gbcA);
		deltakere.addActionListener(this);
		
		gbcA.gridy = 7;
		this.add(inviteViaEmailBtn, gbcA);

		gbcA.gridy = 8;
		this.add(feedback, gbcA);

		gbcA.gridy = 9;
		gbcA.gridwidth = 3;
		gbcA.insets = new Insets(180, 2, 4, 2);
		this.add(slettAvtale, gbcA);

		gbcA.gridy = 10;
		gbcA.gridwidth = 2;
		gbcA.insets = new Insets(0, 2, 4, 2);
		this.add(avbryt, gbcA);

		gbcA.gridy = 10;
		gbcA.gridx = 2;
		gbcA.gridwidth = 1;
		this.add(lagre, gbcA);
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
				LocalDate appointmentDate = new DateTime(datoVelgerFra.getDate()).toLocalDate();
				this.appointment.edit(avtaleNavn.getText(),startTidspunkt.getText(), sluttTidspunkt.getText(),avtaleBeskrivelse.getText(), toOtherDateFormat(appointmentDate), user, this.participants);
				System.out.println(this.appointment.toString());
				appointment.sendAppoinmentToDatabase();
				parent.addNewPanel("avtale", new AvtaleGUI(parent, user));
				System.out.println("Appointment saved to database.");
				
				//Legger til møteleder som deltager med status "Deltar"
				this.participants.put(user, 1);

				//Opprett/Endre AnsattAvtaler
				for (Ansatt a : this.participants.keySet()) {
					DBHandler.createAttendance(a.getBrukernavn(), DBHandler.getCountOfAppointments() + 1, participants.get(a));
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
				new romValgGUI(this, datoVelgerFra.getDateFormatString(),startTidspunkt.getText(), sluttTidspunkt.getText());
			}

		} else if (s == deltakere) {
			new ManageParticipants(this, appointment.getParticipants());
		} else if (s == slettAvtale) {
			
			// her må vi slette gjeldende avtale fra databasen
			// TODO
			// lag slett-funksjon i Appointment
			
			parent.addNewPanel("avtale", new AvtaleGUI(parent, user));
			System.out.println("slettet");
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
		avtaleBeskrivelse = new JTextField(appointment.getDescription());
		
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
		
		if (appointment.getPlace() != null){
			visRom = new JTextField(appointment.getRom().getSted());
		} else if (appointment.getRom() != null) {
			visRom = new JTextField(appointment.getPlace());
		}
	}
	
	public Date toDateFormat(String wrongFormatDate){

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
		
		System.out.println(date);
		
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

}
