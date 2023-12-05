/*
 * Copyright (C) 2014 phramusca <phramusca@gmail.com>
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

import jamuz.utils.ImageUtils;
import jamuz.utils.Popup;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class IconBufferCover {

	/**
	 * Cover Icon Size
	 */
	private static final int coverIconSize = 50;

	/**
	 *
	 * @return
	 */
	public static int getCoverIconSize() {
		return coverIconSize;
	}

	/**
	 *
	 * @param file
	 * @param readIfNotFound
	 * @return
	 */
	public static ImageIcon getCoverIcon(FileInfoInt file, boolean readIfNotFound) {
		ImageIcon icon;
		icon = readIconFromCache(file.getCoverHash());
		if (icon != null) {
			return icon;
		}
		if (readIfNotFound) {
			BufferedImage coverImage = file.getCoverImage();
			icon = new ImageIcon(coverImage.getScaledInstance(coverIconSize, coverIconSize, java.awt.Image.SCALE_SMOOTH));
			file.unsetCover();
			ImageUtils.write(icon, getCacheFile(file.getCoverHash()), true);
		}
		return icon;

	}

	//TODO: Offer at least a cache cleanup function (better would be a smart auto cleanup)
	//Until then, can delete cache folder (or only audio)
	private static ImageIcon readIconFromCache(String coverHash) {
		try {
			File file = getCacheFile(coverHash);
			if (file.exists()) {
				return new ImageIcon(ImageIO.read(file));
			}
			return null;
		} catch (IOException ex) {
			Popup.error(ex);
			return null;
		}
	}

	private static File getCacheFile(String coverHash) {
		String filename = coverHash.isBlank() ? "NA" : coverHash;
		return Jamuz.getFile(filename + ".png", "data", "cache", "audio");
	}
}
