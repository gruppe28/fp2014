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
import fp2014.Rom;

@SuppressWarnings({"serial", "unchecked"})
public class romValgGUI extends JPanel implements ActionListener{
	
	JLabel stedLabel;
	JTextField motested;
	JButton lagreSted;
	JSlider antDeltagere;
	JList<Rom> romList;
	JScrollPane romListScroller;
	JButton lagreRom;
	JLabel antDeltagereLabel;
	JRadioButton velgMotested;
	JRadioButton velgMoterom;
	DefaultListModel<Rom> roomList;
	ArrayList<Rom> availableRooms;
	
	String date;
	String from;
	String to;
	
	public romValgGUI(String date, String from, String to) {
		
		this.date = date;
		this.from = from;
		this.to = to;
		
		
		JDialog romFrame = new JDialog();
		JPanel romPanel = new JPanel();
		romFrame.setTitle("Select location");
		
		velgMoterom = new JRadioButton("Velg moterom", true);
		velgMotested = new JRadioButton("Velg motested");
		
		velgMoterom.addActionListener(this);
		velgMotested.addActionListener(this);
		
		
		romPanel.add(velgMoterom);
		romPanel.add(velgMotested);
		
		// Møtested-kompo
		
		stedLabel = new JLabel("Skriv inn motested:");
		motested = new JTextField(20);
		lagreSted = new JButton("Lagre");
		
		
		/*
		 * Moterom-komponenter
		 */
		
		antDeltagere = new JSlider(1, 40, 10);
		antDeltagere.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				antDeltagereLabel.setText(Integer.toString(antDeltagere.getValue()));
				showAvailableRooms();
				
			}
		});
		antDeltagereLabel = new JLabel(Integer.toString(antDeltagere.getValue()));
		
		

		// List of available rooms
		availableRooms = DBHandler.getAvailableRooms(date, from, to); //Fetch rooms available that date and time from database
		roomList = new DefaultListModel<Rom>();
		showAvailableRooms(); // Fills list
		romList = new JList<Rom>(roomList);
		romListScroller = new JScrollPane(romList);
		romListScroller.setPreferredSize(new Dimension(300, 150));
		romList.setCellRenderer(new RomListCellRenderer());
		romList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lagreRom = new JButton("Lagre");
		
		// Add elements to window
		romPanel.add(stedLabel);
		romPanel.add(motested);
		romPanel.add(lagreSted);
		romPanel.add(antDeltagere);
		romPanel.add(antDeltagereLabel);
		romPanel.add(romListScroller);
		romPanel.add(lagreRom);

		stedLabel.setVisible(false);
		motested.setVisible(false);
		lagreSted.setVisible(false);
		antDeltagere.setVisible(true);
		antDeltagereLabel.setVisible(true);
		romListScroller.setVisible(true);
		romList.setVisible(true);
		lagreRom.setVisible(true);
		
		ButtonGroup group = new ButtonGroup();
		group.add(velgMotested);
		group.add(velgMoterom);
		
		romFrame.setModal(true);
		romFrame.setMinimumSize(new Dimension(350, 350));
		romFrame.setContentPane(romPanel);
		romFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		romFrame.setVisible(true);
		romFrame.pack();
		
	}
	
	
	// Call this method to update list of available rooms
	private void showAvailableRooms(){
		roomList.clear(); // Clears list in case room criteria has changed
		
		ArrayList<Rom> capableRooms = DBHandler.getRoomsWithCapacity(antDeltagere.getValue());
		ArrayList<Integer> roomNumbers = new ArrayList<Integer>();

		for(int i = 0; i < availableRooms.size(); i++){
			roomNumbers.add(availableRooms.get(i).getRomNr());
		}
		
		for(int i = 0; i < capableRooms.size(); i++){
			if(roomNumbers.contains(capableRooms.get(i).getRomNr())){ roomList.addElement(capableRooms.get(i)); }
		}
	}


	// Listeners switching between meeting room/place
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == velgMoterom) {
			
			stedLabel.setVisible(false);
			motested.setVisible(false);
			lagreSted.setVisible(false);
			antDeltagere.setVisible(true);
			antDeltagereLabel.setVisible(true);
			romListScroller.setVisible(true);
			romList.setVisible(true);
			lagreRom.setVisible(true);
			
		} else if (e.getSource() == velgMotested) {
			stedLabel.setVisible(true);
			motested.setVisible(true);
			lagreSted.setVisible(true);
			antDeltagere.setVisible(false);
			antDeltagereLabel.setVisible(false);
			romListScroller.setVisible(false);
			romList.setVisible(false);
			lagreRom.setVisible(false);
		}
	}
}
