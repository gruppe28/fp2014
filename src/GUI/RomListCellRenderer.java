package GUI;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import fp2014.Room;

@SuppressWarnings("rawtypes")
public class RomListCellRenderer implements ListCellRenderer{

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		
		// Import icons for selected/unselected items
		ImageIcon radioSelected = new ImageIcon(getClass().getResource("/fp2014/images/SelectedRadioButton.png"));
		ImageIcon radioNotSelected = new ImageIcon(getClass().getResource("/fp2014/images/NonSelectedRadioButton.png"));
		
		// Set item text to room name ("sted")
		JLabel label = new JLabel(((Room) value).getPlace());
		
		// Apply styles to item
		if (isSelected) {
			label.setOpaque(true);
			label.setIcon(radioSelected);
			label.setBackground(new Color(0xB5DCFF));
		} else {
			label.setIcon(radioNotSelected);
		}
		
		// Return the label
		return label;
	}

}
