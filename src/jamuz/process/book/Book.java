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
import jamuz.process.video.VideoAbstract;
import javax.swing.ImageIcon;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Book implements Comparable {
	private final String title; 
	private final String title_sort;
	private final String pubdate; //FIXME BOOK Format pubdate (keep only year & maybe month)
	private final String author; //FIXME BOOK Can have multiple authors
	private final String author_sort;  
	private final String uuid; 
	private final String filename; //FIXME BOOK Manage multiple formats
	private final String path; 
	private FileSizeComparable length;
	private final String comment; 
	private final String rating; 
	private final String language; //FIXME BOOK Fiter language
	
	//FIXME BOOK Read tags and filter
	//FIXME BOOK Check and complete Export feature
	//FIXME BOOK Check options gui
	//FIXME BOOK Fix jList menu
	
	public Book(String title, String title_sort, String pubdate, String author_sort, 
			String uuid, String path, String comment, String rating, 
			String language, String author, String filename) {
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
		this.filename = filename;
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

	public String getUuid() {
		return uuid;
	}

	public String getRating() {
		return rating;
	}
	
	private String getPath() {
		return FilenameUtils.concat(
								Jamuz.getOptions().get("book.source"), 
								path);
	}
	
	private String getCoverFilePath() {
		return FilenameUtils.concat(
						getPath(), 
						"c2o_resizedcover.jpg");
	}
	
	public String getFilePath() {
		return FilenameUtils.concat(getPath(), filename);
		
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
		return "<html><b>"+this.title+"</b><BR/><i>"+author+"</i><BR/>"+comment+"</html>";
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
