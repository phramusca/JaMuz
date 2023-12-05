/*
 * Copyright (C) 2015 phramusca <phramusca@gmail.com>
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

import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
//TODO: Develop then Use this for all the process start/abort/aborting button
//Custom text for each state
public class ButtonProcess extends JButton {
    
	/**
	 *
	 * @param icon
	 */
	public ButtonProcess(ImageIcon icon) {
        this.state = State.STOPPED;
        setIcon(icon);
        setMargin(new Insets(0, 0, 0, 0));
        setIconTextGap(0);
        setBorderPainted(false);
        setBorder(null);
        setText(null);
        setSize(icon.getImage().getWidth(null), icon.getImage().getHeight(null));
    }
    
	/**
	 *
	 * @param text
	 */
	public ButtonProcess(String text) {
        this.state = State.STOPPED;
        setText(text);
    }
    
    private enum State {
        STOPPED,
        RUNNING,
        ABORTING;
    }
    
    private final State state;
    
	/**
	 *
	 * @return
	 */
	public boolean isRunning() {
        return state.equals(State.RUNNING);
    }
}
