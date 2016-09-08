/*
 * Copyright (C) 2011 phramusca
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
 * @author raph
 */
public class PanelCover extends JPanel {
          
    private transient BufferedImage image;
	private boolean isCover=false;

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
		this.image = image;
		this.repaint();
        this.isCover = image!=null;
	}

	/**
	 * Returns image
	 * @return
	 */
	public BufferedImage getImage() {
		return this.image;
	}
	
	/**
	 * Sets a text image ("Select" or "None")
	 * @param text
	 */
	public void setText(String text) {
		this.isCover=false;
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 18);
		this.image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = this.image.createGraphics();
		FontMetrics fm = g.getFontMetrics(f);
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, this.image.getWidth(), this.image.getHeight());
		g.setFont(f);
		g.setColor(Color.RED);
		g.drawString(text, 10, fm.getAscent() + this.getHeight()/2);
		this.repaint();
	}
	
	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
			if (this.image != null) {
			// Scale it by width
			int scaledWidth = (int)((this.image.getWidth() * getHeight()/this.image.getHeight()));
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

