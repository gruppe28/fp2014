package fp2014;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

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
		
		kalender.setPreferredSize(new Dimension(804, 500));
		header.setPreferredSize(new Dimension(1024, 100));
		avtale.setPreferredSize(new Dimension(220, 500));
		
		/*
		 * Avtale
		 */
		
		avtale.setLayout(new GridBagLayout());
		GridBagConstraints gbcA = new GridBagConstraints();
		
		JTextField avtaleNavn = new JTextField("Navn pï¿½ï¿½ï¿½ avtale");
		JTextField avtaleBeskrivelse = new JTextField("Beskrivelse av avtalen");
		
		JLabel startTid = new JLabel("Fra:");
		JLabel sluttTid = new JLabel("Til:");
		JTextField startTidspunkt = new JTextField("hh:mm");
		JTextField sluttTidspunkt = new JTextField("hh:mm");
		JDateChooser datoVelgerFra = new JDateChooser();
		JDateChooser datoVelgerTil = new JDateChooser();
		
		JButton moterom = new JButton("Velg Mï¿½ï¿½ï¿½terom");
		JTextField visRom = new JTextField("Rom ikke valgt");
		JButton deltakere = new JButton("Administrer deltakere");
		JButton alarm = new JButton("Opprett alarm");
		JButton slettAvtale = new JButton("Slett avtale");
		JButton lagre = new JButton("Lagre");
		
		gbcA.insets = new Insets(4, 2, 4, 2);
		gbcA.anchor = GridBagConstraints.NORTH;
		gbcA.fill = GridBagConstraints.HORIZONTAL;
		gbcA.weightx = 1;
		gbcA.weighty = 0;
		
		gbcA.gridx = 0;
		gbcA.gridy = 0;
		gbcA.gridwidth = 3;
		avtale.add(avtaleNavn, gbcA);
		
		gbcA.gridy = 1;
		gbcA.gridwidth = 3;
		avtale.add(avtaleBeskrivelse, gbcA);

		gbcA.gridy = 2;
		gbcA.gridwidth = 1;
		avtale.add(startTid, gbcA);
		
		gbcA.gridx = 1;
		avtale.add(startTidspunkt, gbcA);

		gbcA.gridx = 2;
		avtale.add(datoVelgerFra, gbcA);
				
		gbcA.gridy = 3;
		gbcA.gridx = 0;
		avtale.add(sluttTid, gbcA);
		
		gbcA.gridx = 1;
		avtale.add(sluttTidspunkt, gbcA);
		
		gbcA.gridx = 2;
		avtale.add(datoVelgerTil, gbcA);
		
		gbcA.gridy = 4;
		gbcA.gridx = 0;
		gbcA.gridwidth = 3;
		moterom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog romFrame = new JDialog();
				JPanel romPanel = new JPanel();
				
				JRadioButton velgMoterom = new JRadioButton("Velg møterom", true);
				JRadioButton velgMotested = new JRadioButton("Velg møtested");
				
				romPanel.add(velgMoterom);
				romPanel.add(velgMotested);
				
				/*
				 * Møtested-komponenter
				 */
				
				JLabel stedLabel = new JLabel("Skrin inn møtested:");
				JTextField motested = new JTextField(20);
				JButton lagreSted = new JButton("Lagre");
				
				
				/*
				 * Møterom-komponenter
				 */
				
				JSlider antDeltagere = new JSlider(1, 40, 10);
				
				DefaultListModel<JRadioButton> romListModel = new DefaultListModel<JRadioButton>();
				
				romListModel.addElement(new JRadioButton("Rom #1"));
				romListModel.addElement(new JRadioButton("Rom #2"));
				romListModel.addElement(new JRadioButton("Rom #3"));
				romListModel.addElement(new JRadioButton("Rom #4"));
				romListModel.addElement(new JRadioButton("Rom #5"));
				romListModel.addElement(new JRadioButton("Rom #6"));
				romListModel.addElement(new JRadioButton("Rom #7"));
				romListModel.addElement(new JRadioButton("Rom #8"));
				romListModel.addElement(new JRadioButton("Rom #9"));
				
				
				JList<JRadioButton> romList = new JList<JRadioButton>(romListModel);
				JScrollPane romListScroller = new JScrollPane(romList);
				romListScroller.setPreferredSize(new Dimension(300, 200));

				romList.setCellRenderer(new DefaultListCellRenderer());
				romList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				
				JButton lagreRom = new JButton("Lagre");
				
				
				/*
				 * 
				 */
				
				romPanel.add(stedLabel);
				romPanel.add(motested);
				romPanel.add(lagreSted);
				romPanel.add(antDeltagere);
				romPanel.add(romListScroller);
				romPanel.add(lagreRom);
				
				ButtonGroup group = new ButtonGroup();
				group.add(velgMotested);
				group.add(velgMoterom);
				
				
				velgMoterom.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				
				romFrame.setModal(true);
				romFrame.setMinimumSize(new Dimension(350, 350));
				romFrame.setContentPane(romPanel);
				romFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				romFrame.setVisible(true);
				romFrame.pack();
			}
		});
		avtale.add(moterom, gbcA);
		
		visRom.setEditable(false);
		gbcA.gridy = 5;
		gbcA.gridwidth = 3;
		avtale.add(visRom, gbcA);
		
		gbcA.gridy = 6;
		gbcA.gridwidth = 3;
		avtale.add(deltakere, gbcA);
		
		gbcA.gridy = 7;
		gbcA.gridwidth = 3;
		avtale.add(alarm, gbcA);
		
		gbcA.gridy = 8;
		gbcA.gridwidth = 2;
		gbcA.insets = new Insets(200, 2, 4, 2);
		avtale.add(slettAvtale, gbcA);
		
		gbcA.gridy = 8;
		gbcA.gridx = 2;
		gbcA.gridwidth = 1;
		avtale.add(lagre, gbcA);
		
		/*
		 * Header
		 */
		
		header.setLayout(new GridBagLayout());
		GridBagConstraints gbcH = new GridBagConstraints();
		
		JButton forrigeUke = new JButton("<--");
		JTextField ukeNr = new JTextField("Uke X");
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
		avtale.setBackground(Color.WHITE);
		this.add(avtale, gbc);
		
	}
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("TestGUI");
		KalenderView mainPanel = new KalenderView();
		
		frame.setContentPane(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		
	}
}
