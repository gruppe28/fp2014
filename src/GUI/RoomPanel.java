package GUI;

import java.awt.Dimension;
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

import database.DBHandler;
import fp2014.Room;

@SuppressWarnings({"serial", "unchecked"})
public class RoomPanel extends JPanel implements ActionListener{
	
	JLabel locationLabel;
	JTextField placeField;
	JButton savePlaceBtn;
	JSlider numberOfParticipants;
	JList<Room> roomList;
	JScrollPane romListScroller;
	JButton saveRoomBtn;
	JLabel numberOfParticipantsLabel;
	JRadioButton choosePlaceBtn;
	JRadioButton chooseRoomBtn;
	DefaultListModel<Room> roomListModel;
	ArrayList<Room> availableRooms;
	EditAppointmentPanel parent;
	JDialog romFrame;
	
	String date;
	String from;
	String to;
	
	public RoomPanel(EditAppointmentPanel parent, String date, String from, String to) {
		
		this.date = date;
		this.from = from;
		this.to = to;
		
		this.parent = parent;
		
		romFrame = new JDialog();
		JPanel romPanel = new JPanel();
		romFrame.setTitle("Select location");
		
		chooseRoomBtn = new JRadioButton("Select a meeting room", true);
		choosePlaceBtn = new JRadioButton("Choose a location");
		
		
		romPanel.add(chooseRoomBtn);
		romPanel.add(choosePlaceBtn);
		
		// M�tested-kompo
		
		locationLabel = new JLabel("Enter a location:");
		placeField = new JTextField(20);
		savePlaceBtn = new JButton("Save");
		
		
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
		availableRooms = DBHandler.getAvailableRooms(date, from, to); //Fetch rooms available that date and time from database
		roomListModel = new DefaultListModel<Room>();
		showAvailableRooms(); // Fills list
		roomList = new JList<Room>(roomListModel);
		romListScroller = new JScrollPane(roomList);
		romListScroller.setPreferredSize(new Dimension(300, 150));
		roomList.setCellRenderer(new RomListCellRenderer());
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		saveRoomBtn = new JButton("Save");
		
		// Add elements to window
		romPanel.add(locationLabel);
		romPanel.add(placeField);
		romPanel.add(savePlaceBtn);
		romPanel.add(numberOfParticipants);
		romPanel.add(numberOfParticipantsLabel);
		romPanel.add(romListScroller);
		romPanel.add(saveRoomBtn);

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
		
		romFrame.setModal(true);
		romFrame.setMinimumSize(new Dimension(350, 350));
		romFrame.setContentPane(romPanel);
		romFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		romFrame.setVisible(true);
		romFrame.pack();
	}
	
	
	// Call this method to update list of available rooms
	private void showAvailableRooms(){
		roomListModel.clear(); // Clears list in case room criteria has changed
		
		ArrayList<Room> capableRooms = DBHandler.getRoomsWithCapacity(numberOfParticipants.getValue());
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
			parent.showLocationField.setText(placeField.getText());
			parent.getAppointment().setPlace(placeField.getText());
			parent.getAppointment().setRom(null);
			romFrame.dispose();
		} else if (e.getSource() == saveRoomBtn){
			parent.showLocationField.setText(roomList.getSelectedValue().getPlace());
			parent.getAppointment().setRom(roomList.getSelectedValue());
			parent.getAppointment().setPlace(null);
			romFrame.dispose();
			
		}
	}
}
