/*
 * Copyright (C) 2019 phramusca ( https://github.com/phramusca/ )
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

import jamuz.Jamuz;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class ImageUtils {

	public static ImageIcon readIconFromInternet(String url, int height, File file) {
        ImageIcon icon=null;
        try {
            BufferedImage myImage = ImageIO.read(Utils.getFinalURL(url));
            icon = new ImageIcon(((new ImageIcon(myImage).getImage())
					.getScaledInstance(-1, height, 
							java.awt.Image.SCALE_SMOOTH)));
            write(icon, file);
		} catch (IIOException ex) {
            Jamuz.getLogger().log(Level.FINEST, url, ex);
        }
        catch (IOException | NullPointerException ex) {
			Jamuz.getLogger().log(Level.FINEST, url, ex);
		}
        return icon;
    }
	
	public static ImageIcon readIconFromFile(String filename, int height, File file) {
        ImageIcon icon=null;
        try {
			File iconFile = new File(filename);
			if(!iconFile.exists()) {
                return icon;
            }
			BufferedImage myImage = ImageIO.read(iconFile);
            icon = new ImageIcon(((new ImageIcon(myImage).getImage())
					.getScaledInstance(-1, height, 
							java.awt.Image.SCALE_SMOOTH)));
            write(icon, file);
		} 
        catch (IOException | OutOfMemoryError ex) {
			Jamuz.getLogger().log(Level.SEVERE, "", ex);
		}
        return icon;
    }
	   
    //TODO: MAke a smart cleanup feature
    public static boolean write(ImageIcon icon, File file, boolean overwrite) {
        try {
            File folder = new File(FilenameUtils.getFullPath(file.getAbsolutePath()));
            if(!file.exists() || overwrite) {
                if(!folder.exists()) {
                    FileUtils.forceMkdir(folder);
                }
                ImageIO.write(toBufferedImage(icon), "png", file);  //NOI18N
            }
			return true;
        } catch (IOException ex) { 
            Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
			return false;
        } 
    }
	
	public static boolean write(ImageIcon icon, File file) {
		return write(icon, file, true);
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
       BufferedImage bimage = new BufferedImage(
			   img.getWidth(null), 
			   img.getHeight(null), 
			   BufferedImage.TYPE_INT_ARGB);
       Graphics2D bGr = bimage.createGraphics();
       bGr.drawImage(img, 0, 0, null);
       bGr.dispose();
       return bimage;
    }
	
	/**
    * Converts a given ImageIcon into a BufferedImage
    *
	 * @param icon
    * @return The converted BufferedImage
    */
    public static BufferedImage toBufferedImage(ImageIcon icon)
    {
		return toBufferedImage(icon.getImage());
    }
	
	public static ImageIcon getBorderedIfTooBig(BufferedImage image) {
		ImageIcon icon = new ImageIcon(((new ImageIcon(image).getImage())));
		//Shrink icon if too big
		int maxIconSize=500;
		if(image.getWidth()>maxIconSize || image.getHeight()>maxIconSize) {
			icon = new ImageIcon(((icon.getImage()).getScaledInstance(maxIconSize, maxIconSize, java.awt.Image.SCALE_SMOOTH)));
			//The following is to create a border => shows user that image is bigger than displayed
		   int borderWidth = 20;
		   int spaceAroundIcon = 0;
		   Color borderColor = Color.GRAY;
		   BufferedImage bi = new BufferedImage(maxIconSize + (2 * borderWidth + 2 * spaceAroundIcon),maxIconSize + (2 * borderWidth + 2 * spaceAroundIcon), BufferedImage.TYPE_INT_ARGB);
		   Graphics2D g = bi.createGraphics();
		   g.setColor(borderColor);
		   g.drawImage(icon.getImage(), borderWidth + spaceAroundIcon, borderWidth + spaceAroundIcon, null);
		   BasicStroke stroke = new BasicStroke(borderWidth*2); //5 pixels wide (thickness of the border)
		   g.setStroke(stroke);
		   g.drawRect(0, 0, bi.getWidth() - 1, bi.getHeight() - 1);
		   g.dispose();
		   icon = new ImageIcon(bi);
		}
		return icon;
	}
	
	/**
     *
     * @param image The image to be scaled
     * @param newWidth The required width
     * @param newHeight The required width
     *
     * @return The scaled image
     */
    public static BufferedImage scaleImage(BufferedImage image, int newWidth, int newHeight) {
        double thumbRatio = newWidth / (double) newHeight;
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);
        double aspectRatio = imageWidth / (double) imageHeight;
        if (thumbRatio < aspectRatio) {
            newHeight = (int) (newWidth / aspectRatio);
        } else {
            newWidth = (int) (newHeight * aspectRatio);
        }
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, newWidth, newHeight, null);
        return newImage;
    }
	
	public static BufferedImage shrinkImage(BufferedImage image, int maxIconSize) {
		if(image !=null && (image.getWidth()>maxIconSize || image.getHeight()>maxIconSize)) {
			return scaleImage(image, maxIconSize, maxIconSize);
		}
		return image;
	}
	
	public static BufferedImage getEmptyCover() {
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 40);
		BufferedImage coverImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = coverImage.createGraphics();
		FontMetrics fm = g.getFontMetrics(f);
		g.setBackground(Color.LIGHT_GRAY);
		g.clearRect(0, 0, coverImage.getWidth(), coverImage.getHeight());
		g.setFont(f);
		g.setColor(Color.DARK_GRAY);	
		//TODO: Translate
		String text = "No Cover";
		g.drawString(text, (coverImage.getWidth()/2)-(text.length()*12),
				(coverImage.getHeight()/2)+20);  //NOI18N
		return coverImage;
	}
	
	public static BufferedImage getTestCover() {
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 50);
        BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        FontMetrics fm = g.getFontMetrics(f);
        g.setBackground(Color.YELLOW);
        g.clearRect(0, 0, image.getWidth(), image.getHeight());
        g.setBackground(Color.WHITE);
        g.clearRect(20, 20, image.getWidth() -40, image.getHeight()-40);
        g.setFont(f);
        g.setColor(Color.BLUE);
        g.drawString("TEST COVER", 40, 180); 
        return image;
    }
	
}
