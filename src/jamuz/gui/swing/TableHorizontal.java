/*
 * Copyright (C) 2011 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.JViewport;

/**
 * Allows to dynamically change autoResizeMode depending on 
 * the number of columns and their minimal width.
 * !! WARNING !! : If you need to change the table header height, use:
 * Dimension d = jTable.getTableHeader().getPreferredSize();
 * d.height = 34; //The actual needed height you like
 * jScrollPane.getColumnHeader().setPreferredSize(d);
 * Instead of 
 * jTable.getTableHeader().setPreferredSize(d);
 * Otherwise the JTable display will get very messy !!
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class TableHorizontal extends JTable  {

 @Override
  public Dimension getPreferredSize() {
    if (getParent () instanceof JViewport) {
      if (getParent().getWidth() > super.getPreferredSize().width) {
        return getMinimumSize();
      }
    }
    return super.getPreferredSize(); 
  }

  @Override
  public boolean getScrollableTracksViewportWidth () {
    if (autoResizeMode != AUTO_RESIZE_OFF) {
      if (getParent() instanceof JViewport) {
        return (getParent().getWidth() > getPreferredSize().width);
      }
      return true;
    }
    return false;
  }
}
