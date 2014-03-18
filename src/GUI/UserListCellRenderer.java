package GUI;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import fp2014.User;

public class UserListCellRenderer implements ListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList list, Object user,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		JLabel label = new JLabel(((User) user).getFirstname() + " " + ((User) user).getLastname());
		
		if (isSelected) {
			label.setOpaque(true);
			label.setBackground(new Color(0xB5DCFF));
		}
		
		return label;
		
	}

}
