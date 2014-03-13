package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

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

import fp2014.Ansatt;
import fp2014.Appointment;
import fp2014.Rom;

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
	private JButton deltakere;
	private JButton alarm;
	private JButton slettAvtale;
	private JButton lagre;
	private JButton avbryt;
	private KalenderView parent;
	private Ansatt user;
	protected Appointment appointment;
	private MaskFormatter formatter;

	public newEventGUI(KalenderView parent, Ansatt ansatt,
			Appointment appointment) {

		this.appointment = appointment;
		this.parent = parent;
		this.user = ansatt;

		setPreferredSize(new Dimension(220, 500));

		avtaleNavn = new JTextField("Navn paa avtale");
		avtaleBeskrivelse = new JTextField("Beskrivelse av avtalen");
		try {
			formatter = new MaskFormatter("##:##");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
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
		alarm = new JButton("Opprett alarm");
		slettAvtale = new JButton("Slett avtale");
		lagre = new JButton("Lagre");
		avbryt = new JButton("Avbryt");

		lagre.addActionListener(this);
		avbryt.addActionListener(this);
		slettAvtale.addActionListener(this);

		this.setLayout(new GridBagLayout());
		GridBagConstraints gbcA = new GridBagConstraints();

		feedback.setForeground(Color.RED);

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
		gbcA.gridwidth = 3;
		this.add(alarm, gbcA);

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
				this.appointment.edit(avtaleNavn.getText(),startTidspunkt.getText(), sluttTidspunkt.getText(),avtaleBeskrivelse.getText(),appointmentDate.toString(), user);
				System.out.println(this.appointment.toString());
				appointment.sendAppoinmentToDatabase();
				parent.addNewPanel("avtale", new AvtaleGUI(parent, user));
				System.out.println("Appointment saved to database.");
			}

		} else if (s == avbryt) {
			parent.addNewPanel("avtale", new AvtaleGUI(parent, user));
		} else if (s == moterom) {

			if (checkDate() == null) {
				romValgGUI romValg = new romValgGUI(this, datoVelgerFra.getDateFormatString(),startTidspunkt.getText(), sluttTidspunkt.getText());
			}

		} else if (s == deltakere) {
			ManageParticipants manageParticipants = new ManageParticipants();
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

	public String checkDate() {
		if (startTidspunkt.getText().contains("_") || sluttTidspunkt.getText().contains("_")) {
			System.out.println("Skriv inn et gyldig tidspunkt.");
			return ("Skriv inn et gyldig tidspunkt.");
		} else {
			String[] start = startTidspunkt.getText().split(":");
			int startM = Integer.parseInt(start[0]);
			int startH = Integer.parseInt(start[1]);

			String[] end = sluttTidspunkt.getText().split(":");
			int endM = Integer.parseInt(end[0]);
			int endH = Integer.parseInt(end[1]);
			
			if (new DateTime(datoVelgerFra.getDate()).toLocalDate().compareTo(
					new LocalDate()) < 0) {
				System.out.println("Velg en gyldig dato");
				return ("Choose a valid date.");
			} else if (datoVelgerFra.getDate() == null) {
				System.out.println("Velg en dato først");
				return ("Choose a date first.");
			} else if ((startM > 59) || (endM > 59) || (startH > 23)
					|| (endH > 23) || (startM < 0) || (endM < 0)
					|| (startH < 0) || (endH < 0)) {
				System.out.println("Skriv inn et gyldig tidspunkt");
				return ("Skriv inn gydlig tidspunkt.");
			} else if (startH > endH || (startH == endH && startM > endM)) {
				System.out.println("Sluttidspunkt er før starttidspunkt.");
				return ("Sluttidspunkt er før starttidspunkt.");
			}
		}
		return null;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Ansatt user = new Ansatt("brukernavn", "passord", "fornavn",
				"etternavn", "epost");
		Appointment appointment = new Appointment();
		KalenderView view = new KalenderView(user, frame);
		newEventGUI test = new newEventGUI(view, user, appointment);
	}

}
