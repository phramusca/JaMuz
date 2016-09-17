/*
 * Copyright (C) 2016 phramusca ( https://github.com/phramusca/JaMuz/ )
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
package jamuz.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Desktop {
	
	public static void openBrowser(String url) {
		try {
			String uri = (url).replaceAll(" ", "+"); //NOI18N
			openBrowser(new URI(uri));
		} catch (URISyntaxException ex) {
			Popup.error(ex);
		}
	}
	
	private static void openBrowser(URI uri) {
        if(uri!=null) {
            if (java.awt.Desktop.isDesktopSupported()) { 
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop(); 
                if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) { 
                    try { 
                        desktop.browse(uri); 
                    } catch (IOException ex) { 
                        Popup.error(ex); 
                    } 
                } 
            }
        }
    }
	
	public static void openFolder(String path) {
		if (java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if (desktop.isSupported(java.awt.Desktop.Action.OPEN)) {
                try {
                    File folderToOpen = new File(path);
                    if(folderToOpen.exists()) {
                        desktop.open(folderToOpen);
                    }
                } catch (IllegalArgumentException | IOException ex) {
                    Popup.error(ex);
                }
            }
        }
	}
	
}
