package fp2014;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
		
		kalender.setLayout(new GridBagLayout());
		
		kalender.setPreferredSize(new Dimension(1000, 500));
		header.setPreferredSize(new Dimension(1000, 300));
		avtale.setPreferredSize(new Dimension(400, 500));
		
		JButton but1 = new JButton();
		but1.setText("kalender");
		
		JButton but2 = new JButton();
		but2.setText("header");
		JButton but3 = new JButton();
		but3.setText("avtale");
		
		kalender.add(but1);
		header.add(but2);
		avtale.add(but3);
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.VERTICAL;
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(kalender, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		this.add(header, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
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
