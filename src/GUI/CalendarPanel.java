package GUI;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class CalendarPanel extends JPanel {

	public CalendarPanel(){
		setLayout(null);
		add(new EventGUI(50, 100, 20, 50));
		
		
	}
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//frame.setLayout(new GridBagLayout());
		frame.setContentPane(new CalendarPanel());
		
		frame.setPreferredSize(new Dimension(220, 500));
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
