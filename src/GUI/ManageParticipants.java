package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import fp2014.Ansatt;

@SuppressWarnings({"serial", "unchecked"})
public class ManageParticipants extends JPanel implements ActionListener{
	
	JButton save;
	JButton add;
	JButton remove;
	JRadioButton attend;
	JRadioButton notattend;
	DefaultListModel<Ansatt> employeeListModel;
	DefaultListModel<Ansatt> participantsListModel;
	JList<Ansatt> employeeList;
	JList<Ansatt> participantsList;
	JScrollPane employeeListBox;
	JScrollPane participantsListBox;
	
	public ManageParticipants() {
		
		// Create dialog window
		JDialog romFrame = new JDialog();
		JPanel romPanel = new JPanel();
		romFrame.setTitle("Manage participants");
		
		// Create radio buttons and listeners
		attend = new JRadioButton("Attends");
		notattend = new JRadioButton("Does not attend");
		attend.addActionListener(this);
		notattend.addActionListener(this);
		
		// Create lists
		employeeListModel = new DefaultListModel<Ansatt>();
		participantsListModel = new DefaultListModel<Ansatt>();
		employeeList = new JList<Ansatt>(employeeListModel);
		participantsList = new JList<Ansatt>(participantsListModel);
		employeeListBox = new JScrollPane(employeeList);
		employeeListBox.setPreferredSize(new Dimension(150, 150));
		participantsListBox = new JScrollPane(participantsList);
		participantsListBox.setPreferredSize(new Dimension(150, 130));
		employeeList.setCellRenderer(new RomListCellRenderer());
		employeeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Create buttons
		add = new JButton("→");
		remove = new JButton("←");
		save = new JButton("Save");

		// Add elements and manage layout
		romPanel.setLayout(new GridBagLayout());
		GridBagConstraints gb = new GridBagConstraints();

		gb.fill = GridBagConstraints.HORIZONTAL;
		gb.weightx = 1;
		gb.weighty = 0;

		gb.gridx = 0;
		gb.gridy = 0;
		gb.gridheight = 3;
		romPanel.add(employeeListBox, gb);
		gb.gridheight = 1;
		
		gb.gridx = 1;
		gb.anchor = GridBagConstraints.SOUTH;
		romPanel.add(add, gb);
		gb.gridy = 1;
		gb.anchor = GridBagConstraints.NORTH;
		romPanel.add(remove, gb);
		
		gb.gridx = 2;
		gb.gridy = 0;
		gb.gridwidth = 2;
		gb.gridheight = 2;
		gb.anchor = GridBagConstraints.NORTH;
		romPanel.add(participantsListBox, gb);
		gb.gridy = 2;
		gb.gridwidth = 1;
		gb.gridheight = 1;
		romPanel.add(attend, gb);
		gb.gridx = 3;
		romPanel.add(notattend, gb);
		
		gb.gridy = 3;
		romPanel.add(save, gb);
		
		ButtonGroup group = new ButtonGroup();
		group.add(attend);
		group.add(notattend);
		
		romFrame.setModal(true);
		romFrame.setMinimumSize(new Dimension(450, 300));
		romFrame.setContentPane(romPanel);
		romFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		romFrame.setVisible(true);
		romFrame.pack();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		
	}
	
}
	
