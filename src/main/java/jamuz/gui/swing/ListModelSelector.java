/*
 * Copyright (C) 2014 phramusca <phramusca@gmail.com>
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

import jamuz.IconBufferCover;
import jamuz.utils.Popup;
import jamuz.utils.ProcessAbstract;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
//Needed to extend ListModel as reading image within renderer freezes MainGUI.
public class ListModelSelector extends DefaultListModel {
    
    private static LoadIconsThread tLoadIcons;

    /**
     * fill lists in thread
     */
    public void loadIconsInThread() {
        //Stop any previously running thread and wait for it to end
        //TODO: No need to stop previous as always called on a new ListModelSelector. Make sure previous is well removed by GC, then remove the abort check (and ability as no need)
		if(tLoadIcons!=null) {
            tLoadIcons.abort();
			try {
				tLoadIcons.join();
			} catch (InterruptedException ex) {
				Popup.error(ex);
			}
		}

		// Démarrage du thread
		tLoadIcons = new LoadIconsThread("Thread.ListModelSelector.loadIconsInThread");
		tLoadIcons.start();
	}
    
    private class LoadIconsThread extends ProcessAbstract {

        LoadIconsThread(String name) {
            super(name);
        }
		
        @Override
		public void run() {
            try {
                loadIcons(); //
            }
            catch (InterruptedException e) {
            } 
		}
	}
    
    private void loadIcons() throws InterruptedException {
        List items=Collections.list(this.elements());
        for(Object obj : items) {
            tLoadIcons.checkAbort();
            ListElement albumElement = (ListElement) obj;
            if(albumElement.getFile()!=null) {
                IconBufferCover.getCoverIcon(albumElement.getFile(), true);
                this.fireContentsChanged(obj, 0, 0);
            }
        }
    }

}