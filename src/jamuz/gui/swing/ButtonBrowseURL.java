/*
 * Copyright (C) 2013 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import jamuz.utils.Desktop;
import java.awt.Component; 
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import javax.swing.DefaultCellEditor;  
import javax.swing.JButton;  
import javax.swing.JCheckBox;  
import javax.swing.JTable; 

/**
 * Cell editor extension to display button in a JTable
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ButtonBrowseURL extends DefaultCellEditor {

    private JButton button = null;
    private String buttonValue = null;
    private boolean isClicked;

    /**
	 * Create a new button cell editor
	 */
	public ButtonBrowseURL() {
        super(new JCheckBox());
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        buttonValue = (value == null) ? "" : value.toString(); //NOI18N
        isClicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isClicked) {
			Desktop.openBrowser(buttonValue);
        }
        isClicked = false;
        return buttonValue;
    }

    @Override
    public boolean stopCellEditing() {
        isClicked = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
