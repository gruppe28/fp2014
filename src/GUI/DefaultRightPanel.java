package GUI;

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
	
	private JButton nyAvtale;
	private MainFrame parent;
	private User user;
	
	public DefaultRightPanel(MainFrame parent, User ansatt) {
		
		this.user = ansatt;
		this.parent = parent;
		
		nyAvtale = new JButton("New appointment");
		nyAvtale.addActionListener(this);
		nyAvtale.setName("DRPnyAvtale");
		nyAvtale.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		setLayout(new GridBagLayout());
		
		this.add(nyAvtale);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == nyAvtale){
			Appointment newAppointment = new Appointment();
			parent.addNewPanel("avtale", new EditAppointmentPanel(parent, user, newAppointment));
		}
	}
}
