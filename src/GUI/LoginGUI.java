package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginGUI extends JPanel {
	
	private GridBagConstraints gbc;
	
	public LoginGUI(){
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		
		JPanel login = new AvtaleGUI();
		login.setPreferredSize(new Dimension(1024, 600));
		
		JLabel usernameLabel = new JLabel("Username: ");
		JLabel passwordLabel = new JLabel("Password: ");
		JTextField username = new JTextField("",15);
		JTextField password = new JTextField("",15);
		JButton loginButton = new JButton("Login");
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(usernameLabel,gbc);
		gbc.gridx=1;
		add(username,gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(passwordLabel,gbc);
		gbc.gridx = 1;
		add(password,gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		add(loginButton,gbc);
	}
	
public static void main(String[] args) {
		
		JFrame frame = new JFrame("Login");
		LoginGUI mainPanel = new LoginGUI();
		
		frame.setContentPane(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1024,600);
		frame.setResizable(false);
		frame.setVisible(true);
		
	}
}
