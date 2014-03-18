package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import database.DBHandler;
import fp2014.Appointment;
import fp2014.Notification;
import fp2014.User;

@SuppressWarnings("serial")
public class NotificationPanel extends JPanel implements ActionListener, FocusListener {

	private JButton exitBtn;
	private MainFrame parent;
	private JPanel notificationPanel;
	private JScrollPane scrollPanel;
	private User user;
	private ArrayList<Notification> unseenNotifications;
	private ArrayList<Notification> seenNotifications;

	public NotificationPanel(MainFrame parent, User user) {
		this.parent = parent; 
		this.user = user;
		
		// Create a panel to store notifications in
		notificationPanel = new JPanel();
		
		// Create GridBag for notification panel. Style it.
		notificationPanel.setLayout(new GridBagLayout());	
		notificationPanel.setBackground(Color.WHITE);
		GridBagConstraints gb = new GridBagConstraints();
		gb.anchor = GridBagConstraints.NORTHWEST;
		gb.weightx = 1;
		gb.gridy = 0;
		gb.gridx = 0;
		
		// Loop through notifications and add them to panel
		unseenNotifications = getUnseenNotifications();

		for(int i = 0; i < unseenNotifications.size(); i++){
			JTextArea notification = new JTextArea();
			notification.setText(unseenNotifications.get(i).getText());
			notification.setFont(new Font("Lucida Grande", Font.BOLD, 12));
			notification.addFocusListener(this);
			notification.setName("u" + i);
			notification.setEditable(false);
			notification.setWrapStyleWord(true);
			notification.setLineWrap(true);
			notification.setLayout(null);
			notification.setSize(200, 200);
			notification.setMargin(new Insets(5, 5, 15, 0));
			notificationPanel.add(notification, gb);
			gb.gridy++;
		}
		
		seenNotifications = getSeenNotifications();

		for(int i = 0; i < seenNotifications.size(); i++){
			JTextArea notification = new JTextArea();
			notification.setText(seenNotifications.get(i).getText());
			notification.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
			notification.addFocusListener(this);
			notification.setName("s" + i);
			notification.setEditable(false);
			notification.setWrapStyleWord(true);
			notification.setLineWrap(true);
			notification.setLayout(null);
			notification.setSize(200, 200);
			notification.setMargin(new Insets(5, 5, 15, 0));
			notificationPanel.add(notification, gb);
			gb.gridy++;
		}
		
		parent.updateAnnounchementCounter();

		// Dirty solution for filling up the remaining space of the frame:
		JTextPane filler = new JTextPane();
		gb.weighty = 1;
		notificationPanel.add(filler, gb);
		
		// Wrap notification panel in a scrollable panel
		scrollPanel = new JScrollPane(notificationPanel, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.setPreferredSize(new Dimension(220, 470));
		
		// Style exit button
		exitBtn = new JButton("Exit");
		exitBtn.addActionListener(this);
		exitBtn.setPreferredSize(new Dimension(220, 30));
		exitBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 12));

		// GridBag
		setLayout(new GridBagLayout());
		gb.gridx = 0;
		gb.gridy = 0;
		add(scrollPanel, gb);
		gb.gridy = 1;
		add(exitBtn, gb);
		
		// Scroll back to the top of the pane
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			   public void run() { scrollPanel.getVerticalScrollBar().setValue(0); }
		});
		
		exitBtn.setName("NPexitButton");
		notificationPanel.setName("NPnotificationPanel");
	}
	
	private ArrayList<Notification> getUnseenNotifications(){
		return DBHandler.getUnseenNotifications(user.getUsername());
	}
	
	private ArrayList<Notification> getSeenNotifications(){
		return DBHandler.getSeenNotifications(user.getUsername());
	}
	
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == exitBtn){
			parent.addNewPanel("avtale", new DefaultRightPanel(parent, user));
			
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		JTextArea area = ((JTextArea) e.getSource());
		String name = area.getName();
		Appointment active;
		if(name.charAt(0) == 'u') { active = (unseenNotifications.get(Integer.parseInt(String.valueOf(name.charAt(1))))).getAppointment(); }
		else { active = (seenNotifications.get(Integer.parseInt(String.valueOf(name.charAt(1))))).getAppointment(); }
		
		parent.addNewPanel("avtale", new ShowAppointmentPanel(parent, user, active));
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {}
}