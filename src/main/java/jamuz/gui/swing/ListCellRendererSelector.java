/*
 * Copyright (C) 2016 phramusca <phramusca@gmail.com>
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

import jamuz.IconBuffer;
import jamuz.IconBufferCover;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class ListCellRendererSelector extends JLabel implements ListCellRenderer
{

	/**
	 *
	 */
	public ListCellRendererSelector()
	{
		setOpaque(true);
		setHorizontalAlignment(2);
		setVerticalAlignment(0);
	}
  
	/**
	 *
	 * @param list
	 * @param value
	 * @param index
	 * @param isSelected
	 * @param cellHasFocus
	 * @return
	 */
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		}
		else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		ListElement cellValue = (ListElement)value;
		String text = cellValue.toString();
		ImageIcon icon;
		if (cellValue.getFile() != null) {
			icon = IconBufferCover.getCoverIcon(cellValue.getFile(), false);
		}
		else {
			icon = IconBuffer.getCoverIcon(cellValue.toString(), IconBuffer.IconVersion.NORMAL_50, "genre");
			switch (text) {
				case "artist": 
					text = "Artiste (Tous)";
					break;
				case "album": 
					text = "Album (Tous)";
			}
		}
		setIcon(icon);
		setText(text);

		return this;
	}
}
