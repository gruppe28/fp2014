package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import fp2014.Alarm;
import fp2014.User;

@SuppressWarnings("serial")
public class AlarmPanel extends JPanel implements ActionListener{

	GridBagConstraints gb;
	JTextArea text;
	JButton ok;
	JButton goTo;
	JDialog dialog;
	JPanel panel;
	MainFrame parent;
	Alarm alarm;
	User user;
	
	
	public AlarmPanel(MainFrame parent, User user, Alarm alarm){
		
		this.alarm = alarm;
		this.user = user;
		this.parent = parent;
		
		// Create dialog window
		dialog = new JDialog();
		dialog.setTitle("Alarm");
		dialog.setName("alarmDialog");
		panel = new JPanel();
		panel.setName("alarmPanel");
		
		// Create Swing elements
		text = new JTextArea("Don't forget your appointment " + alarm.getAppointment().getName() + " on " + alarm.getAppointment().getDate() + " from " + alarm.getAppointment().getStartTime() + " to " + alarm.getAppointment().getEndTime() + ".");
		text.setEditable(false);
		text.setBackground( new Color(0, 0, 0, 0) ); // Transparent background
		text.setMargin(new Insets(0, 0, 10, 0));
		text.setName("alarmTextArea");
		ok = new JButton("OK");
		ok.setName("alarmOk");
		goTo = new JButton("Open appointment");
		goTo.setName("alarmGoTo");
		
		goTo.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		ok.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		text.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		// Add listeners
		ok.addActionListener(this);
		goTo.addActionListener(this);
		
		panel.setLayout(null);
		
		text.setBounds(20,20,1000,25);
		ok.setBounds(20,55,160,25);
		goTo.setBounds(200,55,160,25);
		
		panel.add(text);
		panel.add(ok);
		panel.add(goTo);
		
		// Generate window
		dialog.setModal(true);
		dialog.setAlwaysOnTop(true);
		dialog.setPreferredSize(new Dimension(650, 115));
		dialog.setContentPane(panel);
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		Object s = e.getSource();
		
		if (s == ok){ dialog.dispose();}
		else if (s == goTo) {
			parent.addNewPanel("avtale", new ShowAppointmentPanel(parent, user, alarm.getAppointment()));
			dialog.dispose();
		}
			
	}
	
}
