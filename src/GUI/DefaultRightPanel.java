package GUI;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import fp2014.Appointment;
import fp2014.User;

@SuppressWarnings("serial")
public class DefaultRightPanel extends JPanel implements ActionListener {
	
	private JButton newAppointment;
	private MainPanel parent;
	private User user;
	
	public DefaultRightPanel(MainPanel parent, User ansatt) {
		
		this.user = ansatt;
		this.parent = parent;
		
		newAppointment = new JButton("New appointment");
		newAppointment.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		newAppointment.addActionListener(this);
		newAppointment.setName("DRPnyAvtale");
		newAppointment.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		setLayout(new GridBagLayout());
		
		this.add(newAppointment);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == newAppointment){
			Appointment newAppointment = new Appointment();
			parent.addNewPanel("avtale", new EditAppointmentPanel(parent, user, newAppointment));
		}
	}
}
