/*
 * Copyright (C) 2014 phramusca ( https://github.com/phramusca/JaMuz/ )
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

package jamuz.gui;

import jamuz.IconBuffer;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * ComboBox renderer to display images
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ListCellRendererGenre extends JLabel implements ListCellRenderer {

//    public static final int genreIconSize = 70;
    
	/**
	 * Create a new Combobox renderer
	 */
	public ListCellRendererGenre() {
		setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    /*
    * This method finds the image and text corresponding
    * to the selected value and returns the label, set up
    * to display the text and image.
    */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        String cellValue = (String) value;
        ImageIcon icon=IconBuffer.getCoverIcon(cellValue.equals("%")?"genre":cellValue, "genre");
        setIcon(icon);
        
        if(icon==null) {
            setText(cellValue.equals("%")?"Genre (Tous)":cellValue);
        }
        else {
            setText(""); //NOI18N
        }

        return this;
    }
}
