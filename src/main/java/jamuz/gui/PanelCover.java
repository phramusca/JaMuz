/*
 * Copyright (C) 2011 phramusca <phramusca@gmail.com>
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
 * @author phramusca <phramusca@gmail.com>
 */
public class PanelCover extends JPanel {
          
    private transient BufferedImage image;
	private boolean isCover = false;
	private boolean limitToDouble = false; // TODO: Make an option with more values available (max x2, x1, x3,...)
			
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
			int scaledWidth = ((this.image.getWidth() * getHeight()/this.image.getHeight()));
			int scaledHeight = (this.image.getHeight() * getWidth()) / this.image.getWidth();
				if(limitToDouble && scaledWidth > this.image.getWidth() * 2) {
					scaledWidth = this.image.getWidth() * 2;
				}
				int leftOffset;
				int rightOffset;
				int topOffset;
				int bottomOffset;
				// If the image is not off the screen horizontally...
				if (scaledWidth < getWidth()) {
					// Center the left and right destination x coordinates.
					leftOffset = getWidth() / 2 - scaledWidth / 2;
					rightOffset = getWidth() / 2 + scaledWidth / 2;
					topOffset = 0;
					bottomOffset = getHeight();
					if(limitToDouble && scaledHeight > this.image.getHeight() * 2) {
						scaledHeight = this.image.getHeight() * 2;
						topOffset = getHeight() / 2 - scaledHeight / 2;
						bottomOffset = getHeight() / 2 + scaledHeight / 2;
					}
				}
				// Otherwise, the image width is too much, even scaled
				// So we need to center it the other direction
				else {
					leftOffset = 0;
					rightOffset = getWidth();
					if(limitToDouble && scaledHeight > this.image.getHeight()* 2) {
						scaledHeight = this.image.getHeight() * 2;
						leftOffset = getWidth() / 2 - scaledWidth / 2;
						rightOffset = getWidth() / 2 + scaledWidth / 2;
					}
					topOffset = getHeight() / 2 - scaledHeight / 2;
					bottomOffset = getHeight() / 2 + scaledHeight / 2;
				}
				g.drawImage(this.image, 
					leftOffset < 0 ? 0 : leftOffset, 
					topOffset  < 0 ? 0 : topOffset, 
					rightOffset  > getWidth()  ? getWidth() : rightOffset, 
					bottomOffset > getHeight() ? getHeight() : bottomOffset, 
					0, 0, this.image.getWidth(), this.image.getHeight(), null);
		}
    }
}

