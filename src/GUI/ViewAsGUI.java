package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import database.DBHandler;
import fp2014.Ansatt;

@SuppressWarnings({"serial", "unchecked"})
public class ViewAsGUI extends JPanel implements ActionListener{
	
	JButton save;
	JButton add;
	JButton remove;
	DefaultListModel<Ansatt> employeeListModel;
	DefaultListModel<Ansatt> selectedListModel;
	JList<Ansatt> employeeList;
	JList<Ansatt> selectedList;
	JScrollPane employeeListBox;
	JScrollPane selectedListBox;
	KalenderView parent;
	JDialog viewFrame;
	ArrayList<Ansatt> showUsers;
	JLabel allUsersLabel;
	JLabel selectedUsersLabel;
	
	public ViewAsGUI(KalenderView parent) {
		
		this.parent = parent;
		showUsers = new ArrayList<>();
		
		// Create dialog window
		viewFrame = new JDialog();
		JPanel viewPanel = new JPanel();
		viewFrame.setTitle("View calendar as");
		
		// Create lists
		employeeListModel = new DefaultListModel<Ansatt>();
		employeeList = new JList<Ansatt>(employeeListModel);
		employeeList.setCellRenderer(new UserListCellRenderer());
		employeeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		employeeListBox = new JScrollPane(employeeList);
		employeeListBox.setPreferredSize(new Dimension(150, 150));
		
		selectedListModel = new DefaultListModel<Ansatt>();
		selectedList = new JList<Ansatt>(selectedListModel);
		selectedList.setCellRenderer(new UserListCellRenderer());
		selectedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectedListBox = new JScrollPane(selectedList);
		selectedListBox.setPreferredSize(new Dimension(150, 150));
		
		// Fill lists
		ArrayList<Ansatt> allUsers = DBHandler.getAllUsers();
		boolean userShownFromBefore;
		
		for (Ansatt i : allUsers) {
			userShownFromBefore = false;
			for (Ansatt j : parent.getShowUsers()) {
				if(i.getBrukernavn().equals(j.getBrukernavn())) {
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
		
		allUsersLabel = new JLabel("All users:");
		selectedUsersLabel = new JLabel("Selected users:");

		// Add elements and manage layout
		viewPanel.setLayout(new GridBagLayout());
		GridBagConstraints gb = new GridBagConstraints();

		gb.fill = GridBagConstraints.HORIZONTAL;
		gb.weightx = 1;
		gb.weighty = 0;
		gb.gridx = 0;
		gb.gridy = 0;
		
		viewPanel.add(allUsersLabel, gb);
		gb.gridx = 2;
		viewPanel.add(selectedUsersLabel, gb);
		
		gb.gridx = 0;
		gb.gridy = 1;
		gb.gridheight = 2;
		viewPanel.add(employeeListBox, gb);
	
		gb.gridx = 1;
		gb.gridheight = 1;
		gb.anchor = GridBagConstraints.SOUTH;
		viewPanel.add(add, gb);
		
		gb.gridy = 2;
		gb.anchor = GridBagConstraints.NORTH;
		viewPanel.add(remove, gb);
		
		gb.gridx = 2;
		gb.gridy = 1;
		gb.gridheight = 2;
		gb.anchor = GridBagConstraints.NORTH;
		viewPanel.add(selectedListBox, gb);

		gb.gridy = 3;
		gb.gridheight = 1;
		viewPanel.add(save, gb);
		
		// Set frame options
		viewFrame.setModal(true);
		viewFrame.setAlwaysOnTop(true);
		viewFrame.setMinimumSize(new Dimension(450, 230));
		viewFrame.setContentPane(viewPanel);
		viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		viewFrame.pack();
		viewFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		
		Object s = e.getSource();
		
		if (s == add) {
			if (employeeList.getSelectedValue() != null) {
				Ansatt moved = employeeList.getSelectedValue();
				selectedListModel.addElement(moved);
				employeeListModel.removeElement(moved);
				showUsers.add(moved);
			}
		} else if (s == remove) {
			if (selectedList.getSelectedValue() != null) {
				Ansatt moved = selectedList.getSelectedValue();
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
	