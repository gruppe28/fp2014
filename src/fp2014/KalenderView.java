package fp2014;

import java.awt.Color;
import java.awt.Dimension;
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


@SuppressWarnings("serial")
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
		header.setPreferredSize(new Dimension(1400, 150));
		avtale.setPreferredSize(new Dimension(400, 500));
		
		/*
		 * Avtale
		 */
		
		avtale.setLayout(new GridBagLayout());
		GridBagConstraints gbcA = new GridBagConstraints();
		
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
		
		gbcA.insets = new Insets(4, 2, 4, 2);
		gbcA.anchor = GridBagConstraints.EAST;
		gbcA.fill = GridBagConstraints.HORIZONTAL;
		
		gbcA.gridx = 0;
		gbcA.gridy = 0;
		gbcA.gridwidth = 2;
		avtale.add(avtaleNavn, gbcA);
		
		gbcA.gridy = 1;
		gbcA.gridwidth = 2;
		avtale.add(avtaleBeskrivelse, gbcA);

		gbcA.gridy = 2;
		gbcA.gridwidth = 2;
		avtale.add(startTidspunkt, gbcA);
		
		gbcA.gridy = 3;
		gbcA.gridwidth = 2;
		avtale.add(sluttTidspunkt, gbcA);
		
		gbcA.gridy = 4;
		gbcA.gridwidth = 2;
		avtale.add(datoVelger, gbcA);
		
		gbcA.gridy = 5;
		gbcA.gridwidth = 2;
		avtale.add(moterom, gbcA);
		
		gbcA.gridy = 6;
		gbcA.gridwidth = 2;
		avtale.add(deltakere, gbcA);
		
		gbcA.gridy = 7;
		gbcA.gridwidth = 2;
		avtale.add(alarm, gbcA);
		
		gbcA.gridy = 8;
		gbcA.gridwidth = 1;
		avtale.add(slettAvtale, gbcA);
		
		gbcA.gridy = 8;
		gbcA.gridx = 1;
		avtale.add(lagre, gbcA);
		
		/*
		 * Header
		 */
		
		header.setLayout(new GridBagLayout());
		GridBagConstraints gbcH = new GridBagConstraints();
		
		JButton forrigeUke = new JButton("<--");
		JTextField ukeNr = new JTextField("          Uke X          ");
		JButton nesteUke = new JButton("-->");
		JButton logUt = new JButton("Logg ut 'brukernavn'");
		JButton varsler = new JButton("Varsler: X");
		
		gbcH.anchor = GridBagConstraints.EAST;
		gbcH.fill = GridBagConstraints.HORIZONTAL;
		
		gbcH.gridx=0;
		gbcH.gridheight = 2;
		gbcH.insets = new Insets(4, 330, 4, 40);
		header.add(forrigeUke, gbcH);

		gbcH.gridx=1;
		gbcH.insets = new Insets(4, 4, 4, 40);
		header.add(ukeNr, gbcH);
		
		gbcH.gridx=2;
		gbcH.insets = new Insets(4, 4, 4, 570);
		header.add(nesteUke, gbcH);
		
		gbcH.gridx=3;
		gbcH.gridheight = 1;
		gbcH.insets = new Insets(4, 4, 4, 0);
		header.add(logUt, gbcH);
		
		gbcH.gridx=3;
		gbcH.gridy=1;
		header.add(varsler, gbcH);
		
		
		/*
		 * Kalender
		 */
		
		
		
		/*
		 * 
		 */
		
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
