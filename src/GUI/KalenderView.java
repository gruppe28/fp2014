package GUI;

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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fp2014.Appointment;




/*
 * Tenker dette som GUI for programmet.
 */


@SuppressWarnings("serial")
public class KalenderView extends JPanel {
	
	protected ArrayList<Appointment> avtaler;

	private GridBagConstraints gbc;
	
	
	public KalenderView() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		
		JPanel kalender = new JPanel();
		JPanel header = new JPanel();
		JPanel avtale = new AvtaleGUI();
		
		kalender.setPreferredSize(new Dimension(804, 500));
		header.setPreferredSize(new Dimension(1024, 100));
		avtale.setPreferredSize(new Dimension(220, 500));
		
		/*
		 * Header
		 */
		
		header.setLayout(new GridBagLayout());
		GridBagConstraints gbcH = new GridBagConstraints();
		
		JButton forrigeUke = new JButton("<--");
		JTextField ukeNr = new JTextField("Uke X", 15);
		JButton nesteUke = new JButton("-->");
		JButton logUt = new JButton("Logg ut 'brukernavn'");
		JButton varsler = new JButton("Varsler: X");
		
		gbcH.anchor = GridBagConstraints.CENTER;
		gbcH.fill = GridBagConstraints.HORIZONTAL;
		
		gbcH.gridx=0;
		gbcH.gridheight = 2;
		gbcH.insets = new Insets(4, 330, 4, 40);
		header.add(forrigeUke, gbcH);

		gbcH.gridx=1;
		gbcH.insets = new Insets(4, 4, 4, 40);
		header.add(ukeNr, gbcH);
		
		gbcH.gridx=2;
		gbcH.insets = new Insets(4, 4, 4, 330);
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
		
//		kalender.add(new JButton("visAvtale"));
		
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