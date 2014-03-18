package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class SendEmailPanel extends JPanel implements ActionListener {
	
	JButton addAdressBtn;
	JButton saveBtn;
	JButton deleteAdressBtn;
	JButton cancelBtn;
	JList emailList;
	JTextField newEmail;
	EditAppointmentPanel parent;
	DefaultListModel<String> emailListModel;
	JDialog sendMailFrame;
	JPanel sendMailPanel;
	ArrayList<String> participantsList;
	
	public SendEmailPanel (EditAppointmentPanel parent, ArrayList<String> participantsList){
		this.parent = parent;
		
		sendMailFrame = new JDialog();
		sendMailPanel = new JPanel();
		sendMailFrame.setTitle("Invite participant via email");
		
		sendMailPanel.setLayout(null);
		
		this.participantsList = participantsList;
		emailListModel = new DefaultListModel<String>();
		emailList = new JList(emailListModel);
		emailList.setCellRenderer(new DefaultListCellRenderer());
		emailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		emailList.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		for (String email : participantsList){
			emailListModel.addElement(email);
		}
		addAdressBtn = new JButton("Add");
		saveBtn = new JButton("Save");
		deleteAdressBtn = new JButton("Remove");
		cancelBtn = new JButton("Cancel");
		newEmail = new JTextField("Enter email");
		
		addAdressBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		saveBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		deleteAdressBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		cancelBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		newEmail.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		addAdressBtn.addActionListener(this);
		saveBtn.addActionListener(this);
		deleteAdressBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
		
		addAdressBtn.setBounds(230,20,95,25);
		deleteAdressBtn.setBounds(230,55,95,25);
		
		saveBtn.setBounds(125,225,95,25);
		cancelBtn.setBounds(20,225,95,25);
		
		emailList.setBounds(20,55,200,160);
		newEmail.setBounds(20,20,200,25);
		
		sendMailPanel.add(addAdressBtn);
		sendMailPanel.add(saveBtn);
		sendMailPanel.add (deleteAdressBtn);
		sendMailPanel.add (cancelBtn);
		sendMailPanel.add (emailList);
		sendMailPanel.add(newEmail);
		
		addAdressBtn.setName("SEPaddAdressButton");
		saveBtn.setName("SEPsaveButton");
		deleteAdressBtn.setName("SEPdeleteAdressButton");
		cancelBtn.setName("SEPcancelButton");
		emailList.setName("SEPemailList");
		newEmail.setName("SEPnewEmail");
		sendMailFrame.setName("SEPsendMailFrame");
		
		sendMailFrame.setModal(true);
		sendMailFrame.setMinimumSize(new Dimension(450, 300));
		sendMailFrame.setContentPane(sendMailPanel);
		sendMailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		sendMailFrame.pack();
		sendMailFrame.setLocationRelativeTo(parent.parent);
		sendMailFrame.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == addAdressBtn 
				&& newEmail.getText().contains("@") 
				&& newEmail.getText().contains(".") 
				&& !newEmail.getText().contains(" ")){
			
			emailListModel.addElement(newEmail.getText());
			participantsList.add(newEmail.getText());
		} else if (s == deleteAdressBtn) {
			if (emailList.getSelectedValue() != null){
				participantsList.remove(emailList.getSelectedIndex());
				emailListModel.remove(emailList.getSelectedIndex());
			}
		} else if (s == saveBtn) {
			parent.setEmailParticipants(participantsList);
			sendMailFrame.dispose();
		} else if (s == cancelBtn){
			sendMailFrame.dispose();
		}
	}

}
