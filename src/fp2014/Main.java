package fp2014;

import javax.swing.JFrame;

import GUI.LoginPanel;

public class Main {
	
	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame("Login");
		frame.setContentPane(new LoginPanel(frame));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1024,600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
