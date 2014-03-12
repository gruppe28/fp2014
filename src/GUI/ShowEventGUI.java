package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class ShowEventGUI extends JPanel implements ChangeListener{

	private JLabel name, time, place, attending, alert;
	private JTextArea description;
	private DefaultListModel<String> listModel;
	private JList<String> participants;
	private JScrollPane scrollDescription, scrollParticipants;
	private JRadioButton yes, no;
	private JComboBox<String> alertBox;
	private JButton edit, delete, save;
	
	public ShowEventGUI(int AvtaleID, boolean isOwner){
		
		this.setPreferredSize(new Dimension(220, 500));
		
		name = new JLabel("My Event");
		name.setFont(new Font(name.getFont().getFontName(), Font.BOLD, 26));
		time = new JLabel("dd.mm.yy" + "kl. " + "hh:mm" + "-" + "hh:mm");
		place = new JLabel("Place: " + "Room 42");
		attending = new JLabel("Attending:");
		alert = new JLabel("Alert:");
		
		description = new JTextArea("This is not a text area. This is not a text area. This is not a text area.his is not a text area. This is not a text area. This is not a text area.his is not a text area. This is not a text area. This is not a text area.");
		description.setEditable(false);
		description.setLineWrap(true);
		
		listModel = new DefaultListModel<String>();
		listModel.addElement("Participant 1 - attending");
		listModel.addElement("Participant 2 - not attending");
		listModel.addElement("Participant 3 - ");
		listModel.addElement("Participant 4 - attending");
		listModel.addElement("Participant 5 - ");
		
		participants = new JList<String>(listModel);
		
		scrollDescription = new JScrollPane(description);
		scrollDescription.setPreferredSize(new Dimension(200, 80));
		
		scrollParticipants = new JScrollPane(participants);
		scrollParticipants.setPreferredSize(new Dimension(200, 120));
		
		yes = new JRadioButton("yes");
		yes.addChangeListener(this);
		no = new JRadioButton("no");
		no.addChangeListener(this);
		
		alertBox = new JComboBox<String>();
		alertBox.setPrototypeDisplayValue("xx minutes ");
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
		
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 5;
		
		add(name, c);
		
		c.gridy++;
		add(time, c);
		
		c.insets = new Insets(0, 0, 50, 0);
		c.gridy++;
		add(place, c);
		
		c.insets = new Insets(-40, 0, 10, 0);
		c.gridy++;
		add(scrollDescription, c);
		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy++;
		add(scrollParticipants, c);
		
		c.insets = new Insets(0, 0, 0, 0);
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
	
	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new ShowEventGUI(1337, false));
		frame.setPreferredSize(new Dimension(220, 500));
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Object s = e.getSource();
		if (s == yes){
			if (!yes.isSelected()){
				no.setSelected(false);
			}
		}else if(s == no){
			if (!no.isSelected()){
				yes.setSelected(false);
			}
		}
	}
	
}
