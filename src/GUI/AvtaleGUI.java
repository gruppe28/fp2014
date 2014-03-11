package GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import fp2014.Ansatt;
import fp2014.Appointment;

@SuppressWarnings("serial")
public class AvtaleGUI extends JPanel implements ActionListener {
	
	private ArrayList appointments = new ArrayList();
	
	private JButton nyAvtale;
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
	private Appointment appointment;
	
	public AvtaleGUI() {
		
		nyAvtale = new JButton("Ny avtale");
		nyAvtale.addActionListener(this);
		
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
		avbryt = new JButton("Avbryt");
		
		lagre.addActionListener(this);
		avbryt.addActionListener(this);
		
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
		deltakere.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ManageParticipants manageParticipants = new ManageParticipants();
			}
		});
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
		
		changeState(true);
	}

	public void changeState(boolean nyAvtaleKnappenSynlig){
		
		nyAvtale.setVisible(nyAvtaleKnappenSynlig);
		avtaleNavn.setVisible(!nyAvtaleKnappenSynlig);
		avtaleBeskrivelse.setVisible(!nyAvtaleKnappenSynlig);
		startTid.setVisible(!nyAvtaleKnappenSynlig);
		sluttTid.setVisible(!nyAvtaleKnappenSynlig);
		startTidspunkt.setVisible(!nyAvtaleKnappenSynlig);
		sluttTidspunkt.setVisible(!nyAvtaleKnappenSynlig);
		datoVelgerFra.setVisible(!nyAvtaleKnappenSynlig);
		datoVelgerTil.setVisible(!nyAvtaleKnappenSynlig);
		moterom.setVisible(!nyAvtaleKnappenSynlig);
		visRom.setVisible(!nyAvtaleKnappenSynlig);
		deltakere.setVisible(!nyAvtaleKnappenSynlig);
		alarm.setVisible(!nyAvtaleKnappenSynlig);
		slettAvtale.setVisible(!nyAvtaleKnappenSynlig);
		lagre.setVisible(!nyAvtaleKnappenSynlig);
		avbryt.setVisible(!nyAvtaleKnappenSynlig);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == lagre){

			System.out.println(appointments);
			changeState(true);
		}else if (s == avbryt){
			changeState(true);
		}else if(s == moterom){
			romValgGUI romValg = new romValgGUI();
		}else if (s == nyAvtale){
			changeState(false);
		}
	}
}
