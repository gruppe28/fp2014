package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import database.DBHandler;
import fp2014.Ansatt;
import fp2014.Appointment;
import fp2014.Group;

@SuppressWarnings({"serial", "unchecked"})
public class ManageParticipants extends JPanel implements ActionListener, ListSelectionListener, ItemListener{
	
	JButton save;
	JButton add;
	JButton remove;
	JRadioButton attend;
	JRadioButton notattend;
	
	JRadioButton groups;
	JRadioButton people;
	
	DefaultListModel<Ansatt> employeeListModel;
	DefaultListModel<Ansatt> participantsListModel;
	DefaultListModel<Group> groupListModel;
	
	JList<Ansatt> employeeList;
	JList<Ansatt> participantsList;
	JScrollPane employeeListBox;
	JScrollPane participantsListBox;
	
	JList<Group> groupList;
	JScrollPane groupListBox;
	
	newEventGUI parent;
	JDialog romFrame;
	HashMap<Ansatt, Integer> participants;
	private boolean changeBlock;
	private Appointment appointment;
	private ArrayList<Group> groupsArray;
	
	public ManageParticipants(newEventGUI parent, HashMap<Ansatt, Integer> participants) {
		
		this.parent = parent;
		this.participants = participants;
		
		changeBlock = false;
		
		appointment = new Appointment(); // Creates a new appointment object to work on. If the user saves, the content of this appointment will be written to the actual appointment.
		
		// Create dialog window
		romFrame = new JDialog();
		JPanel romPanel = new JPanel();
		romFrame.setTitle("Manage participants");
		
		// Create radio buttons and listeners
		attend = new JRadioButton("Attending");
		notattend = new JRadioButton("Not attending");
		groups = new JRadioButton("Add groups");
		people = new JRadioButton("Add people");
		attend.addActionListener(this);
		attend.addItemListener(this);
		attend.setEnabled(false);
		notattend.addActionListener(this);
		notattend.addItemListener(this);
		notattend.setEnabled(false);
		groups.addActionListener(this);
		people.addActionListener(this);
		people.setSelected(true);
		
		ButtonGroup group = new ButtonGroup();
		group.add(groups);
		group.add(people);
		
		// Create lists
		employeeListModel = new DefaultListModel<Ansatt>();
		employeeList = new JList<Ansatt>(employeeListModel);
		employeeList.setCellRenderer(new UserListCellRenderer());
		employeeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		groupListModel = new DefaultListModel<Group>();
		groupList = new JList<Group>(groupListModel);
		//groupList.setCellRenderer(new GroupListCellRenderer());
		groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		participantsListModel = new DefaultListModel<Ansatt>();
		participantsList = new JList<Ansatt>(participantsListModel);
		participantsList.setCellRenderer(new UserListCellRenderer());
		participantsListBox = new JScrollPane(participantsList);
		participantsListBox.setPreferredSize(new Dimension(150, 130));
		participantsList.addListSelectionListener(this);
		getUsers(participants);		
		
		employeeListBox = new JScrollPane(employeeList);
		employeeListBox.setPreferredSize(new Dimension(150, 130));
		
		groupListBox = new JScrollPane(groupList);
		groupListBox.setPreferredSize(new Dimension(150, 130));

		
		// Fill group list
		
		groupsArray = DBHandler.getGroups();
		
		for(Group g : groupsArray){
			groupListModel.addElement(g);
		}
		
		// Create buttons
		add = new JButton("→");
		remove = new JButton("←");
		save = new JButton("Save");
		
		add.addActionListener(this);
		remove.addActionListener(this);
		save.addActionListener(this);

		// Add elements and manage layout
		romPanel.setLayout(new GridBagLayout());
		GridBagConstraints gb = new GridBagConstraints();

		gb.fill = GridBagConstraints.HORIZONTAL;
		gb.anchor = GridBagConstraints.NORTH;
		gb.weightx = 1;
		gb.weighty = 0;

		gb.gridx = 0;
		gb.gridy = 0;
		gb.gridheight = 3;
		gb.gridwidth = 2;
		romPanel.add(employeeListBox, gb);
		romPanel.add(groupListBox, gb);
		gb.gridheight = 1;
		gb.gridwidth = 1;
		
		gb.gridx = 2;
		gb.anchor = GridBagConstraints.SOUTH;
		romPanel.add(add, gb);
		gb.gridy = 1;
		gb.anchor = GridBagConstraints.NORTH;
		romPanel.add(remove, gb);
		
		gb.gridx = 3;
		gb.gridy = 0;
		gb.gridwidth = 2;
		gb.gridheight = 2;
		gb.anchor = GridBagConstraints.NORTH;
		romPanel.add(participantsListBox, gb);
		gb.gridy = 2;
		gb.gridwidth = 1;
		gb.gridheight = 1;
		
		gb.gridx = 0;
		romPanel.add(people, gb);
		
		gb.gridx = 1;
		romPanel.add(groups, gb);
		
		gb.gridx = 3;
		romPanel.add(attend, gb);
		
		gb.gridx = 4;
		romPanel.add(notattend, gb);
		
		
		gb.gridy = 4;
		romPanel.add(save, gb);
		
		attend.addActionListener(this);
		notattend.addActionListener(this);
			
		romFrame.setModal(true);
		romFrame.setAlwaysOnTop(true);
		romFrame.setMinimumSize(new Dimension(450, 230));
		romFrame.setContentPane(romPanel);
		romFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		romFrame.pack();
		romFrame.setVisible(true);
	}
	
