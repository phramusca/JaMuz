/*
 * Copyright (C) 2014 phramusca
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

import jamuz.process.check.Cover;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

/**
 * ComboBox renderer to display images
 * @author phramusca
 */
public class ListRendererCover extends JLabel implements ListCellRenderer {

	/**
	 * Create a new Combobox renderer
	 */
	public ListRendererCover() {
		setOpaque(true);
        setHorizontalAlignment(SwingConstants.CENTER);
        // Text below image
        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        // And centered
        this.setHorizontalTextPosition(SwingConstants.CENTER); 
    }

    /*
    * This method finds the image and text corresponding
    * to the selected value and returns the label, set up
    * to display the text and image.
    */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

         Cover cellValue = (Cover) value;
        
        //Set the icon.
         BufferedImage image = cellValue.getImage();
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
             
         setIcon(icon);
         
         //Set text
         setText(cellValue.toString());
        
        return this;
    }
}
