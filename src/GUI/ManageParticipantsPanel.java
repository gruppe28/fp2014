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
import fp2014.Appointment;
import fp2014.Group;
import fp2014.User;

@SuppressWarnings({"serial", "unchecked"})
public class ManageParticipantsPanel extends JPanel implements ActionListener, ListSelectionListener, ItemListener{
	
	JButton saveBtn;
	JButton addBtn;
	JButton removeBtn;
	JRadioButton attendBtn;
	JRadioButton notattendBtn;
	
	JRadioButton groupsBtn;
	JRadioButton people;
	
	DefaultListModel<User> employeeListModel;
	DefaultListModel<User> participantsListModel;
	DefaultListModel<Group> groupListModel;
	
	JList<User> employeeList;
	JList<User> participantsList;
	JScrollPane employeeListBox;
	JScrollPane participantsListBox;
	
	JList<Group> groupList;
	JScrollPane groupListBox;
	
	EditAppointmentPanel parent;
	JDialog romFrame;
	HashMap<User, Integer> participants;
	private boolean changeBlock;
	private Appointment appointment;
	private ArrayList<Group> groupsArray;
	
	public ManageParticipantsPanel(EditAppointmentPanel parent, HashMap<User, Integer> participants) {
		
		this.parent = parent;
		this.participants = participants;
		
		changeBlock = false;
		
		appointment = new Appointment(); // Creates a new appointment object to work on. If the user saves, the content of this appointment will be written to the actual appointment.
		
		// Create dialog window
		romFrame = new JDialog();
		JPanel romPanel = new JPanel();
		romFrame.setTitle("Manage participants");
		
		// Create radio buttons and listeners
		attendBtn = new JRadioButton("Attending");
		notattendBtn = new JRadioButton("Not attending");
		groupsBtn = new JRadioButton("Add groups");
		people = new JRadioButton("Add people");
		attendBtn.addActionListener(this);
		attendBtn.addItemListener(this);
		attendBtn.setEnabled(false);
		notattendBtn.addActionListener(this);
		notattendBtn.addItemListener(this);
		notattendBtn.setEnabled(false);
		groupsBtn.addActionListener(this);
		people.addActionListener(this);
		people.setSelected(true);
		
		ButtonGroup group = new ButtonGroup();
		group.add(groupsBtn);
		group.add(people);
		
		// Create lists
		employeeListModel = new DefaultListModel<User>();
		employeeList = new JList<User>(employeeListModel);
		employeeList.setCellRenderer(new UserListCellRenderer());
		employeeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		groupListModel = new DefaultListModel<Group>();
		groupList = new JList<Group>(groupListModel);
		//groupList.setCellRenderer(new GroupListCellRenderer());
		groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		participantsListModel = new DefaultListModel<User>();
		participantsList = new JList<User>(participantsListModel);
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
		addBtn = new JButton("→");
		removeBtn = new JButton("←");
		saveBtn = new JButton("Save");
		
		addBtn.addActionListener(this);
		removeBtn.addActionListener(this);
		saveBtn.addActionListener(this);

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
		romPanel.add(addBtn, gb);
		gb.gridy = 1;
		gb.anchor = GridBagConstraints.NORTH;
		romPanel.add(removeBtn, gb);
		
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
		romPanel.add(groupsBtn, gb);
		
		gb.gridx = 3;
		romPanel.add(attendBtn, gb);
		
		gb.gridx = 4;
		romPanel.add(notattendBtn, gb);
		
		gb.gridy = 4;
		romPanel.add(saveBtn, gb);
		
		attendBtn.addActionListener(this);
		notattendBtn.addActionListener(this);
			
		romFrame.setModal(true);
		romFrame.setAlwaysOnTop(true);
		romFrame.setMinimumSize(new Dimension(450, 230));
		romFrame.setContentPane(romPanel);
		romFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		romFrame.pack();
		romFrame.setLocationRelativeTo(parent.parent);
		romFrame.setVisible(true);
	}
	
	public void getUsers(HashMap<User, Integer> participants){
		
		for (User i : participants.keySet()) {
			if (!i.getUsername().equals(parent.user.getUsername())) {
				participantsListModel.addElement(i);
				appointment.editParticipant(i, participants.get(i));				
			}
		}
		
		ArrayList<User> allUsers = DBHandler.getAllUsers();
		
		for (User i : allUsers) {
			if (!i.getUsername().equals(parent.user.getUsername())) {
				employeeListModel.addElement(i);
			}
		}
		
		for (User alle : allUsers) {
			for (User participant : participants.keySet()) {
				if (alle.getUsername().equals(participant.getUsername())) {
					employeeListModel.removeElement(alle);
			}
		}
	}
}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		changeBlock = true; // When you change selected participant, the radio box listeners should not trigger.
		
		if (participantsList.isSelectionEmpty()){
			attendBtn.setEnabled(false);
			notattendBtn.setEnabled(false);
		}else{
			attendBtn.setEnabled(true);
			notattendBtn.setEnabled(true);
		}
		Object s = e.getSource();
		
		if (s == attendBtn){
			if (notattendBtn.isSelected()){
				notattendBtn.setSelected(false);
			}
			
		}else if(s == notattendBtn){
			if (attendBtn.isSelected()){
				attendBtn.setSelected(false);
			}
		}else if(s == people){
			groupListBox.setVisible(false);
			employeeListBox.setVisible(true);
		}else if(s == groupsBtn){
			groupListBox.setVisible(true);
			employeeListBox.setVisible(false);
		}
		else if (s == addBtn) {
			
			
			if(groupsBtn.isSelected() && groupList.getSelectedValue() != null){
				Group selectedGroup = groupList.getSelectedValue();
				
				for(String username : selectedGroup.getMembers()){
					for(int i = 0; i < employeeListModel.size(); i++){
						User a = employeeListModel.getElementAt(i);
						if(a.getUsername().equals(username)){
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
		} else if (s == removeBtn) {
				if (participantsList.getSelectedValue() == null) {
				
			} else {
				employeeListModel.addElement(participantsList.getSelectedValue());
				appointment.removeParticipant(participantsList.getSelectedValue());
				participantsListModel.removeElement(participantsList.getSelectedValue());
			}
		} else if (s == saveBtn) {
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
			attendBtn.setEnabled(true);
			notattendBtn.setEnabled(true);
		}else{
			status = 2;
			attendBtn.setEnabled(false);
			notattendBtn.setEnabled(false);
		}
		
		if (arg0.getSource() == participantsList) {
			if (status == 1) {
				attendBtn.setSelected(true);
				notattendBtn.setSelected(false);
			} else if (status == 0) {
				attendBtn.setSelected(false);
				notattendBtn.setSelected(true);				
			} else {
				attendBtn.setSelected(false);
				notattendBtn.setSelected(false);
			}
		}
		
		changeBlock = false;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		
		if(changeBlock) { return; } // Omits status changes if true
		
		if (e.getSource().equals(attendBtn)) {
			if (attendBtn.isSelected()) {
				appointment.editParticipant(participantsList.getSelectedValue(), 1);				
			} else {
				appointment.editParticipant(participantsList.getSelectedValue(), 2);	
			}
		} else if (e.getSource().equals(notattendBtn)) {
			if (notattendBtn.isSelected()) {
				appointment.editParticipant(participantsList.getSelectedValue(), 0);
			} else {
				appointment.editParticipant(participantsList.getSelectedValue(), 2);
			}
		}
	}
}
	
