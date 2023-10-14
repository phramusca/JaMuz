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

import jamuz.Jamuz;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ClipboardImage {
	/**
	* Get an image off the system clipboard.
	* 
	* @return Returns an Image if successful; otherwise returns null.
	*/
	public static Image getImageFromClipboard()
	{
		Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
		{
			try
			{
				return (Image) transferable.getTransferData(DataFlavor.imageFlavor);
			}
			catch (UnsupportedFlavorException | IOException ex)
			{
				Jamuz.getLogger().log(Level.SEVERE, "getImageFromClipboard: ", ex); 
			}
		}
		else
		{
			Jamuz.getLogger().log(Level.SEVERE, "getImageFromClipboard: That wasn't an image!"); 
		}
		return null;
	}
}
