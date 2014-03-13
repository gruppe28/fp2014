package GUI;

import java.awt.Color;
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
import database.User;
import fp2014.Ansatt;

@SuppressWarnings("serial")
public class LoginGUI extends JPanel {
	
	private GridBagConstraints gbc;
	private JPasswordField passwordField;
	private JTextField usernameField;
	private JLabel feedback;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JButton loginButton;
	
	public LoginGUI(final JFrame loginFrame){

		// Create Swing elements
		usernameLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");
		usernameField = new JTextField("",15);
		passwordField = new JPasswordField("",15);
		loginButton = new JButton("Login");
		feedback = new JLabel();
		feedback.setForeground(Color.RED);
		loginFrame.getRootPane().setDefaultButton(loginButton); // Pressing Enter will now activate the login button
		
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
		
		// Listener logging the user in if correct credentials are given
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Fetch data and create user object
				String pw = new String(passwordField.getPassword());
				String un = usernameField.getText();
				User newUser = new User(); // User object to perform validating methods on
				
				if(newUser.checkLogin(un, pw)){
					loginFrame.dispose(); // Close the login form before opening the calendar
					Ansatt you = newUser.getAnsatt(un); // Fetch an Ansatt object based on username. Will be used throughout the session in order to identify logged in user.
					
					// Create new calendar window
					JFrame frame = new JFrame(you.getFornavn() + " " + you.getEtternavn() + "'s calendar");
					KalenderView mainPanel = new KalenderView(you, frame);
					frame.setContentPane(mainPanel);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.pack();
					frame.setResizable(false);
					frame.setVisible(true);
				}
				else{ feedback.setText(newUser.feedback); } // Show feedback message from the login attempt
			}
		});
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Login");
		LoginGUI mainPanel = new LoginGUI(frame);
		frame.setContentPane(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1024,600);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}