package fp2014;

import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class CalendarGUI extends JPanel{
	
	public CalendarGUI(){
		
		this.setPreferredSize(new Dimension(804, 500));
		JList list = new JList();
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(804, 500));
		DefaultListModel model = new DefaultListModel();
		list.setCellRenderer(new DefaultListCellRenderer());
		list.setModel(model);
		list.setFixedCellWidth(114);
		
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		list.setVisibleRowCount(96);
		
		for (int i = 0; i < 672; i++) {
			model.addElement("Test" + i);
		}
		
		JTable table = new JTable();
		DefaultTableModel modelt = new DefaultTableModel(96, 7);
		table.setModel(modelt);
		
		TableColumn column = null;
		for (int i = 0; i < table.getColumnCount(); i++) {
			column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth(114);
		}
		
		this.add(listScroller);
		
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel panel = new CalendarGUI();
		
		frame.setContentPane(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
	}
	
}
