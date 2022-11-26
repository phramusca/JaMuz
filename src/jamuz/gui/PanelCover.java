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

package jamuz.gui;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

/**
 * JPanel extension to display a cover
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PanelCover extends JPanel {
          
    private transient BufferedImage image;
	private boolean isCover = false;
	private boolean limitToDouble = false;
			
	/**
	 * Returns if this is a valid cover
	 * @return
	 */
	public boolean isCover() {
		return this.isCover;
	}
	
	/**
	 * Sets image
	 * @param image
	 */
	public void setImage(BufferedImage image) {
		setImage(image, false);
	}
	
	/**
	 * Sets image
	 * @param image
	 * @param limitToDouble
	 */
	public void setImage(BufferedImage image, boolean limitToDouble) {
		this.image = image;
		this.repaint();
        this.isCover = image!=null;
		this.limitToDouble = limitToDouble;
	}

	/**
	 * Returns image
	 * @return
	 */
	public BufferedImage getImage() {
		return this.image;
	}
	
	/**
	 *
	 * @param g
	 */
	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.image != null) {
			
			int imageWidth = limitToDouble  ? this.image.getWidth()  : this.image.getWidth();
			int imaheHeight = limitToDouble ? this.image.getHeight() : this.image.getHeight();
			
			
			// Scale it by width
			int scaledWidth = ((this.image.getWidth() * getHeight()/this.image.getHeight()));
			
			if(limitToDouble) {
				if(scaledWidth > this.image.getWidth() * 2) {
					scaledWidth = this.image.getWidth() * 2;
				}
				// If the image is not off the screen horizontally...
				if (scaledWidth < getWidth()) {
					// Center the left and right destination x coordinates.
					int leftOffset = getWidth() / 2 - scaledWidth / 2;
					int rightOffset = getWidth() / 2 + scaledWidth / 2;
					int scaledHeight = (this.image.getHeight() * getWidth()) / this.image.getWidth();
					int topOffset = 0;
					int bottomOffset = getHeight();
					if(scaledHeight > this.image.getHeight() * 2) {
						scaledHeight = this.image.getHeight() * 2;
						topOffset = getHeight() / 2 - scaledHeight / 2;
						bottomOffset = getHeight() / 2 + scaledHeight / 2;
					}
					g.drawImage(this.image, leftOffset, topOffset, rightOffset, bottomOffset, 0, 0, this.image.getWidth(), this.image.getHeight(), null);
				}
				// Otherwise, the image width is too much, even scaled
				// So we need to center it the other direction
				else {
					int scaledHeight = (this.image.getHeight() * getWidth()) / this.image.getWidth();
					int leftOffset = 0;
					int rightOffset = getWidth();
					if(scaledHeight > this.image.getHeight()* 2) {
						scaledHeight = this.image.getHeight() * 2;
						leftOffset = getWidth() / 2 - scaledWidth / 2;
						rightOffset = getWidth() / 2 + scaledWidth / 2;
					}
					int topOffset = getHeight() / 2 - scaledHeight / 2;
					int bottomOffset = getHeight() / 2 + scaledHeight / 2;
					g.drawImage(this.image, leftOffset, topOffset, rightOffset, bottomOffset, 0, 0, this.image.getWidth(), this.image.getHeight(), null);
				}
			} else {
				// If the image is not off the screen horizontally...
				if (scaledWidth < getWidth()) {
					// Center the left and right destination x coordinates.
					int leftOffset = getWidth() / 2 - scaledWidth / 2;
					int rightOffset = getWidth() / 2 + scaledWidth / 2;
					g.drawImage(this.image, leftOffset, 0, rightOffset, getHeight(), 0, 0, this.image.getWidth(), this.image.getHeight(), null);
				}
				// Otherwise, the image width is too much, even scaled
				// So we need to center it the other direction
				else {
					int scaledHeight = (this.image.getHeight() * getWidth()) / this.image.getWidth();
					int topOffset = getHeight() / 2 - scaledHeight / 2;
					int bottomOffset = getHeight() / 2 + scaledHeight / 2;
					g.drawImage(this.image, 0, topOffset, getWidth(), bottomOffset, 0, 0, this.image.getWidth(), this.image.getHeight(), null);
				}
			}
		}
    }
}

