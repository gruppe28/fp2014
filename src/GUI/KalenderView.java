package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import fp2014.Ansatt;
import fp2014.Appointment;

@SuppressWarnings("serial")
public class KalenderView extends JPanel  {
	
	protected ArrayList<Appointment> avtaler;

	private GridBagConstraints gbc;
	
	private JFrame activeWindow;
	
	
	public KalenderView(Ansatt user, JFrame activeWindow) {
		
		this.activeWindow = activeWindow;
		
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
		JPanel headerLeft = new JPanel();
		JPanel headerRight = new JPanel();
		JPanel avtale = new AvtaleGUI();
		
		kalender.setPreferredSize(new Dimension(804, 500));
		headerLeft.setPreferredSize(new Dimension(804, 100));
		headerRight.setPreferredSize(new Dimension(220, 100));
		avtale.setPreferredSize(new Dimension(220, 500));
		
		/*
		 * Header
		 */
		
		headerLeft.setLayout(new GridBagLayout());
		GridBagConstraints gbcH = new GridBagConstraints();
		
		JButton forrigeUke = new JButton("<--");
		JTextField ukeNr = new JTextField("Week X", 15);
		JButton nesteUke = new JButton("-->");
		JButton logUt = new JButton("Log off " + user.getBrukernavn());
		
		logUt.addActionListener(new logOffListener());
		
		JButton varsler = new JButton("Alerts: X");
		
		gbcH.anchor = GridBagConstraints.CENTER;
		gbcH.fill = GridBagConstraints.HORIZONTAL;
		
		gbcH.gridx=0;
		gbcH.gridheight = 2;
		gbcH.insets = new Insets(4, 330, 4, 40);
		headerLeft.add(forrigeUke, gbcH);

		gbcH.gridx=1;
		gbcH.insets = new Insets(4, 4, 4, 40);
		headerLeft.add(ukeNr, gbcH);
		
		gbcH.gridx=2;
		gbcH.insets = new Insets(4, 4, 4, 330);
		headerLeft.add(nesteUke, gbcH);
		
		
		// Gridbag, right header
		
		headerRight.setLayout(new GridBagLayout());
		
		gbcH.gridx=3;
		gbcH.gridheight = 1;
		gbcH.insets = new Insets(4, 4, 4, 0);
		headerRight.add(logUt, gbcH);
		
		gbcH.gridx=3;
		gbcH.gridy=1;
		headerRight.add(varsler, gbcH);
		
		
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
		headerLeft.setBackground(Color.GREEN);
		this.add(headerLeft, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		headerRight.setBackground(Color.GREEN);
		this.add(headerRight, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		avtale.setBackground(Color.WHITE);
		this.add(avtale, gbc);
		
	}
	
	private void logOff(){
		String[] args = {};
		LoginGUI.main(args);
		activeWindow.dispose();
	}

	class logOffListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			logOff();
		}
	}

}
