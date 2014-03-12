package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fp2014.Ansatt;
import fp2014.Appointment;

@SuppressWarnings("serial")
public class KalenderView extends JPanel {
	
	protected ArrayList<Appointment> avtaler;
	private GridBagConstraints gbc;
	protected JFrame activeWindow;
	private int week;
	private int year;
	private JLabel weekNumberLabel;
	private JButton previousWeek;
	private JButton nextWeek;
	private JButton alerts;
	private JButton logOut;
	private JPanel kalender, headerLeft, headerRight, avtale;
	
	
	public KalenderView(Ansatt user, JFrame activeWindow) {
		
		this.activeWindow = activeWindow; // Binds argument JFrame to the JFrame field. Makes it possible for the window to close itself on logout.
		
		// Assures uniform LookAndFeel across systems
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) { e.printStackTrace(); }
		
		// Create and set size of various panels
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.VERTICAL;
		
		kalender = new JPanel();
		headerLeft = new JPanel();
		headerRight = new JPanel();
		avtale = new AvtaleGUI(this);
		
		kalender.setPreferredSize(new Dimension(804, 500));
		headerLeft.setPreferredSize(new Dimension(804, 100));
		headerRight.setPreferredSize(new Dimension(220, 100));
		avtale.setPreferredSize(new Dimension(220, 500));
		
		// Find current week and year
		Calendar calendar = Calendar.getInstance();
		week = calendar.get(Calendar.WEEK_OF_YEAR);
		year = calendar.get(Calendar.YEAR);
		
		// Create Swing elements
		previousWeek = new JButton("<--");
		weekNumberLabel = new JLabel("WEEK " + week + " - " + year);
		weekNumberLabel.setFont(new Font("Arial", Font.PLAIN, 26)); // Larger font for the week header
		alerts = new JButton("Alerts: X");
		nextWeek = new JButton("-->");
		logOut = new JButton("Log off " + user.getBrukernavn());
		
		// Create listeners
		logOut.addActionListener(new logOffListener());
		previousWeek.addActionListener(new changeWeekListener());
		nextWeek.addActionListener(new changeWeekListener());
		
		// GridBag, left header
		headerLeft.setLayout(new GridBagLayout());
		GridBagConstraints gbcH = new GridBagConstraints();
		
		gbcH.anchor = GridBagConstraints.CENTER;
		gbcH.fill = GridBagConstraints.HORIZONTAL;
		
		gbcH.gridx = 0;
		gbcH.gridheight = 2;
		gbcH.insets = new Insets(4, 330, 4, 40);
		headerLeft.add(previousWeek, gbcH);

		gbcH.gridx=1;
		gbcH.insets = new Insets(4, 4, 4, 40);
		headerLeft.add(weekNumberLabel, gbcH);
		
		gbcH.gridx=2;
		gbcH.insets = new Insets(4, 4, 4, 330);
		headerLeft.add(nextWeek, gbcH);
		
		// GridBag, right header
		headerRight.setLayout(new GridBagLayout());
		
		gbcH.gridx=3;
		gbcH.gridheight = 1;
		gbcH.insets = new Insets(4, 4, 4, 0);
		headerRight.add(logOut, gbcH);
		
		gbcH.gridx=3;
		gbcH.gridy=1;
		headerRight.add(alerts, gbcH);
		
		// GridBag, whole calendar window
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
	
	// Changes the selected week, and if necessary the year too
	private void changeWeek(int value){
		int newWeek = week;
		int newYear = year;
		
		// Check if the change has taken us into a new year. Apply changes.
		if(week + value > 0 && week + value <= 52) { newWeek = week + value; }
		else if(week + value <= 0) { newWeek = 52; newYear--; }
		else if(week + value > 52) { newWeek = 1; newYear++; }
		
		// Update fields and labels accordingly
		week = newWeek;
		year = newYear;
		weekNumberLabel.setText("WEEK " + newWeek + " - " + newYear);
	}
	
	
	// Supposed to calculate number of weeks in year. Does not work :(
	private int weeksInYear(int year){
	    Calendar c = Calendar.getInstance();
	    c.set(Calendar.YEAR, year);
	    c.set(Calendar.MONTH, Calendar.DECEMBER);
	    c.set(Calendar.DAY_OF_MONTH, 31);
	    int ordinalDay = c.get(Calendar.DAY_OF_YEAR);
	    int weekDay = c.get(Calendar.DAY_OF_WEEK) - 1;
	    int numberOfWeeks = (ordinalDay - weekDay + 10) / 7;
	    return numberOfWeeks; //return 52; There are always 52 weeks in a year.
	}
	
	private void logOff(){
		String[] args = {};
		LoginGUI.main(args);
		activeWindow.dispose(); // Close the calendar window
	}

	public void addNewPanel(String panel, JPanel newPanel){
	
		if (panel.equals("kalender")){
			remove(kalender);
			kalender = newPanel;
			kalender.setPreferredSize(new Dimension(804, 500));
			gbc.gridx = 0;
			gbc.gridy = 1;
			newPanel.setBackground(Color.BLUE);
		}else if(panel.equals("headerLeft")){
			remove(headerLeft);
			headerLeft = newPanel;
			headerLeft.setPreferredSize(new Dimension(804, 100));
			gbc.gridx = 0;
			gbc.gridy = 0;
			newPanel.setBackground(Color.GREEN);
		}else if(panel.equals("headerRight")){
			remove(headerRight);
			headerRight = newPanel;
			headerRight.setPreferredSize(new Dimension(220, 100));
			gbc.gridx = 1;
			gbc.gridy = 0;
			newPanel.setBackground(Color.GREEN);
		}else if(panel.equals("avtale")){
			remove(avtale);
			avtale = newPanel;
			avtale.setPreferredSize(new Dimension(220, 500));
			gbc.gridx = 1;
			gbc.gridy = 1;
			newPanel.setBackground(Color.WHITE);
		}
		this.add(newPanel, gbc);
		
		revalidate();
		repaint();
	}
	
	public JPanel getAvtale() {
		return avtale;
	}

	public void setAvtale(JPanel avtale) {
		this.avtale = avtale;
	}
	
	// Listener classes below

	class logOffListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			logOff();
		}
	}
	
	class changeWeekListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == previousWeek) { changeWeek(-1); }
			else if (e.getSource() == nextWeek) { changeWeek(1); }
		}
	}

}