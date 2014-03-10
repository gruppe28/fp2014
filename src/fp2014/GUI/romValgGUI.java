package fp2014.GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
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

import fp2014.RomListCellRenderer;

@SuppressWarnings("serial")
public class romValgGUI extends JPanel implements ActionListener{
	
	JLabel stedLabel;
	JTextField motested;
	JButton lagreSted;
	JSlider antDeltagere;
	JList romList;
	JScrollPane romListScroller;
	JButton lagreRom;
	JLabel antDeltagereLabel;
	JRadioButton velgMotested;
	
	
	public romValgGUI() {

		JDialog romFrame = new JDialog();
		JPanel romPanel = new JPanel();
		
		velgMoterom = new JRadioButton("Velg moterom", true);
		velgMotested = new JRadioButton("Velg motested");
		
		velgMoterom.addActionListener(this);
		velgMotested.addActionListener(this);
		
		
		romPanel.add(velgMoterom);
		romPanel.add(velgMotested);
		
		/*
		 * Motested-komponenter
		 */
		
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
			}
		});
		antDeltagereLabel = new JLabel(Integer.toString(antDeltagere.getValue()));
		
		DefaultListModel romListModel = new DefaultListModel();
		
		//Test-elementer
		romListModel.addElement("Rom #1");
		romListModel.addElement("Rom #2");
		romListModel.addElement("Rom #3");
		romListModel.addElement("Rom #4");
		romListModel.addElement("Rom #5");
		romListModel.addElement("Rom #6");
		romListModel.addElement("Rom #7");
		romListModel.addElement("Rom #8");
		romListModel.addElement("Rom #9");
		romListModel.addElement("Rom #10");
		romListModel.addElement("Rom #11");
		romListModel.addElement("Rom #12");
		
		romList = new JList(romListModel);
		romListScroller = new JScrollPane(romList);
		romListScroller.setPreferredSize(new Dimension(300, 150));

		romList.setCellRenderer(new RomListCellRenderer());
		romList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		lagreRom = new JButton("Lagre");
		
		
		/*
		 * 
		 */
		
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

	@Override
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
