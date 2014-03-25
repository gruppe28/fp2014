package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.Client;
import client.ClientDBCalls;
import client.UserDB;
import fp2014.User;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel implements ActionListener {
	
	private GridBagConstraints gbc;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JLabel feedback, usernameLabel, passwordLabel;
	private JButton loginButton;
	private Client client;
	private JFrame frame;
	private UserDB userDB;
	
	public LoginPanel(JFrame frame){
		this.frame = frame;
		// Create Swing elements
		usernameLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");
		
		usernameField = new JTextField("",15);
		passwordField = new JPasswordField("",15);
		
		loginButton = new JButton("Login");
		loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loginButton.addActionListener(this);
		frame.getRootPane().setDefaultButton(loginButton); // Pressing Enter will now activate the login button
		
		feedback = new JLabel();
		feedback.setForeground(Color.RED);
		
		usernameField.setName("LFusernameField");
		passwordField.setName("LFpasswordField");
		loginButton.setName("LFloginButton");
		
		// Place elements through GridBag
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(usernameLabel,gbc);
		gbc.gridx=1;
		add(usernameField,gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(passwordLabel,gbc);
		gbc.gridx = 1;
		add(passwordField,gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		add(loginButton,gbc);
		gbc.gridx = 0;
		gbc.gridy = 3;					
		gbc.gridwidth = 5;
		gbc.anchor = GridBagConstraints.WEST;
		add(feedback,gbc);
	}

	private boolean userExists(){
		userDB = new UserDB(client, usernameField.getText(), new String(passwordField.getPassword())); // User object to perform validating methods on
		return userDB.checkLogin();
	}
	
	private void logIn(){
		// Create client connection
		client = new Client();
		ClientDBCalls.setClient(client);
		client.run();
		
		if(userExists()){
			User you = ClientDBCalls.getAnsatt(usernameField.getText()); // Fetch an Ansatt object based on username. Will be used throughout the session in order to identify logged in user.
			
			// Create new calendar window
			frame.setContentPane(new MainPanel(you, frame, client));
			frame.setTitle(you.getFirstname() + " " + you.getLastname() + "'s calendar");
			frame.pack();
			frame.setLocationRelativeTo(null);
		
		}else{
			// Show feedback message from the login attempt
			feedback.setText(userDB.getFeedback());
			client.close();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		
		if (s == loginButton){
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			logIn();
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}