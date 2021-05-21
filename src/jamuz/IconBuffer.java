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

package jamuz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import javax.swing.GrayFilter;
import javax.swing.Icon;

//FIXME Z Looks like there is only one repo for both tags and genre => Split
// So that "Classique" and "Musical" are no more mixed

/**
 *
 * @author raph
 */
public class IconBuffer {
    private static final Map<String, Map<IconVersion, ImageIcon>> icons = new HashMap<>();

	/**
	 *
	 */
	public enum IconVersion {

		/**
		 *
		 */
		NORMAL_70,

		/**
		 *
		 */
		NORMAL_50,

		/**
		 *
		 */
		NORMAL_30,

		/**
		 *
		 */
		GRAY_30
    }
    
    /**
     * Icon size (pixels)
     */
    public static final int iconSize = 70;
    
	/**
	 *
	 * @param name
	 * @param location
	 * @return
	 */
	public static ImageIcon getCoverIcon(String name, String location) {
        return getCoverIcon(name, IconVersion.NORMAL_70, location);
    }
    
    /**
     * Get Cover Icon
     * @param name
     * @param version
     * @param location
     * @return
     */
    public static ImageIcon getCoverIcon(String name, IconVersion version, String location) {
		if(icons.containsKey(name)) {
            if(icons.get(name).containsKey(version)) {
                return icons.get(name).get(version);
            }
            icons.remove(name); //Will re-read all, should not happen anyway
        }
        //Icon not found, retrieving it and add it to the map
        BufferedImage image=null;
        if(iconFileExists(name, "png", location)) { //NOI18N
            image = readImage(name, "png", location); //NOI18N
        }
        else if(iconFileExists(name, "jpg", location)) { //NOI18N
            image = readImage(name, "jpg", location); //NOI18N
        }
        //Making all versions at once. Yes, why not ? We're reading file only once as it
        Map<IconVersion, ImageIcon> versionMap = new HashMap<>();
        if(image!=null) {
            versionMap.put(IconVersion.NORMAL_70, getIcon(image, IconVersion.NORMAL_70));
			versionMap.put(IconVersion.NORMAL_50, getIcon(image, IconVersion.NORMAL_50));
            versionMap.put(IconVersion.NORMAL_30, getIcon(image, IconVersion.NORMAL_30));
            versionMap.put(IconVersion.GRAY_30, getIcon(image, IconVersion.GRAY_30));
        }
        icons.put(name, versionMap);
        
        return icons.get(name).get(version);
	}
    
    private static ImageIcon getIcon(BufferedImage image, IconVersion version) {
        switch(version) {
            case NORMAL_70:
                return getScaledInstance(image, 70);
			case NORMAL_50:
                return getScaledInstance(image, 50);
            case NORMAL_30:
                return getScaledInstance(image, 30);
            case GRAY_30:
                return getGray(getScaledInstance(image, 30));
        }
        return null;
    }
    
    /**
     * @see http://stackoverflow.com/q/14358499/230513
     * @see http://stackoverflow.com/a/12228640/230513
     */
    private static ImageIcon getGray(Icon icon) {
        final int w = icon.getIconWidth();
        final int h = icon.getIconHeight();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(w, h);
        Graphics2D g2d = image.createGraphics();
        icon.paintIcon(null, g2d, 0, 0);
        Image gray = GrayFilter.createDisabledImage(image);
        return new ImageIcon(gray);
    }    

    private static boolean iconFileExists(String genre, String ext, String location) {
        File iconFile = getFile(genre, ext, location);
        return iconFile.exists();
    }
    
    private static File getFile(String name, String ext, String location) {
        return Jamuz.getFile(name+"."+ext, "data", "icon", location);
    }
    
    private static ImageIcon getScaledInstance(BufferedImage image, int size) {
        return new ImageIcon(image.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH));
    }
    
    private static BufferedImage readImage(String name, String ext, String location) {
        //Read the icon.
        File iconFile = getFile(name, ext, location);
        BufferedImage image = null;
        try { 
            image = ImageIO.read(iconFile);
        } catch (IOException ex) {
            Popup.error(java.text.MessageFormat.format(Inter.get("Error.ReadingCover"), new Object[] {iconFile}), ex); //NOI18N
        }
        return image;
    }

}
