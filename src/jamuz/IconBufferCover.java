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

import jamuz.utils.Popup;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */


public class IconBufferCover {

    /**
     * Cover Icon Size
     */
    private static final int coverIconSize = 50;

    public static int getCoverIconSize() {
        return coverIconSize;
    }
    
    public static ImageIcon getCoverIcon(FileInfoInt file, boolean readIfNotFound) {
        ImageIcon icon;        
        //Read from file cache first
        icon= readIconFromCache(file.getCoverHash());
        if(icon!=null) {
            return icon;
        }
        //If not found, reading from tags
        if(readIfNotFound) {
            icon = readIconFromTag(file);
            //Save the icon to file
            if(icon!=null) {
                saveCoverToFile(toBufferedImage(icon.getImage()), file.getCoverHash(), true);
            }
        }
        return icon;

    }
    
    //TODO: Offer at least a cache cleanup function (better would be a smart auto cleanup)
    //Until then, can delete cache folder (or only audio)
    private static ImageIcon readIconFromCache(String coverHash) {
        try {
            File file = getCacheFile(coverHash);
            if(file.exists()) {
                return new ImageIcon(ImageIO.read(file));
            }
            return null;
        } catch (IOException ex) {
            Popup.error(ex);
            return null;
        }
    }
    
    private static File getCacheFile(String coverHash) {
        String filename = coverHash.equals("")?"NA":coverHash;
        return Jamuz.getFile(filename+".png", "data", "cache", "audio");
    }
    
    private static ImageIcon readIconFromTag(FileInfoInt file) {
        
        //Read the cover from file's tags
        BufferedImage coverImage = file.getCoverImage();
        //Generate an icon
        ImageIcon icon = getScaledInstance(coverImage, coverIconSize);
        //Remove image from RAM. TODO: Is this really a good way ?
        file.unsetCover(); 
        return icon;
    }
    
    //TODO: Move those (and other image related general functions) to a dedicated class in Utils
    
    private static ImageIcon getScaledInstance(BufferedImage image, int size) {
        return new ImageIcon(image.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH));
    }

    /**
    * Converts a given Image into a BufferedImage
    *
    * @param img The Image to be converted
    * @return The converted BufferedImage
    */
    public static BufferedImage toBufferedImage(Image img)
    {
       if (img instanceof BufferedImage)
       {
           return (BufferedImage) img;
       }

       // Create a buffered image with transparency
       BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

       // Draw the image on to the buffered image
       Graphics2D bGr = bimage.createGraphics();
       bGr.drawImage(img, 0, 0, null);
       bGr.dispose();

       // Return the buffered image
       return bimage;
    }
    
    //TODO: MAke a smart cleanup feature (also needed for video cache)
    private static void saveCoverToFile(BufferedImage coverImage, String coverHash, boolean overwrite) {
        try {
            File coverCacheFile = getCacheFile(coverHash);
            File coverCacheFolder = new File(FilenameUtils.getFullPath(coverCacheFile.getAbsolutePath()));
            if(!coverCacheFile.exists() || overwrite) {
                if(!coverCacheFolder.exists()) {
                    FileUtils.forceMkdir(coverCacheFolder);
                }
                ImageIO.write(coverImage, "png", coverCacheFile);  //NOI18N
            }
        } catch (IOException ex) { 
            //FIXME: Why such errors are not caught ????
            // Path was indeed not created, so OK with the exception
            //But why not caught ? FileNotFoundException derivates IOException
//            java.io.FileNotFoundException: /home/raph/Documents/04-Creations/Dev/NetBeans/JaMuz/Source/JaMuz_DEV/data/cache/audio/575fc5b16cec2875d676150944ebae72.png (Aucun fichier ou dossier de ce type)
//	at java.io.RandomAccessFile.open(Native Method)
//	at java.io.RandomAccessFile.<init>(RandomAccessFile.java:241)
//	at javax.imageio.stream.FileImageOutputStream.<init>(FileImageOutputStream.java:69)
//	at com.sun.imageio.spi.FileImageOutputStreamSpi.createOutputStreamInstance(FileImageOutputStreamSpi.java:55)
//	at javax.imageio.ImageIO.createImageOutputStream(ImageIO.java:419)
//	at javax.imageio.ImageIO.write(ImageIO.java:1530)
//	at jamuz.buffer.IconBufferCover.saveCoverToFile(IconBufferCover.java:144)
//	at jamuz.buffer.IconBufferCover.readIconFromTag(IconBufferCover.java:102)
//	at jamuz.buffer.IconBufferCover.getCoverIcon(IconBufferCover.java:64)
//	at jamuz.gui.swing.ListModelSelector.loadIcons(ListModelSelector.java:78)
//	at jamuz.gui.swing.ListModelSelector.access$000(ListModelSelector.java:31)
//	at jamuz.gui.swing.ListModelSelector$LoadIconsThread.run(ListModelSelector.java:65)
//Exception in thread "Thread.ListModelAlbum.fillListsInThread" java.lang.NullPointerException
//	at javax.imageio.ImageIO.write(ImageIO.java:1538)
//	at jamuz.buffer.IconBufferCover.saveCoverToFile(IconBufferCover.java:144)
//	at jamuz.buffer.IconBufferCover.readIconFromTag(IconBufferCover.java:102)
//	at jamuz.buffer.IconBufferCover.getCoverIcon(IconBufferCover.java:64)
//	at jamuz.gui.swing.ListModelSelector.loadIcons(ListModelSelector.java:78)
//	at jamuz.gui.swing.ListModelSelector.access$000(ListModelSelector.java:31)
//	at jamuz.gui.swing.ListModelSelector$LoadIconsThread.run(ListModelSelector.java:65)
            Popup.error(ex);
        } 
    }
}
