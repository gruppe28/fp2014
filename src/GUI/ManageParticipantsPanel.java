package GUI;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

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

import client.ClientDBCalls;
import fp2014.Appointment;
import fp2014.Group;
import fp2014.User;

@SuppressWarnings({"serial", "unchecked"})
public class ManageParticipantsPanel extends JPanel implements ActionListener, ListSelectionListener, ItemListener{

	private EditAppointmentPanel parent;
	private JDialog romFrame;
	private Appointment appointment;
	
	private JButton saveBtn;
	private JButton addBtn;
	private JButton removeBtn;
	
	private JRadioButton attendBtn;
	private JRadioButton notattendBtn;
	private JRadioButton groupsBtn;
	private JRadioButton people;
	
	private DefaultListModel<User> employeeListModel;
	private DefaultListModel<User> participantsListModel;
	private DefaultListModel<Group> groupListModel;
	
	private JList<User> employeeList;
	private JList<User> participantsList;
	private JList<Group> groupList;

	private JScrollPane employeeListBox;
	private JScrollPane participantsListBox;
	private JScrollPane groupListBox;
	
	private ArrayList<Group> groupsArray;
	
	private boolean changeBlock;
	
	public ManageParticipantsPanel(EditAppointmentPanel parent, HashMap<User, Integer> participants) {
		this.parent = parent;
		
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
		groupsArray = ClientDBCalls.getGroups();
		
		for(Group g : groupsArray){
			groupListModel.addElement(g);
		}
		
		// Create buttons
		addBtn = new JButton("→");
		removeBtn = new JButton("←");
		saveBtn = new JButton("Save");
		
		addBtn.addActionListener(this);
		addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		removeBtn.addActionListener(this);
		removeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		saveBtn.addActionListener(this);
		saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		people.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		groupsBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		attendBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		notattendBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		saveBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		groupList.setFont(new Font("Lucida Grande", Font.PLAIN, 12));

		// Add elements and manage layout
		romPanel.setLayout(null);
		
		employeeListBox.setBounds(20,20,200,130);
		groupListBox.setBounds(20,20,200,130);
		participantsListBox.setBounds(280,20,200,130);
		
		addBtn.setBounds(224,20,50,25); 
		removeBtn.setBounds(224,55,50,25);
		
		people.setBounds(20,150,100,25);
		groupsBtn.setBounds(120,150,110,25);
		
		attendBtn.setBounds(280,150,90,25); 
		notattendBtn.setBounds(370,150,130,25);
		
		saveBtn.setBounds(400,180,80,25);

		romPanel.add(employeeListBox);
		romPanel.add(groupListBox);
		romPanel.add(addBtn);
		romPanel.add(removeBtn);
		romPanel.add(participantsListBox);
		romPanel.add(people);
		romPanel.add(groupsBtn);
		romPanel.add(attendBtn);
		romPanel.add(notattendBtn);
		romPanel.add(saveBtn);
		
		attendBtn.addActionListener(this);
		notattendBtn.addActionListener(this);
		
		//setName on components
		
		saveBtn.setName("MPsaveButton");
		addBtn.setName("MPaddButton");
		removeBtn.setName("MPremoveButton");
		attendBtn.setName("MPattendButton");
		notattendBtn.setName("MPnotAttendButton");
		people.setName("MPpeopleButton");
		groupsBtn.setName("MPgroupsButton");
		
		employeeList.setName("MPemployeeList");
		participantsList.setName("MPparticipantsList");
		groupList.setName("MPgroupList");
		romFrame.setName("MPromFrame");
		
		//
			
		romFrame.setModal(true);
		romFrame.setAlwaysOnTop(true);
		romFrame.setMinimumSize(new Dimension(500, 245));
		romFrame.setContentPane(romPanel);
		romFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		romFrame.pack();
		romFrame.setResizable(false);
		romFrame.setLocationRelativeTo(parent.getParent());
		romFrame.setVisible(true);
	}
	
	public void getUsers(HashMap<User, Integer> participants){
		
		for (User i : participants.keySet()) {
			if (!i.getUsername().equals(parent.getUser().getUsername())) {
				participantsListModel.addElement(i);
				appointment.editParticipant(i, participants.get(i));				
			}
		}
		
		ArrayList<User> allUsers = ClientDBCalls.getAllUsers();
		
		for (User i : allUsers) {
			if (!i.getUsername().equals(parent.getUser().getUsername())) {
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
			parent.getAppointment().setParticipants(appointment.getParticipants());
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
	
