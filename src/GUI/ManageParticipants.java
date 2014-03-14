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

@SuppressWarnings({"serial", "unchecked"})
public class ManageParticipants extends JPanel implements ActionListener, ListSelectionListener, ItemListener{
	
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
	newEventGUI parent;
	JDialog romFrame;
	HashMap<Ansatt, Integer> participants;
	
	public ManageParticipants(newEventGUI parent, HashMap<Ansatt, Integer> participants) {
		
		this.parent = parent;
		this.participants = participants;
		
		// Create dialog window
		romFrame = new JDialog();
		JPanel romPanel = new JPanel();
		romFrame.setTitle("Manage participants");
		
		// Create radio buttons and listeners
		attend = new JRadioButton("Attends");
		notattend = new JRadioButton("Does not attend");
		attend.addActionListener(this);
		attend.addItemListener(this);
		attend.setEnabled(false);
		notattend.addActionListener(this);
		notattend.addItemListener(this);
		notattend.setEnabled(false);
		
		// Create lists
		employeeListModel = new DefaultListModel<Ansatt>();
		employeeList = new JList<Ansatt>(employeeListModel);
		employeeList.setCellRenderer(new UserListCellRenderer());
		employeeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		participantsListModel = new DefaultListModel<Ansatt>();
		participantsList = new JList<Ansatt>(participantsListModel);
		participantsList.setCellRenderer(new UserListCellRenderer());
		participantsListBox = new JScrollPane(participantsList);
		participantsListBox.setPreferredSize(new Dimension(150, 130));
		participantsList.addListSelectionListener(this);
		getUsers(participants);		
		
		employeeListBox = new JScrollPane(employeeList);
		employeeListBox.setPreferredSize(new Dimension(150, 150));
		
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
			participantsListModel.addElement(i);
		}
		
		ArrayList<Ansatt> allUsers = DBHandler.getAllUsers();
		
		for (Ansatt i : allUsers) {
			employeeListModel.addElement(i);
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
			
		}else if (s == add) {
			if (employeeList.getSelectedValue() == null) {
				
			} else {
				participantsListModel.addElement(employeeList.getSelectedValue());
				parent.appointment.editParticipant(employeeList.getSelectedValue(), 2);
				employeeListModel.removeElement(employeeList.getSelectedValue());
				System.out.println(parent.getParticipants());
			}
		} else if (s == remove) {
				if (participantsList.getSelectedValue() == null) {
				
			} else {
				employeeListModel.addElement(participantsList.getSelectedValue());
				parent.appointment.removeParticipant(participantsList.getSelectedValue());
				participantsListModel.removeElement(participantsList.getSelectedValue());
			}
		} else if (s == save) {
			System.out.println("----------------");
			//romFrame.dispose();
		} 
		
	}
	

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		
		int status;
		if (!participantsList.isSelectionEmpty()){
			status = parent.appointment.getParticipantStatus(participantsList.getSelectedValue());
			attend.setEnabled(true);
			notattend.setEnabled(true);
		}else{
			status = 2;
			attend.setEnabled(false);
			notattend.setEnabled(false);
		}
		
		System.out.println(status);
		
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
	}

//	@Override
//	public void focusGained(FocusEvent e) {
//		if (e.getSource().equals(attend)) {
//			parent.appointment.editParticipant(participantsList.getSelectedValue(), 1);
//			notattend.setSelected(false);
//		} else if (e.getSource().equals(notattend)) {
//			parent.appointment.editParticipant(participantsList.getSelectedValue(), 0);
//			attend.setSelected(false);
//		}
//		System.out.println(attend.isSelected());
//		System.out.println(notattend.isSelected());
//	}
//
//	@Override
//	public void focusLost(FocusEvent e) {
//		
//	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		
		if (e.getSource().equals(attend)) {
			if (attend.isSelected()) {
				parent.appointment.editParticipant(participantsList.getSelectedValue(), 1);				
			} else {
				parent.appointment.editParticipant(participantsList.getSelectedValue(), 2);	
				System.out.println("kall 1");
			}
		} else if (e.getSource().equals(notattend)) {
			if (notattend.isSelected()) {
				parent.appointment.editParticipant(participantsList.getSelectedValue(), 0);				
			} else {
				parent.appointment.editParticipant(participantsList.getSelectedValue(), 2);				
				System.out.println("kall 2");
			}
		}
	}
}
	
