package GUI;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import client.ClientDBCalls;
import fp2014.User;

@SuppressWarnings({"serial", "unchecked"})
public class ViewAsPanel extends JPanel implements ActionListener{
	
	private MainPanel parent;
	private JDialog viewFrame;
	private ArrayList<User> showUsers;
	private JList<User> employeeList, selectedList;
	private JScrollPane employeeListBox, selectedListBox;
	private DefaultListModel<User> employeeListModel, selectedListModel;
	private JButton save, add, remove;
	private JLabel allUsersLabel, selectedUsersLabel;
	
	public ViewAsPanel(MainPanel parent) {
		this.parent = parent;
		showUsers = new ArrayList<>();
		
		// Create dialog window
		viewFrame = new JDialog();
		JPanel viewPanel = new JPanel();
		viewFrame.setTitle("View calendar as");
		viewPanel.setLayout(null);
		
		// Create lists
		employeeListModel = new DefaultListModel<User>();
		employeeList = new JList<User>(employeeListModel);
		employeeList.setCellRenderer(new UserListCellRenderer());
		employeeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		employeeListBox = new JScrollPane(employeeList);
		employeeListBox.setPreferredSize(new Dimension(150, 150));
		
		selectedListModel = new DefaultListModel<User>();
		selectedList = new JList<User>(selectedListModel);
		selectedList.setCellRenderer(new UserListCellRenderer());
		selectedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectedListBox = new JScrollPane(selectedList);
		selectedListBox.setPreferredSize(new Dimension(150, 150));
		
		// Fill lists
		ArrayList<User> allUsers = ClientDBCalls.getAllUsers();
		boolean userShownFromBefore;
		
		for (User i : allUsers) {
			userShownFromBefore = false;
			for (User j : parent.getShowUsers()) {
				if(i.getUsername().equals(j.getUsername())) {
					selectedListModel.addElement(i);
					showUsers.add(i); 
					userShownFromBefore = true;
					break;
				}
			}
			if(userShownFromBefore) { continue; }
			employeeListModel.addElement(i);
		}
		
		// Create buttons,listeners and labels
		add = new JButton(">");
		remove = new JButton("<");
		save = new JButton("Save");
	
		add.addActionListener(this);
		remove.addActionListener(this);
		save.addActionListener(this);
		
		add.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		remove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		save.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		allUsersLabel = new JLabel("All users:");
		selectedUsersLabel = new JLabel("Selected users:");
		
		allUsersLabel = new JLabel("All users:");
		selectedUsersLabel = new JLabel("Selected users:");
		
		allUsersLabel.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		selectedUsersLabel.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		add.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		remove.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
	
		
		allUsersLabel.setBounds(20,20,200,20);
		selectedUsersLabel.setBounds(280,20,200,20);
		
		employeeListBox.setBounds(20,40,200,130);
		selectedListBox.setBounds(280,40,200,130);
		
		add.setBounds(224,40,50,25); 
		remove.setBounds(224,75,50,25);
		
		save.setBounds(400,180,80,25);

		viewPanel.add(allUsersLabel);
		viewPanel.add(selectedUsersLabel);
		viewPanel.add(employeeListBox);
		
		viewPanel.add(add);
		viewPanel.add(remove);
		viewPanel.add(selectedListBox);
		
		viewPanel.add(save);
		
		// Set frame options
		viewFrame.setModal(true);
		viewFrame.setAlwaysOnTop(true);
		viewFrame.setMinimumSize(new Dimension(500, 245));
		viewFrame.setContentPane(viewPanel);
		viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		viewFrame.pack();
		viewFrame.setLocationRelativeTo(parent);
		viewFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		
		if (s == add) {
			if (employeeList.getSelectedValue() != null) {
				User moved = employeeList.getSelectedValue();
				selectedListModel.addElement(moved);
				employeeListModel.removeElement(moved);
				showUsers.add(moved);
			}
		} else if (s == remove) {
			if (selectedList.getSelectedValue() != null) {
				User moved = selectedList.getSelectedValue();
				employeeListModel.addElement(moved);
				selectedListModel.removeElement(moved);
				showUsers.remove(moved);
			}
		} else if (s == save) {
			parent.setShowUsers(showUsers);
			viewFrame.dispose();
		} 
	}
}
	
