package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fp2014.Appointment;
import fp2014.User;
import fp2014.Watcher;

@SuppressWarnings("serial")
public class MainFrame extends JPanel implements ActionListener {
	
	protected ArrayList<Appointment> appointments;
	private GridBagConstraints gbc;
	protected JFrame activeWindow;
	private int week;
	private int year;
	private JLabel weekNumberLabel;
	private JButton previousWeek;
	private JButton nextWeek;
	private JButton alerts;
	private JButton logOut;
	private JButton viewAs;
	protected JPanel kalender, headerLeft, headerRight, appointment;
	private User user;
	private ArrayList<User> showUsers;
	
	
	public MainFrame(User user, JFrame activeWindow) {
		this.user = user;
		
		showUsers = new ArrayList<>();
		showUsers.add(user);
		
		this.activeWindow = activeWindow; // Binds argument JFrame to the JFrame field. Makes it possible for the window to close itself on logout.
		
		// Assures uniform LookAndFeel across systems
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) { e.printStackTrace(); }
		
		// Find current week and year
		Calendar calendar = Calendar.getInstance();
		week = calendar.get(Calendar.WEEK_OF_YEAR);
		year = calendar.get(Calendar.YEAR);
		
		// Create and set size of various panels
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.VERTICAL;
		
		headerLeft = new JPanel();
		headerRight = new JPanel();
		appointment = new DefaultRightPanel(this, user);
		
		headerLeft.setPreferredSize(new Dimension(804, 100));
		headerRight.setPreferredSize(new Dimension(220, 100));
		appointment.setPreferredSize(new Dimension(220, 500));
	
		// Initiate CalendarPanel
		kalender = new JPanel();
		CalendarPanel newCalendar = new CalendarPanel(this, user, showUsers, week, year);
		addNewPanel("kalender", newCalendar);
		
		// Create Swing elements
		
		previousWeek = new JButton("");
		nextWeek = new JButton("");
		  try {
		    Image leftArrowIcon = ImageIO.read(getClass().getResource("/fp2014/images/left_arrow.png"));
		    previousWeek.setIcon(new ImageIcon(leftArrowIcon));
		    
		    Image rigthArrowIcon = ImageIO.read(getClass().getResource("/fp2014/images/right_arrow.png"));
		    nextWeek.setIcon(new ImageIcon(rigthArrowIcon));
		  } catch (IOException ex) {
		  }

		  previousWeek.setMargin(new Insets(0, 0, 0, 0));
		  previousWeek.setBorder(BorderFactory.createEmptyBorder());
		  previousWeek.setContentAreaFilled(false);
		  nextWeek.setMargin(new Insets(0, 0, 0, 0));
		  nextWeek.setBorder(BorderFactory.createEmptyBorder());
		  nextWeek.setContentAreaFilled(false);

		 
		weekNumberLabel = new JLabel("Week " + week + " - " + year);
		weekNumberLabel.setFont(new Font("Arial", Font.PLAIN, 26)); // Larger font for the week header
		alerts = new JButton();
		logOut = new JButton("Log out " + user.getUsername());
		viewAs = new JButton("View calendar as");
		
		previousWeek.setName("MFpreviousWeek");
		nextWeek.setName("MFnextWeek");
		alerts.setName("MFalerts");
		logOut.setName("MFlogOut");
		viewAs.setName("MFviewAs");
		
		// Create watcher. This will update the announcement counter and trigger alarms regularly
		new Watcher(this, user);
		
		// Create listeners
		logOut.addActionListener(new logOffListener());
		previousWeek.addActionListener(new changeWeekListener());
		nextWeek.addActionListener(new changeWeekListener());
		alerts.addActionListener(this);
		viewAs.addActionListener(this);
		
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
		gbcH.gridy=2;
		headerRight.add(viewAs, gbcH);
		
		// GridBag, whole calendar window
			gbc.gridx = 0;
			gbc.gridy = 1;
			this.add(kalender, gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			this.add(headerLeft, gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 0;
			this.add(headerRight, gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 1;
			this.add(appointment, gbc);
			
	}
	
	// Changes the selected week, and if necessary the year too
	private void changeWeek(int value){
		int newWeek = week;
		int newYear = year;
		int weeksInYear = weeksInYear(newYear);
		
		// Check if the change has taken us into a new year. Apply changes.
		if(week + value > 0 && week + value <= weeksInYear) { newWeek = week + value; }
		else if(week + value <= 0) { newYear--; newWeek = weeksInYear(newYear);  }
		else if(week + value > weeksInYear) { newYear++; newWeek = 1;  }
		
		// Update fields and labels accordingly
		week = newWeek;
		year = newYear;
		weekNumberLabel.setText("Week " + newWeek + " - " + newYear);
		
		// Update the calendar panel
		CalendarPanel newCalendar = new CalendarPanel(this, user, showUsers, week, year);
		addNewPanel("kalender", newCalendar);
	}
	
	
	// Calculates the number of weeks in a year. Used to detect years with 53 weeks.
	private int weeksInYear(int year){
	    Calendar c = Calendar.getInstance();
	    c.set(Calendar.YEAR, year);
	    c.set(Calendar.MONTH, Calendar.DECEMBER);
	    c.set(Calendar.DAY_OF_MONTH, 31);
	    if(c.get(Calendar.WEEK_OF_YEAR) == 53) { return 53; }
	    else { return 52; }
	}
	
	private void logOff(){
		String[] args = {};
		LoginFrame.main(args);
		activeWindow.dispose(); // Close the calendar window
	}

	public void addNewPanel(String panel, JPanel newPanel){
	
		if (panel.equals("kalender")){
			remove(kalender);
			kalender = newPanel;
			kalender.setPreferredSize(new Dimension(804, 500));
			gbc.gridx = 0;
			gbc.gridy = 1;
		}else if(panel.equals("headerLeft")){
			remove(headerLeft);
			headerLeft = newPanel;
			headerLeft.setPreferredSize(new Dimension(804, 100));
			gbc.gridx = 0;
			gbc.gridy = 0;
		}else if(panel.equals("headerRight")){
			remove(headerRight);
			headerRight = newPanel;
			headerRight.setPreferredSize(new Dimension(220, 100));
			gbc.gridx = 1;
			gbc.gridy = 0;
		}else if(panel.equals("avtale")){
			remove(appointment);
			appointment = newPanel;
			appointment.setPreferredSize(new Dimension(220, 500));
			gbc.gridx = 1;
			gbc.gridy = 1;
		}
		this.add(newPanel, gbc);
		
		revalidate();
		repaint();
	}
	
	public JPanel getAvtale() {
		return appointment;
	}

	public void setAvtale(JPanel avtale) {
		this.appointment = avtale;
	}
	
	public int getWeek() {
		return week;
	}

	public int getYear() {
		return year;
	}

	public ArrayList<User> getShowUsers(){
		return showUsers;
	}
	
	public void setShowUsers(ArrayList<User> newList){
		showUsers = newList;
		CalendarPanel newCalendar = new CalendarPanel(this, user, showUsers, week, year);
		addNewPanel("kalender", newCalendar);
	}
	
	public void updateAnnounchementCounter(){
		int unseenNotifications = user.getNumberOfUnseenNotifications();
		alerts.setText("Notifications: " + unseenNotifications);
		if(unseenNotifications > 0) { alerts.setForeground(Color.RED); }
		else { alerts.setForeground(Color.BLACK); }
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
	
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == alerts){
			addNewPanel("avtale", new NotificationPanel(this, user));
		}
		else if(s == viewAs){
			new ViewAsPanel(this);
		}
	}

}