	public void getUsers(HashMap<Ansatt, Integer> participants){
		
		for (Ansatt i : participants.keySet()) {
			if (!i.getBrukernavn().equals(parent.user.getBrukernavn())) {
				participantsListModel.addElement(i);
				appointment.editParticipant(i, participants.get(i));				
			}
		}
		
		ArrayList<Ansatt> allUsers = DBHandler.getAllUsers();
		
		for (Ansatt i : allUsers) {
			if (!i.getBrukernavn().equals(parent.user.getBrukernavn())) {
				employeeListModel.addElement(i);
			}
		}
		
		for (Ansatt alle : allUsers) {
			for (Ansatt participant : participants.keySet()) {
				if (alle.getBrukernavn().equals(participant.getBrukernavn())) {
					employeeListModel.removeElement(alle);
			}
		}
	}
}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		changeBlock = true; // When you change selected participant, the radio box listeners should not trigger.
		
		if (participantsList.isSelectionEmpty()){
			attend.setEnabled(false);
			notattend.setEnabled(false);
		}else{
			attend.setEnabled(true);
			notattend.setEnabled(true);
		}
		Object s = e.getSource();
		
		if (s == attend){
			if (notattend.isSelected()){
				notattend.setSelected(false);
			}
			
		}else if(s == notattend){
			if (attend.isSelected()){
				attend.setSelected(false);
			}
		}else if(s == people){
			groupListBox.setVisible(false);
			employeeListBox.setVisible(true);
		}else if(s == groups){
			groupListBox.setVisible(true);
			employeeListBox.setVisible(false);
		}
		else if (s == add) {
			
			
			if(groups.isSelected() && groupList.getSelectedValue() != null){
				Group selectedGroup = groupList.getSelectedValue();
				
				for(String username : selectedGroup.getMembers()){
					for(int i = 0; i < employeeListModel.size(); i++){
						Ansatt a = employeeListModel.getElementAt(i);
						if(a.getBrukernavn().equals(username)){
							participantsListModel.addElement(a);
							appointment.editParticipant(a, 2);
							employeeListModel.removeElement(a);
						}
						
					}
				}
				
			}
			else if (employeeList.getSelectedValue() == null) {
				
			} else {
				participantsListModel.addElement(employeeList.getSelectedValue());
				appointment.editParticipant(employeeList.getSelectedValue(), 2);
				employeeListModel.removeElement(employeeList.getSelectedValue());
			}
		} else if (s == remove) {
				if (participantsList.getSelectedValue() == null) {
				
			} else {
				employeeListModel.addElement(participantsList.getSelectedValue());
				appointment.removeParticipant(participantsList.getSelectedValue());
				participantsListModel.removeElement(participantsList.getSelectedValue());
			}
		} else if (s == save) {
			parent.appointment.setParticipants(appointment.getParticipants());
			romFrame.dispose();
		} 
		
		changeBlock = false;
		
	}
	

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		
		changeBlock = true; // When you change selected participant, the radio box listeners should not trigger.
		
		int status;
		if (!participantsList.isSelectionEmpty()){
			status = appointment.getParticipantStatus(participantsList.getSelectedValue());
			attend.setEnabled(true);
			notattend.setEnabled(true);
		}else{
			status = 2;
			attend.setEnabled(false);
			notattend.setEnabled(false);
		}
		
		
		if (arg0.getSource() == participantsList) {
			if (status == 1) {
				attend.setSelected(true);
				notattend.setSelected(false);
			} else if (status == 0) {
				attend.setSelected(false);
				notattend.setSelected(true);				
			} else {
				attend.setSelected(false);
				notattend.setSelected(false);
			}
		}
		
		changeBlock = false;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		
		if(changeBlock) { return; } // Omits status changes if true
		
		if (e.getSource().equals(attend)) {
			if (attend.isSelected()) {
				appointment.editParticipant(participantsList.getSelectedValue(), 1);				
			} else {
				appointment.editParticipant(participantsList.getSelectedValue(), 2);	
			}
		} else if (e.getSource().equals(notattend)) {
			if (notattend.isSelected()) {
				appointment.editParticipant(participantsList.getSelectedValue(), 0);
			} else {
				appointment.editParticipant(participantsList.getSelectedValue(), 2);
			}
		}
	}
}
	
