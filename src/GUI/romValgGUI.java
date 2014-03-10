package GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
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
import database.Database;
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
	
	public romValgGUI() {
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
		try {
			roomList.clear(); // Clears list in case room criteria has changed
			Database db = new Database();
			int minPlass = antDeltagere.getValue();
			boolean validRoom;
			String from = "10:00";
			String to = "11:00";
			
			ResultSet rs = db.query("select * from Rom WHERE antPlasser >= " + minPlass + "");
		    while (rs.next()) {
		    	validRoom = true;
		    	ResultSet avtaleRes = db.query("select * from Avtale WHERE romNr = " + rs.getInt("romNr") + "");
		    	
		    	while (avtaleRes.next()) {
		    		if(checkOverlap(from, to, avtaleRes.getString("starttidspunkt"), avtaleRes.getString("sluttidspunkt"))) { validRoom = false; }
		    	}
		    	
		    	
		    	
		    	if(validRoom) { roomList.addElement(new Rom(rs.getInt("romNr"), rs.getString("sted"), rs.getInt("antPlasser"), rs.getString("Beskrivelse"))); }
		    	}
	    	db.close();

		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	private boolean checkOverlap(String from1, String to1, String from2, String to2){
		// Convert strings to floats
		float from1float = Float.parseFloat(from1.replace(":", "."));
		float from2float = Float.parseFloat(from2.replace(":", "."));
		float to1float = Float.parseFloat(to1.replace(":", "."));
		float to2float = Float.parseFloat(to2.replace(":", "."));
		
		// Check for overlap
		return (from2float > from1float && from2float < to1float) || (to2float > from1float && to2float < to1float) || (from2float <= from1float && to2float >= to1float);
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
