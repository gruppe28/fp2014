package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

@SuppressWarnings("serial")
public class newEventGUI extends JPanel implements ActionListener {

	private JTextField avtaleNavn;
	private JTextField avtaleBeskrivelse;
	private JLabel startTid;
	private JLabel sluttTid;
	private JTextField startTidspunkt;
	private JTextField sluttTidspunkt;
	private JDateChooser datoVelgerFra;
	private JDateChooser datoVelgerTil;
	private JButton moterom;
	private JTextField visRom;
	private JButton deltakere;
	private JButton alarm;
	private JButton slettAvtale;
	private JButton lagre;
	private JButton avbryt;
	private KalenderView parent;
	
	public newEventGUI(KalenderView parent){
		
		this.parent = parent;
		setPreferredSize(new Dimension(220, 500));
		
		avtaleNavn = new JTextField("Navn paa avtale");
		avtaleBeskrivelse = new JTextField("Beskrivelse av avtalen");
		
		startTid = new JLabel("Fra:");
		sluttTid = new JLabel("Til:");
		startTidspunkt = new JTextField("hh:mm");
		sluttTidspunkt = new JTextField("hh:mm");
		datoVelgerFra = new JDateChooser();
		datoVelgerTil = new JDateChooser();
		
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
		
		gbcA.gridx = 2;
		this.add(datoVelgerTil, gbcA);
		
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
		gbcA.gridwidth = 3;
		gbcA.insets = new Insets(200, 2, 4, 2);
		this.add(slettAvtale, gbcA);
		
		gbcA.gridy = 9;
		gbcA.gridwidth = 2;
		gbcA.insets = new Insets(0, 2, 4, 2);
		this.add(avbryt, gbcA);
		
		gbcA.gridy = 9;
		gbcA.gridx = 2;
		gbcA.gridwidth = 1;
		this.add(lagre, gbcA);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == lagre){
			parent.addNewPanel("avtale", new AvtaleGUI(parent));
			System.out.println("saved");
		}else if (s == avbryt){
			parent.addNewPanel("avtale", new AvtaleGUI(parent));
		}else if(s == moterom){
			romValgGUI romValg = new romValgGUI("19.04.2014", "10:00", "11:00");
		}else if(s == deltakere){
			ManageParticipants manageParticipants = new ManageParticipants();
		}else if(s == slettAvtale){
			parent.addNewPanel("avtale", new AvtaleGUI(parent));
			System.out.println("slettet");
		}
	}
	
}
