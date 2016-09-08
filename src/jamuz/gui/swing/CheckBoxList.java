/*
 * Copyright (C) 2012 phramusca
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jamuz.gui.swing;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;


// Handles rendering cells in the list using a check box
/**
 * A JList extension class to display check boxes in list
 * @author raph
 */
public class CheckBoxList extends JList
{
	/**
	 * Constructor
	 */
	public CheckBoxList() {
		super();

		setModel(new DefaultListModel());
		setCellRenderer(new CheckListRenderer());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Add a mouse listener to handle changing selection
	     addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				JList list = (JList) event.getSource();
            
				// Get index of item clicked
	            int index = list.locationToIndex(event.getPoint());
		        CheckBoxListItem item = (CheckBoxListItem)
			    list.getModel().getElementAt(index);
            
				// Toggle selected state
	            item.setSelected(! item.isSelected());
           
		        // Repaint cell
	            list.repaint(list.getCellBounds(index, index));
			}
		}); 
	}
	
}
class CheckListRenderer extends JCheckBox implements ListCellRenderer {
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, 
		boolean isSelected, boolean hasFocus) {
		
		setEnabled(list.isEnabled());
		setSelected(((CheckBoxListItem)value).isSelected());
		setFont(list.getFont());
		setBackground(list.getBackground());
		setForeground(list.getForeground());
		setText(value.toString());
		return this;
	}
}