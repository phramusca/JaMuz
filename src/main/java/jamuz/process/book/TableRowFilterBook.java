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

package jamuz.process.book;

import jamuz.gui.swing.TriStateCheckBox;
import jamuz.gui.swing.TriStateCheckBox.State;
import jamuz.utils.Inter;
import java.util.List;
import javax.swing.RowFilter;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */

//TODO: Include this in TableModelVideo, so we can refresh table (fire) when filter changes
public class TableRowFilterBook extends RowFilter {

    private State displaySelected=State.ALL;
	private State displayLocal=State.ALL;
	private String rating = null;
	private String tag = null;
 
    /**
     * Change what to be displayed
     * @param display
     */
    public void displaySelected(TriStateCheckBox.State display) {
        this.displaySelected=display;
    }
        
	
	/**
	 *
	 * @param rating
	 */
	public void displayByRating(String rating) {
        if(rating.equals(Inter.get("Label.All"))) {
            this.rating=null;
        }
        else {
            this.rating=rating;
        }
    }
	
	/**
	 *
	 * @param tag
	 */
	public void displayByTag(String tag) {
        if(tag.equals(Inter.get("Label.All"))) {
            this.tag=null;
        }
        else {
            this.tag=tag;
        }
    }
	
	/**
	 *
	 * @param display
	 */
	public void displayLocal(TriStateCheckBox.State display) {
        this.displayLocal=display;
    }
	
	/**
	 *
	 * @param entry
	 * @return
	 */
	@Override
    public boolean include(Entry entry) {
        Book book = (Book) entry.getValue(2);
        return isToDisplaySelected(book.isSelected())
				&& isToDisplayLocal(book.isLocal())
				&& isToDisplayRating(book.getRating())
				&& isToDisplayTag(book.getTags()); 
    }
    
	private boolean isToDisplayLocal(boolean local) {
        return isToDisplay(local, displayLocal);
    }
	
    private boolean isToDisplaySelected(boolean selected) {
        return isToDisplay(selected, displaySelected);
    }

    private boolean isToDisplay(boolean value, State state) {
        switch(state) {
            case ALL: 
                return true;
            case SELECTED:
                return value;
            case UNSELECTED:
                return !value;
        }
        return true;
    }
 
	private boolean isToDisplayRating(String rating) {
        return this.rating==null ? true : rating.equals(this.rating);
    }
	
	private boolean isToDisplayTag(List<String> tags) {
        return this.tag==null ? true : tags.contains(this.tag);
    }
	
}
