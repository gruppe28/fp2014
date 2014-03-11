package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class EventGUI extends JTextArea {

	
	public EventGUI(int posX, int posY, int width, int height){
		super();
		setEditable(false);
		setText("This is a test, or is it...");
		
		
		setBackground(Color.DARK_GRAY);
		setBounds(posX, posY, width, height);
	}
	
	
	
}
