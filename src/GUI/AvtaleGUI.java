package GUI;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import fp2014.Appointment;

@SuppressWarnings("serial")
public class AvtaleGUI extends JPanel implements ActionListener {
	
	private ArrayList appointments = new ArrayList();
	
	private JButton nyAvtale;
	private Appointment appointment;
	private KalenderView parent;
	
	public AvtaleGUI(KalenderView parent) {
		
		this.parent = parent;
		
		nyAvtale = new JButton("Ny avtale");
		nyAvtale.addActionListener(this);
		
		setLayout(new GridBagLayout());
		
		this.add(nyAvtale);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == nyAvtale){
			parent.addNewPanel("avtale", new newEventGUI(parent));
			
		}
	}
}
