/*
 * Copyright (C) 2014 phramusca ( https://github.com/phramusca/JaMuz/ )
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
import javax.swing.RowFilter;
import jamuz.gui.swing.TriStateCheckBox.State;
import jamuz.utils.Inter;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */

//TODO: Include this in TableModelVideo, so we can refresh table (fire) when filter changes
public class TableRowFilterBook extends RowFilter {

    private State displaySelected=State.ALL;
	private State displayLocal=State.ALL;
	private String rating = null;
	private String genre = null;
 
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
	 * @param display
	 */
	public void displayLocal(TriStateCheckBox.State display) {
        this.displayLocal=display;
    }
	
    @Override
    public boolean include(Entry entry) {
        Book book = (Book) entry.getValue(2);
        return isToDisplaySelected(book.isSelected())
				&& isToDisplayLocal(book.isLocal())
				&& isToDisplayRating(book.getRating()); 
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
	
}
