package GUI;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import client.ClientDBCalls;
import fp2014.Room;

@SuppressWarnings({"serial", "unchecked"})
public class RoomPanel extends JPanel implements ActionListener{
	
	private EditAppointmentPanel parent;
	private JDialog roomFrame;
	private DefaultListModel<Room> roomListModel;
	private ArrayList<Room> availableRooms;
	private JLabel locationLabel, numberOfParticipantsLabel;
	private JTextField placeField;
	private JButton savePlaceBtn, saveRoomBtn;
	private JSlider numberOfParticipants;
	private JList<Room> roomList;
	private JScrollPane romListScroller;
	private JRadioButton choosePlaceBtn, chooseRoomBtn;
	
	public RoomPanel(EditAppointmentPanel parent, String date, String from, String to) {
		this.parent = parent;
		
		roomFrame = new JDialog();
		JPanel romPanel = new JPanel();
		roomFrame.setTitle("Select location");
		
		chooseRoomBtn = new JRadioButton("Select a meeting room", true);
		choosePlaceBtn = new JRadioButton("Choose a location");
		
		romPanel.add(chooseRoomBtn);
		romPanel.add(choosePlaceBtn);
		
		locationLabel = new JLabel("Enter a location:");
		placeField = new JTextField(20);
		savePlaceBtn = new JButton("Save");
		savePlaceBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		/*
		 * Moterom-komponenter
		 */
		
		numberOfParticipants = new JSlider(1, 40, 10);
		numberOfParticipants.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				numberOfParticipantsLabel.setText(Integer.toString(numberOfParticipants.getValue()));
				showAvailableRooms();
				
			}
		});
		numberOfParticipantsLabel = new JLabel(Integer.toString(numberOfParticipants.getValue()));
		
		

		// List of available rooms
		availableRooms = ClientDBCalls.getAvailableRooms(date, from, to); //Fetch rooms available that date and time from database
		roomListModel = new DefaultListModel<Room>();
		showAvailableRooms(); // Fills list
		roomList = new JList<Room>(roomListModel);
		romListScroller = new JScrollPane(roomList);
		romListScroller.setPreferredSize(new Dimension(300, 150));
		roomList.setCellRenderer(new RomListCellRenderer());
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		saveRoomBtn = new JButton("Save");
		saveRoomBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		chooseRoomBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		choosePlaceBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		locationLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		placeField.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		savePlaceBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		saveRoomBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		numberOfParticipants.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		numberOfParticipantsLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		romPanel.setLayout(null);
		
		chooseRoomBtn.setBounds(20,20,160,25);
		choosePlaceBtn.setBounds(180,20,150,25);
		
		placeField.setBounds(20,50,310,25);
		savePlaceBtn.setBounds(275,90,60,25);
		
		numberOfParticipants.setBounds(20,50,280,25);
		numberOfParticipantsLabel.setBounds(310,50,25,25);
		romListScroller.setBounds(20,80,310,100);
		saveRoomBtn.setBounds(230,200,100,25);
		
		// Add elements to window
		romPanel.add(locationLabel);
		romPanel.add(placeField);
		romPanel.add(savePlaceBtn);
		romPanel.add(numberOfParticipants);
		romPanel.add(numberOfParticipantsLabel);
		romPanel.add(romListScroller);
		romPanel.add(saveRoomBtn);
		
		placeField.setName("RPplaceField");
		savePlaceBtn.setName("RPsavePlaceButton");
		saveRoomBtn.setName("RPsaveRoomBtn");
		roomList.setName("RProomList");
		roomFrame.setName("RProomFrame");

		locationLabel.setVisible(false);
		placeField.setVisible(false);
		savePlaceBtn.setVisible(false);
		numberOfParticipants.setVisible(true);
		numberOfParticipantsLabel.setVisible(true);
		romListScroller.setVisible(true);
		roomList.setVisible(true);
		saveRoomBtn.setVisible(true);
		
		ButtonGroup group = new ButtonGroup();
		group.add(choosePlaceBtn);
		group.add(chooseRoomBtn);
		
		chooseRoomBtn.addActionListener(this);
		choosePlaceBtn.addActionListener(this);
		saveRoomBtn.addActionListener(this);
		savePlaceBtn.addActionListener(this);
		
		roomFrame.setModal(true);
		roomFrame.setMinimumSize(new Dimension(350, 265));
		roomFrame.setContentPane(romPanel);
		roomFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		roomFrame.pack();
		roomFrame.setResizable(false);
		roomFrame.setLocationRelativeTo(parent.getParent());
		roomFrame.setVisible(true);
	}
	
	
	// Call this method to update list of available rooms
	private void showAvailableRooms(){
		roomListModel.clear(); // Clears list in case room criteria has changed
		
		ArrayList<Room> capableRooms = ClientDBCalls.getRoomsWithCapacity(numberOfParticipants.getValue());
		ArrayList<Integer> roomNumbers = new ArrayList<Integer>();

		for(int i = 0; i < availableRooms.size(); i++){
			roomNumbers.add(availableRooms.get(i).getRoomNumber());
		}
		
		for(int i = 0; i < capableRooms.size(); i++){
			if(roomNumbers.contains(capableRooms.get(i).getRoomNumber())){ roomListModel.addElement(capableRooms.get(i)); }
		}
	}

	// Listeners switching between meeting room/place
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == chooseRoomBtn) {
			
			locationLabel.setVisible(false);
			placeField.setVisible(false);
			savePlaceBtn.setVisible(false);
			numberOfParticipants.setVisible(true);
			numberOfParticipantsLabel.setVisible(true);
			romListScroller.setVisible(true);
			roomList.setVisible(true);
			saveRoomBtn.setVisible(true);
			
		} else if (e.getSource() == choosePlaceBtn) {
			locationLabel.setVisible(true);
			placeField.setVisible(true);
			savePlaceBtn.setVisible(true);
			numberOfParticipants.setVisible(false);
			numberOfParticipantsLabel.setVisible(false);
			romListScroller.setVisible(false);
			roomList.setVisible(false);
			saveRoomBtn.setVisible(false);
		} 
		if (e.getSource() == savePlaceBtn){
			parent.getShowLocationField().setText(placeField.getText());
			parent.getAppointment().setPlace(placeField.getText());
			parent.getAppointment().setRom(null);
			roomFrame.dispose();
		} else if (e.getSource() == saveRoomBtn){
			parent.getShowLocationField().setText(roomList.getSelectedValue().getPlace());
			parent.getAppointment().setRom(roomList.getSelectedValue());
			parent.getAppointment().setPlace(null);
			roomFrame.dispose();
			
		}
	}
}
