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

import jamuz.process.video.FileInfoVideo;
import jamuz.process.video.VideoAbstract;
import java.util.List;
import java.util.TreeMap;
import javax.swing.ImageIcon;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Book {
	private String title; 
	private String sort;
	private String pubdate; 
	private String author_sort;  
	private String uuid; 
	private String path; 
	protected List<String> thumbnails;
	

	public Book(String title, String sort, String pubdate, String author_sort, String uuid, String path) {
		this.title = title;
		this.sort = sort;
		this.pubdate = pubdate;
		this.author_sort = author_sort;
		this.uuid = uuid;
		this.path = path;
	}

	/**
     * get thumbnail
     * @param readIfNotFound
     * @return
     */
    public ImageIcon getThumbnail(boolean readIfNotFound) {
        ImageIcon icon=null;
        for(String url : this.thumbnails) {
            icon=IconBufferBook.getCoverIcon(url, readIfNotFound);
            if(icon!=null) {
                return icon;
            }
        }
        return icon;
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
		return sort;
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

	public String getPath() {
		
		//FIXME: Return relative Full Path
		return path;
	}
		
	
}
