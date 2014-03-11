package GUI;

import java.awt.Dimension;


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
	
	public LoginGUI(final JFrame loginFrame){

		
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		
		JPanel login = new AvtaleGUI();
		login.setPreferredSize(new Dimension(1024, 600));
		
		JLabel usernameLabel = new JLabel("Username: ");
		JLabel passwordLabel = new JLabel("Password: ");
		usernameField = new JTextField("",15);
		passwordField = new JPasswordField("",15);
		JButton loginButton = new JButton("Login");
		
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
		
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String pw = new String(passwordField.getPassword());
				String un = usernameField.getText();
				
				User newUser = new User();
				
				if(newUser.checkLogin(un, pw)){
					
					loginFrame.dispose();
					
					Ansatt you = newUser.getAnsatt(un);
					
					JFrame frame = new JFrame(you.getFornavn() + " " + you.getEtternavn() + "'s calendar");
					KalenderView mainPanel = new KalenderView(you);
					
					frame.setContentPane(mainPanel);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.pack();
					frame.setResizable(false);
					frame.setVisible(true);



				}
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
