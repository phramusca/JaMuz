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

package jamuz.process.book;

import jamuz.Jamuz;
import jamuz.utils.ImageUtils;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import jamuz.utils.Popup;
import jamuz.utils.StringManager;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */


public class IconBufferBook {

    /**
     * Icon height.
     * Height set to this value
     */
    public static final int ICON_HEIGHT = 140;

    /**
     * Icon width.
     * Width is set auto based on image ratio, this value is to set column width
     */
    public static final int ICON_WIDTH = 105;
    
    /**
     * Get cover icon from cache if exists, from internet if not
	 * @param key
     * @param file
     * @param readIfNotFound
     * @return
     */
    public static ImageIcon getCoverIcon(String key, String file, boolean readIfNotFound) {
        ImageIcon icon=readIconFromCache(key);
		if(icon==null && readIfNotFound) {
			icon = ImageUtils.readIconFromFile(file, ICON_HEIGHT, getCacheFile(key));
        }
        return icon;
	}
 
    //TODO: Offer at least a cache cleanup function (better would be a smart auto cleanup)
    private static ImageIcon readIconFromCache(String key) {
        try {
            File file = getCacheFile(key);
            if(file.exists()) {
                return new ImageIcon(ImageIO.read(file));
            }
            return null;
        } catch (IOException ex) {
            Popup.error(ex);
            return null;
        }
    }
    
    private static File getCacheFile(String key) {
        return Jamuz.getFile(StringManager.removeIllegal(key)+".png", "data", "cache", "book");
    }
}
