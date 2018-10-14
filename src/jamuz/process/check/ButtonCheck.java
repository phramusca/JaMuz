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

package jamuz.process.check;

import jamuz.process.check.ProcessCheck.Action;
import java.awt.Color;
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
public class ButtonCheck extends DefaultCellEditor {

    private JButton button = null;
    private FolderInfo folder = null;
    private boolean isClicked;

    /**
	 * Create a new button cell editor
	 */
	public ButtonCheck() {
        super(new JCheckBox());
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener((ActionEvent e) -> {
			fireEditingStopped();
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
        folder = (FolderInfo) value;
        
        if(folder.isActionDone) {
            isClicked = false;
            button.setIcon(null);
            button.setText("Done");
            button.setBackground(Color.GRAY); 
        }
        else if(folder.getResults().size()<=0 || folder.action==Action.ANALYZING) {
            isClicked = false;
            button.setIcon(null);
            button.setText("N/A");
            button.setBackground(Color.GRAY); 
        }
        else {
            isClicked = true;
            button.setText("...");
        }
        button.setEnabled(isClicked);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isClicked) {
            DialogCheck.main(folder);
        }
        isClicked = false;
        return folder;
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
