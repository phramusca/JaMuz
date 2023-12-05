/*
 * Copyright (C) 2017 phramusca <phramusca@gmail.com>
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
package jamuz.process.book;

import jamuz.Jamuz;
import jamuz.gui.swing.FileSizeComparable;
import jamuz.utils.StringManager;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class Book implements Comparable {
	private final String title; 
	private final String title_sort;
	private final String pubdate; //FIXME ZZZ BOOK Filter pubdate (& format: keep only year & maybe month)
	private final String author; //FIXME ZZZ BOOK Filter author (use books_authors_link => ArrayList)
	private final String author_sort;  
	private final String uuid; 
	private final String filenameWithoutExtension;
	private final String path; 
	private FileSizeComparable length;
	private final String comment; 
	private final String rating; 
	private final String language; //FIXME ZZZ BOOK Filter language (use books_languages_link)
	private final List<String> formats; //FIXME ZZZ BOOK Filter format
	private final List<String> tags;
	private final String tagStr;

	/**
	 * Get the value of format
	 *
	 * @return the value of format
	 */
	public String getFormat() {
		//FIXME ZZZ BOOK A book can have multiple formats (epub and azw for instance)
		//Which to select ?
		return formats.contains("EPUB")?"epub":formats.get(0).toLowerCase();
	}
	
	/**
	 *
	 * @param title
	 * @param title_sort
	 * @param pubdate
	 * @param author_sort
	 * @param uuid
	 * @param path
	 * @param comment
	 * @param rating
	 * @param language
	 * @param author
	 * @param filenameWithoutExtension
	 * @param formats
	 * @param tags
	 */
	public Book(String title, String title_sort, String pubdate, String author_sort, 
			String uuid, String path, String comment, String rating, 
			String language, String author, String filenameWithoutExtension, 
			String formats, String tags) {
		this.title = title;
		this.title_sort = title_sort;
		this.pubdate = pubdate;
		this.author_sort = author_sort;
		this.uuid = uuid;
		this.path = path;
		this.length = new FileSizeComparable((long) 0);
		this.comment = comment;
		this.rating = rating;
		this.language = language;
		this.author = author;
		this.filenameWithoutExtension = filenameWithoutExtension;		
		this.formats = StringManager.parseSlashList(formats);
		this.tagStr=tags;
		this.tags=StringManager.parseSlashList(tags); //TODO BOOK Use books_tags_link instead
	}

	/**
     * get thumbnail
     * @param readIfNotFound
     * @return
     */
    public ImageIcon getThumbnail(boolean readIfNotFound) {
        return IconBufferBook.getCoverIcon(uuid, getCoverFilePath(), readIfNotFound);
    }

	/**
	 *
	 * @return
	 */
	public List<String> getTags() {
		return tags;
	}
	    /**
     * is selected ?
     * @return
     */
    public boolean isSelected() {
		return selected;
    }
	
	    /**
     * set selected
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
	private boolean selected;

	/**
	 *
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 *
	 * @return
	 */
	public String getSort() {
		return title_sort;
	}

	/**
	 *
	 * @return
	 */
	public String getPubdate() {
		return pubdate;
	}

	/**
	 *
	 * @return
	 */
	public String getAuthor_sort() {
		return author_sort;
	}

	/**
	 *
	 * @return
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 *
	 * @return
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 *
	 * @return
	 */
	public String getRating() {
		return rating;
	}
	
	private String getLocalPath() {
		return FilenameUtils.concat(
								Jamuz.getOptions().get("book.calibre"), 
								path);
	}
	
	/**
	 *
	 * @return
	 */
	public String getRelativeFullPath() {
		return FilenameUtils.concat(path,
				filenameWithoutExtension).concat(".").concat(getFormat());
	}
	
	private String getCoverFilePath() {
		String file = FilenameUtils.concat(
						getLocalPath(), 
						"c2o_resizedcover.jpg");
		File iconFile = new File(file);
		if(!iconFile.exists()) {
			file = FilenameUtils.concat(
						getLocalPath(), 
						"cover.jpg");
		}
		return file;
	}
	
	/**
	 *
	 * @return
	 */
	public String getFullPath() {
		return FilenameUtils.concat(getLocalPath(), filenameWithoutExtension)
				.concat(".").concat(getFormat());
	}

	/**
     * set length
     * @param length
     */
    public void setLength(long length) {
        this.length = new FileSizeComparable(length);
    }

    /**
     * get length
     * @return
     */
    public FileSizeComparable getLength() {
        return length;
    }
	
	/**
	 *
	 * @return
	 */
	public boolean isLocal() {
        return getLength().getLength()>0;
    }
	
	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "<html><b>"+this.title+"</b><BR/><i>"+author+"</i><BR/>"+tagStr+"<BR/><BR/>"+comment+"</html>";
	}
	
	/**
	 * Overring method for sorting by title
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(Object o) {
		return (this.title_sort.compareTo(((Book) o).title_sort));
	}
}
