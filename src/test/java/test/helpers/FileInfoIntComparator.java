/*
 * Copyright (C) 2023 phramusca <phramusca@gmail.com>
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
package test.helpers;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
import jamuz.FileInfoInt;
import java.awt.image.BufferedImage;
import java.util.Comparator;

public class FileInfoIntComparator implements Comparator<FileInfoInt> {

	@Override
	public int compare(FileInfoInt o1, FileInfoInt o2) {
		if (o1 == o2) {
			return 0;
		}

		if (o1 == null || o2 == null) {
			return o1 == null ? -1 : 1;
		}

		int result = Integer.compare(o1.getTrackNo(), o2.getTrackNo());
		if (result != 0) {
			return result;
		}

		result = Integer.compare(o1.getTrackTotal(), o2.getTrackTotal());
		if (result != 0) {
			return result;
		}

		result = Integer.compare(o1.getDiscNo(), o2.getDiscNo());
		if (result != 0) {
			return result;
		}

		result = Integer.compare(o1.getDiscTotal(), o2.getDiscTotal());
		if (result != 0) {
			return result;
		}

		result = Integer.compare(o1.getNbCovers(), o2.getNbCovers());
		if (result != 0) {
			return result;
		}

		result = Boolean.compare(o1.hasID3v1(), o2.hasID3v1());
		if (result != 0) {
			return result;
		}

		result = Long.compare(o1.getLength(), o2.getLength());
		if (result != 0) {
			return result;
		}

		result = Long.compare(o1.getSize(), o2.getSize());
		if (result != 0) {
			return result;
		}

//		result = Boolean.compare(o1.isFromLibrary(), o2.isFromLibrary());
//		if (result != 0) {
//			return result;
//		}

		result = compareObjects(o1.getArtist(), o2.getArtist());
		if (result != 0) {
			return result;
		}

		result = compareObjects(o1.getAlbum(), o2.getAlbum());
		if (result != 0) {
			return result;
		}

		result = compareObjects(o1.getAlbumArtist(), o2.getAlbumArtist());
		if (result != 0) {
			return result;
		}

		result = compareObjects(o1.getTitle(), o2.getTitle());
		if (result != 0) {
			return result;
		}

		result = compareObjects(o1.getYear(), o2.getYear());
		if (result != 0) {
			return result;
		}

		result = compareObjects(o1.getComment(), o2.getComment());
		if (result != 0) {
			return result;
		}

		result = compareObjects(o1.getCoverHash(), o2.getCoverHash());
		if (result != 0) {
			return result;
		}

		result = compareObjects(o1.getBitRate(), o2.getBitRate());
		if (result != 0) {
			return result;
		}

		result = compareObjects(o1.getFormat(), o2.getFormat());
		if (result != 0) {
			return result;
		}

//		result = compareObjects(o1.getLengthDisplay(), o2.getLengthDisplay());
//		if (result != 0) {
//			return result;
//		}
//
//		result = compareObjects(o1.getSizeDisplay(), o2.getSizeDisplay());
//		if (result != 0) {
//			return result;
//		}

		result = compareObjects(o1.getFullPath(), o2.getFullPath());
		if (result != 0) {
			return result;
		}

		result = compareObjects(o1.getLyrics(), o2.getLyrics());
		if (result != 0) {
			return result;
		}

//		result = compareObjects(o1.getCoverImage(), o2.getCoverImage());
//		if (result != 0) {
//			return result;
//		}
		
		result = compareImages(o1.getCoverImage(), o2.getCoverImage(), (image1, image2) -> {
            // Implement your custom comparison logic for BufferedImage here
            // Example: Compare based on image dimensions, or convert to a common representation for comparison
            // Return 0 if objects are equal, a positive value if image1 > image2, and a negative value if image1 < image2
            return Integer.compare(image1.getWidth(), image2.getWidth());
        });
        if (result != 0) {
            return result;
        }

		result = compareObjects(o1.getFormattedModifDate(), o2.getFormattedModifDate());
		if (result != 0) {
			return result;
		}

		//From path table
//		result = Boolean.compare(o1.isCheckedFlag(), o2.isCheckedFlag());
//		if (result != 0) {
//			return result;
//		}
//		result = Integer.compare(o1.getCopyRight(), o2.getCopyRight());
//		if (result != 0) {
//			return result;
//		}
//		result = Double.compare(o1.getAlbumRating(), o2.getAlbumRating());
//		if (result != 0) {
//			return result;
//		}
//
//		result = Double.compare(o1.getPercentRated(), o2.getPercentRated());
//		if (result != 0) {
//			return result;
//		}
//
//		result = Integer.compare(o1.getStatus(), o2.getStatus());
//		if (result != 0) {
//			return result;
//		}
//
//		result = compareObjects(o1.getPathModifDate(), o2.getPathModifDate());
//		if (result != 0) {
//			return result;
//		}
//
//		result = compareObjects(o1.getPathMbid(), o2.getPathMbid());
//		if (result != 0) {
//			return result;
//		}
//
//		result = compareObjects(o1.getReplaygain(), o2.getReplaygain());
//		if (result != 0) {
//			return result;
//		}

		return result;
	}

	private static <T extends Comparable<? super T>> int compareObjects(T obj1, T obj2) {
        if (obj1 == obj2) {
            return 0;
        }
        if (obj1 == null || obj2 == null) {
            return obj1 == null ? -1 : 1;
        }
        return obj1.compareTo(obj2);
    }
	
	private static <T> int compareImages(T obj1, T obj2, Comparator<? super T> comparator) {
        if (obj1 == obj2) {
            return 0;
        }
        if (obj1 == null || obj2 == null) {
            return obj1 == null ? -1 : 1;
        }
        return comparator.compare(obj1, obj2);
    }

    // Custom comparator for BufferedImage
    private static class BufferedImageComparator implements Comparator<BufferedImage> {
        private static final BufferedImageComparator INSTANCE = new BufferedImageComparator();

        public static BufferedImageComparator getInstance() {
            return INSTANCE;
        }

        @Override
        public int compare(BufferedImage o1, BufferedImage o2) {
            // Implement your custom comparison logic for BufferedImage here
            // Example: Compare based on image dimensions, or convert to a common representation for comparison
            // Return 0 if objects are equal, a positive value if o1 > o2, and a negative value if o1 < o2
            return Integer.compare(o1.getWidth(), o2.getWidth());
        }
    }
}
