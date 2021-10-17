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

package jamuz.gui.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */


public class PopupListener extends MouseAdapter {
    private final JPopupMenu popupMenu;

    /**
     * create a popup menu listener
     * @param popupMenu
     */
    public PopupListener(JPopupMenu popupMenu) {
		this.popupMenu = popupMenu;
	}
        
	/**
	 *
	 * @param e
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		maybeShowPopup(e);
	}

	/**
	 *
	 * @param e
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popupMenu.show(e.getComponent(),
					   e.getX(), e.getY());
		}
	}
}
