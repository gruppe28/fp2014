package GUI;

import java.awt.Color;
import java.awt.Dimension;
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
		
		// Add listeners
		ok.addActionListener(this);
		goTo.addActionListener(this);
		
		// GridBag
		panel.setLayout(new GridBagLayout());		
		gb = new GridBagConstraints();
		gb.fill = GridBagConstraints.BOTH;

		
		gb.gridx = 0;
		gb.gridy = 0;
		gb.gridwidth = 2;
		panel.add(text, gb);
		
		gb.gridy = 1;
		gb.gridwidth = 1;
		panel.add(ok, gb);
		
		gb.gridx = 1;
		panel.add(goTo, gb);
		
		// Generate window
		dialog.setModal(true);
		dialog.setAlwaysOnTop(true);
		dialog.setMinimumSize(new Dimension(440, 100));
		dialog.setContentPane(panel);
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.pack();
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
