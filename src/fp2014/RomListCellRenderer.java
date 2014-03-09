package fp2014;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

//Vil ta inn et rom etter hvert, når det er laget ferdig.

public class RomListCellRenderer implements ListCellRenderer<String>{

	@Override
	public Component getListCellRendererComponent(JList<? extends String> list,
			String value, int index, boolean isSelected, boolean cellHasFocus) {
		
		ImageIcon radioSelected = new ImageIcon(getClass().getResource("images/SelectedRadioButton.png"));
		ImageIcon radioNotSelected = new ImageIcon(getClass().getResource("images/NonSelectedRadioButton.png"));
		
		JLabel label = new JLabel(value);
		
		if (isSelected) {
			label.setIcon(radioSelected);
		} else {
			label.setIcon(radioNotSelected);
		}
		
		return label;
	}

}
