package GUI;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class CalendarPanel extends JPanel {

	public CalendarPanel(JFrame frame){
		
		JSeparator hSep, vSep;
		JLabel label;
		int hours = 24;
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(804, 1000));
		panel.setLayout(null);
		
		for (int i = 0; i < hours; i++) {
			label = new JLabel( i+":00");
			label.setBounds(5, 30+(i*40), 50, 40);
			panel.add(label);
		}
		
		String[] days = {"Monday", "Tuesday", "Wedensday", "Thursday", "Friday", "Saturday", "Sunday"};
		
		for (int i = 0; i < days.length; i++) {
			this.addDay(days[i], 50+(i*105), 0, 100, 40, panel);
		}
		
		for (int i = 0; i <= hours; i++) {
			hSep = new JSeparator(JSeparator.HORIZONTAL);
			hSep.setBounds(40, 30+(i*40), 735, 5);			
			panel.add(hSep);
		}
		
		for (int i = 0; i <= days.length; i++) {
			vSep = new JSeparator(JSeparator.VERTICAL);
			vSep.setBounds(40+(i*105), 30, 5, 960);
			panel.add(vSep);			
		}
		
		
		JScrollPane s = new JScrollPane(panel, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		s.setPreferredSize(new Dimension(804, 500));
		add(s);
		
		
	}
	
	public void addDay(String day, int x, int y, int width, int height, JPanel panel){
		JLabel label = new JLabel(day);
		label.setBounds(x, y, width, height);
		panel.add(label);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//frame.setLayout(new GridBagLayout());
		frame.setContentPane(new CalendarPanel(frame));
		
		frame.setSize(804, 500);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
