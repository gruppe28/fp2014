package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import client.Client;
import client.ClientDBCalls;
import fp2014.Alarm;
import fp2014.Main;
import fp2014.User;
import fp2014.Watcher;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements ActionListener {
	
	private JFrame activeWindow;
	private User user;
	private Client client;
	private Watcher watcher;
	private int year, week;
	private JPanel kalender, headerLeft, headerRight, appointment;
	private JButton previousWeek, nextWeek, notificationBtn, logOut, viewAs;
	private JLabel weekNumberLabel;
	private ArrayList<User> showUsers;
	private GridBagConstraints gbc;
	
	public MainPanel(User user, JFrame activeWindow, Client newClient) {
		this.user = user;
		this.activeWindow = activeWindow; // Binds argument JFrame to the JFrame field. Makes it possible for the window to close itself on logout.
		
		// Initiate list of users to show in calendar
		showUsers = new ArrayList<>();
		showUsers.add(user);
		
		// Set up client
		client = newClient;
		ClientDBCalls.setClient(client);
		
		
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
		previousWeek.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		nextWeek = new JButton("");
		nextWeek.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
		notificationBtn = new JButton();
		notificationBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		logOut = new JButton("Log out " + user.getUsername());
		logOut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		viewAs = new JButton("View calendar as");
		viewAs.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		previousWeek.setName("MFpreviousWeek");
		nextWeek.setName("MFnextWeek");
		notificationBtn.setName("MFalerts");
		logOut.setName("MFlogOut");
		viewAs.setName("MFviewAs");
		
		// Create watcher. This will update the announcement counter and trigger alarms regularly
		watcher = new Watcher(this, user);
		
		// Create listeners
		logOut.addActionListener(new logOffListener());
		previousWeek.addActionListener(new changeWeekListener());
		nextWeek.addActionListener(new changeWeekListener());
		notificationBtn.addActionListener(this);
		viewAs.addActionListener(this);
		
		logOut.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		notificationBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		viewAs.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
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
		headerRight.add(notificationBtn, gbcH);
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
			
			// If window is closed abruptly, close down the server connection
		    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		        public void run() {
		            if(client.isOpen()) { client.close(); }
		        }
		    }, "Shutdown-thread"));
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
		client.close();
		watcher.stop();
		String[] args = {};
		try {
			Main.main(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		activeWindow.setContentPane(new LoginPanel(activeWindow));
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
	
	public JPanel getKalender() {
		return kalender;
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
		notificationBtn.setText("Notifications: " + unseenNotifications);
		if(unseenNotifications > 0) { notificationBtn.setForeground(Color.RED); }
		else { notificationBtn.setForeground(Color.BLACK); }
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
		if (s == notificationBtn){
			addNewPanel("avtale", new NotificationPanel(this, user));
		}
		else if(s == viewAs){
			new ViewAsPanel(this);
		}
	}
	
	public void setAlarms(ArrayList<Alarm> alarms){
		watcher.setAlarms(alarms);
	}

}