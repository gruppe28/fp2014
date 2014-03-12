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
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import database.DBHandler;
import fp2014.Ansatt;

@SuppressWarnings("serial")
public class NotificationGUI extends JPanel implements ActionListener {

	private JButton exit;
	private KalenderView parent;
	private JPanel notificationPanel;
	private JScrollPane scrollPanel;
	private Ansatt user;

	public NotificationGUI(KalenderView parent, Ansatt user) {
		this.parent = parent; 
		this.user = user;
		
		// Create a panel to store notifications in
		notificationPanel = new JPanel();
		notificationPanel.setBackground(Color.WHITE);
		
		// Create GridBag for notification panel. Style it.
		notificationPanel.setLayout(new GridBagLayout());		
		GridBagConstraints gb = new GridBagConstraints();
		gb.anchor = GridBagConstraints.NORTHWEST;
		gb.weightx = 1;
		gb.gridy = 0;
		gb.gridx = 0;
		
		// Loop through notifications and add them to panel
		ArrayList<String> unseenNotifications = getUnseenNotifications();

		for(int i = 0; i < unseenNotifications.size(); i++){
			JTextArea notification = new JTextArea();
			notification.setText(unseenNotifications.get(i));
			notification.setFont(new Font("Arial", Font.BOLD, 13));
			notification.setEditable(false);
			notification.setWrapStyleWord(true);
			notification.setLineWrap(true);
			notification.setLayout(null);
			notification.setSize(200, 200);
			notification.setMargin(new Insets(5, 5, 15, 0));
			notificationPanel.add(notification, gb);
			gb.gridy++;
		}
		
		ArrayList<String> seenNotifications = getSeenNotifications();

		for(int i = 0; i < seenNotifications.size(); i++){
			JTextArea notification = new JTextArea();
			notification.setText(seenNotifications.get(i));
			notification.setFont(new Font("Arial", Font.PLAIN, 12));
			notification.setEditable(false);
			notification.setWrapStyleWord(true);
			notification.setLineWrap(true);
			notification.setLayout(null);
			notification.setSize(200, 200);
			notification.setMargin(new Insets(5, 5, 15, 0));
			notificationPanel.add(notification, gb);
			gb.gridy++;
		}

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
		exit = new JButton("Exit");
		exit.addActionListener(this);
		exit.setPreferredSize(new Dimension(220, 30));

		// GridBag
		setLayout(new GridBagLayout());
		gb.gridx = 0;
		gb.gridy = 0;
		add(scrollPanel, gb);
		gb.gridy = 1;
		add(exit, gb);
		
		// Scroll back to the top of the pane
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			   public void run() { scrollPanel.getVerticalScrollBar().setValue(0); }
		});
	}
	
	private ArrayList<String> getUnseenNotifications(){
		return DBHandler.getUnseenNotifications(user.getBrukernavn());
	}
	
	private ArrayList<String> getSeenNotifications(){
		return DBHandler.getSeenNotifications(user.getBrukernavn());
	}
	
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == exit){
			parent.addNewPanel("avtale", new AvtaleGUI(parent));
			
		}
	}
}
