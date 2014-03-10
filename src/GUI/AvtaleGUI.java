package GUI;

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
public class AvtaleGUI extends JPanel {
	
	JButton nyAvtale;
	JTextField avtaleNavn;
	JTextField avtaleBeskrivelse;
	JLabel startTid;
	JLabel sluttTid;
	JTextField startTidspunkt;
	JTextField sluttTidspunkt;
	JDateChooser datoVelgerFra;
	JDateChooser datoVelgerTil;
	JButton moterom;
	JTextField visRom;
	JButton deltakere;
	JButton alarm;
	JButton slettAvtale;
	JButton lagre;
	
	public AvtaleGUI() {
		
		nyAvtale = new JButton("Ny avtale");
		nyAvtale.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				nyAvtale.setVisible(false);
				avtaleNavn.setVisible(true);
				avtaleBeskrivelse.setVisible(true);
				startTid.setVisible(true);
				sluttTid.setVisible(true);
				startTidspunkt.setVisible(true);
				sluttTidspunkt.setVisible(true);
				datoVelgerFra.setVisible(true);
				datoVelgerTil.setVisible(true);
				moterom.setVisible(true);
				visRom.setVisible(true);
				deltakere.setVisible(true);
				alarm.setVisible(true);
				slettAvtale.setVisible(true);
				lagre.setVisible(true);
				
			}
		});
		
		this.add(nyAvtale);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbcA = new GridBagConstraints();
		
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
		moterom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				romValgGUI romValg = new romValgGUI();
			}
		});
		this.add(moterom, gbcA);
		
		visRom.setEditable(false);
		gbcA.gridy = 5;
		gbcA.gridwidth = 3;
		this.add(visRom, gbcA);
		
		gbcA.gridy = 6;
		gbcA.gridwidth = 3;
		this.add(deltakere, gbcA);
		
		gbcA.gridy = 7;
		gbcA.gridwidth = 3;
		this.add(alarm, gbcA);
		
		gbcA.gridy = 8;
		gbcA.gridwidth = 2;
		gbcA.insets = new Insets(200, 2, 4, 2);
		this.add(slettAvtale, gbcA);
		
		gbcA.gridy = 8;
		gbcA.gridx = 2;
		gbcA.gridwidth = 1;
		this.add(lagre, gbcA);
		
		nyAvtale.setVisible(true);
		avtaleNavn.setVisible(false);
		avtaleBeskrivelse.setVisible(false);
		startTid.setVisible(false);
		sluttTid.setVisible(false);
		startTidspunkt.setVisible(false);
		sluttTidspunkt.setVisible(false);
		datoVelgerFra.setVisible(false);
		datoVelgerTil.setVisible(false);
		moterom.setVisible(false);
		visRom.setVisible(false);
		deltakere.setVisible(false);
		alarm.setVisible(false);
		slettAvtale.setVisible(false);
		lagre.setVisible(false);
	}
}
