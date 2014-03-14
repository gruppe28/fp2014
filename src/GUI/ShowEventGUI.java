package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import fp2014.Ansatt;
import fp2014.Appointment;

@SuppressWarnings("serial")
public class ShowEventGUI extends JPanel implements ActionListener{

	private JLabel name, time, place, attending, alert;
	private JTextArea description;
	private DefaultListModel<String> participantListModel;
	private JList<String> participants;
	private JScrollPane scrollDescription, scrollParticipants;
	private JRadioButton yes, no;
	private JComboBox<String> alertBox;
	private JButton edit, delete, save, cancel;
	private Appointment appointment;
	private KalenderView parent;
	private Ansatt user;
	private boolean isOwner;
	
	public ShowEventGUI(KalenderView parent, Appointment appointment, Ansatt user){
		
		this.parent = parent;
		this.appointment = appointment;
		this.user = user;
		
		isOwner = (appointment.getMadeBy().getBrukernavn().equals(user.getBrukernavn()));
		
		this.setPreferredSize(new Dimension(220, 500));
		
		name = new JLabel(appointment.getName());
		name.setFont(new Font(name.getFont().getFontName(), Font.BOLD, 26));
		
		time = new JLabel(appointment.getDate() + " kl. " + appointment.getStartTime() + "-" + appointment.getEndTime());
		
		if (appointment.getPlace() != null){
			place = new JLabel("Place: " + appointment.getPlace());
		}else{
			place = new JLabel("Room: " + appointment.getRom().getSted());
		}
		attending = new JLabel("Attending:");
		alert = new JLabel("Alert:");
		
		description = new JTextArea(appointment.getDescription());
		description.setEditable(false);
		description.setLineWrap(true);
		
		participantListModel = new DefaultListModel<String>();
		// TODO Appointment trenger liste over deltagere
		participantListModel.addElement("Participant 1 - attending");
		participantListModel.addElement("Participant 2 - not attending");
		participantListModel.addElement("Participant 3 - ");
		participantListModel.addElement("Participant 4 - attending");
		participantListModel.addElement("Participant 5 - ");
		
		participants = new JList<String>(participantListModel);
		
		scrollDescription = new JScrollPane(description);
		scrollDescription.setPreferredSize(new Dimension(200, 80));
		
		scrollParticipants = new JScrollPane(participants);
		scrollParticipants.setPreferredSize(new Dimension(200, 120));
		
		yes = new JRadioButton("yes");
		yes.addActionListener(this);
		no = new JRadioButton("no");
		no.addActionListener(this);
		
		alertBox = new JComboBox<String>();
		alertBox.setPrototypeDisplayValue("xx minutes "); // Set JComboBox size 
		alertBox.addItem("none");
		alertBox.addItem("5 minutes before");
		alertBox.addItem("10 minutes before");
		alertBox.addItem("15 minutes before");
		alertBox.addItem("30 minutes before");
		alertBox.addItem("1 hour before");
		alertBox.addItem("2 hours before");
		alertBox.addItem("1 day before");
		alertBox.addItem("2 days before");
		
		edit = new JButton("Edit");
		delete = new JButton("Delete");
		save = new JButton("Save");
		cancel = new JButton("Cancel");
		
		edit.addActionListener(this);
		delete.addActionListener(this);
		save.addActionListener(this);
		cancel.addActionListener(this);
		
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 5;
		
		add(name, c);
		
		c.gridy++;
		add(time, c);
		
		c.insets = new Insets(0, 0, 10, 0);
		c.gridy++;
		add(place, c);
		
		c.gridy++;
		add(scrollDescription, c);
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy++;
		add(scrollParticipants, c);
		
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		c.gridy++;
		add(attending, c);
		
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 2;
		c.gridx++;
		add(yes, c);
		
		c.gridx+=2;
		add(no, c);
		
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(0, 0, 80, 0);
		c.gridy++;
		c.gridx=1;
		c.gridwidth = 1;
		add(alert, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx++;
		c.gridwidth = 4;
		add(alertBox, c);
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridwidth = 5;
		c.gridx--;
		c.gridy++;
		add(edit, c);
		
		if (isOwner){
			c.insets = new Insets(0, 0, 0, 0);
		}else{
			edit.setVisible(false);
			edit.setEnabled(false);
			c.insets = new Insets(30, 0, 0, 0);
		}
		
		c.gridwidth = 2;
		c.gridy++;
		add(delete, c);
		
		c.gridx+=3;
		add(save, c);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		
		if (s == yes){
			if (no.isSelected()){
				no.setSelected(false);
			}
		}else if(s == no){
			if (yes.isSelected()){
				yes.setSelected(false);
			}
		}else if(s == cancel){
			parent.addNewPanel("avtale", new AvtaleGUI(parent, user));
		}
		
	}
	
}
