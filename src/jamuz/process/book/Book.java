/*
 * Copyright (C) 2017 phramusca ( https://github.com/phramusca/JaMuz/ )
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
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
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

	public String getTitle() {
		return title;
	}

	public String getSort() {
		return title_sort;
	}

	public String getPubdate() {
		return pubdate;
	}

	public String getAuthor_sort() {
		return author_sort;
	}

	public String getAuthor() {
		return author;
	}

	public String getUuid() {
		return uuid;
	}

	public String getRating() {
		return rating;
	}
	
	private String getLocalPath() {
		return FilenameUtils.concat(
								Jamuz.getOptions().get("book.calibre"), 
								path);
	}
	
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
	
	public boolean isLocal() {
        return getLength().getLength()>0;
    }
	
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
