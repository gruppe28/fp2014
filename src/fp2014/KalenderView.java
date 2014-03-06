package fp2014;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

/*
 * Tenker dette som GUI for programmet.
 */


public class KalenderView extends JPanel {
	
	private ArrayList<Avtale> avtaler;
	private GridBagConstraints gbc;
	
	
	public KalenderView() {
		
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		
		JPanel kalender = new JPanel();
		JPanel header = new JPanel();
		JPanel avtale = new JPanel();
		
		kalender.setPreferredSize(new Dimension(1000, 500));
		header.setPreferredSize(new Dimension(1400, 200));
		avtale.setPreferredSize(new Dimension(400, 500));
		
		/*
		 * Avtale
		 */
		
		avtale.setLayout(new GridBagLayout());
		
		JTextField avtaleNavn = new JTextField("Navn på avtale");
		JTextField avtaleBeskrivelse = new JTextField("Beskrivelse av avtalen");
		JTextField startTidspunkt = new JTextField("Starttidspunkt");
		JTextField sluttTidspunkt = new JTextField("Slutttidspunkt");
		JDateChooser datoVelger = new JDateChooser();
		
		JButton moterom = new JButton("Velg Møterom");
		JButton deltakere = new JButton("Administrer deltakere");
		JButton alarm = new JButton("Opprett alarm");
		JButton slettAvtale = new JButton("Slett avtale");
		JButton lagre = new JButton("Lagre");
		
		gbc.insets = new Insets(4, 2, 4, 2);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		avtale.add(avtaleNavn, gbc);
		
		gbc.gridy = 1;
		avtale.add(avtaleBeskrivelse, gbc);
		
		gbc.gridy = 2;
		avtale.add(startTidspunkt, gbc);
		
		gbc.gridy = 3;
		avtale.add(sluttTidspunkt, gbc);
		
		gbc.gridy = 4;
		avtale.add(datoVelger, gbc);
		
		gbc.gridy = 5;
		avtale.add(moterom, gbc);
		
		gbc.gridy = 6;
		avtale.add(deltakere, gbc);
		
		gbc.gridy = 7;
		avtale.add(alarm, gbc);
		
		gbc.gridy = 8;
		avtale.add(slettAvtale, gbc);
		
		gbc.gridy = 9;
		avtale.add(lagre, gbc);
		
		/*
		 * Header
		 */
		
		header.setLayout(new GridBagLayout());
		
//		JButton forrigeUke
		
		
		
		/*
		 * Kalender
		 */
		JButton but1 = new JButton();
		but1.setText("kalender");
		kalender.add(but1);
		
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.VERTICAL;
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		kalender.setBackground(Color.BLUE);
		this.add(kalender, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		header.setBackground(Color.GREEN);
		this.add(header, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		avtale.setBackground(Color.BLACK);
		this.add(avtale, gbc);
		
	}
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("TestGUI");
		KalenderView mainPanel = new KalenderView();
		
		frame.setContentPane(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();frame.setVisible(true);
		
	}
}
