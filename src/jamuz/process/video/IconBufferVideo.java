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

package jamuz.process.video;

import jamuz.Jamuz;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import jamuz.utils.Popup;
import jamuz.utils.StringManager;
import jamuz.utils.Utils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */


public class IconBufferVideo {
    private static final Map<String, ImageIcon> icons = new HashMap<>();

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
     * @param url
     * @param readIfNotFound
     * @return
     */
    public static ImageIcon getCoverIcon(String url, boolean readIfNotFound) {
		if(icons.containsKey(url)) {
            return icons.get(url);
        }
        ImageIcon icon=null;
        if(readIfNotFound) {
            //Icon not found, retrieving it and add it to the map
            icon= readIconFromCache(url);
            if(icon!=null) {
                icons.put(url, icon);
                return icon;
            }
            icon= readIconFromInternet(url);
            if(icon!=null) {
                icons.put(url, icon);
            }
        }
        return icon;
	}
 
    //TODO: Offer at least a cache cleanup function (better would be a smart auto cleanup)
    private static ImageIcon readIconFromCache(String url) {
        try {
            File file = getCacheFile(url);
            if(file.exists()) {
                return new ImageIcon(ImageIO.read(file));
            }
            return null;
        } catch (IOException ex) {
            Popup.error(ex);
            return null;
        }
    }
    
    private static File getCacheFile(String url) {
        return Jamuz.getFile(
				StringManager.removeIllegal(url)+".png", 
				"data", "cache", "video");
    }

    private static ImageIcon readIconFromInternet(String url) {
        ImageIcon icon=null;
        try {
            BufferedImage myImage = ImageIO.read(Utils.getFinalURL(url));
            icon = new ImageIcon(((new ImageIcon(myImage).getImage())
					.getScaledInstance(-1, IconBufferVideo.ICON_HEIGHT, 
							java.awt.Image.SCALE_SMOOTH)));
            
            //Write to cache
            BufferedImage bi = new BufferedImage(
					icon.getImage().getWidth(null),
					icon.getImage().getHeight(null),
					BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g2 = bi.createGraphics();
            g2.drawImage(icon.getImage(), 0, 0, null);
            g2.dispose();
            ImageIO.write(bi, "png", getCacheFile(url)); //NOI18N
            
		} catch (IIOException ex) {
            Jamuz.getLogger().log(Level.FINEST, url, ex);
        }
        catch (IOException | NullPointerException ex) {
			Jamuz.getLogger().log(Level.FINEST, url, ex);
		}
        return icon;
    }

}
