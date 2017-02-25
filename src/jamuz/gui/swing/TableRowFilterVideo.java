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

package jamuz.gui.swing;

import jamuz.process.video.VideoAbstract;
import java.util.List;
import javax.swing.RowFilter;
import jamuz.gui.swing.TriStateCheckBox.State;
import jamuz.utils.Inter;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */

//TODO: Include this in TableModelVideo, so we can refresh table (fire) when filter changes
public class TableRowFilterVideo extends RowFilter {

    private State displaySelected=State.ALL;
    private State displayWatched=State.ALL;
    private State displayWatchList=State.ALL;
    private State displayFavorite=State.ALL;
    private State displayRated=State.ALL;
    private State displayLocal=State.ALL;
    private State displayMovies=State.ALL;
    private State displayHD=State.ALL;
    private String genre = null;
    private String mppaRating = null;
    private String rating = null;
    
    /**
     * What to display (all, selected or not) in video list
     */
//    public enum DisplaySelected {
//
//        /**
//         * All
//         */
//        ALL,
//
//        /**
//         * only selected
//         */
//        SELECTED,
//
//        /**
//         * only unSelected
//         */
//        UNSELECTED;
//    }
    
    /**
     * What to display (all, WATCHED or not) in video list
     */
    public enum DisplayWatched {

        /**
         * All
         */
        ALL,

        /**
         * only WATCHED
         */
        WATCHED,

        /**
         * only unWATCHED
         */
        UNWATCHED;
    }
    
    /**
     * Change what to be displayed
     * @param display
     */
    public void displaySelected(TriStateCheckBox.State display) {
        this.displaySelected=display;
    }
    
	/**
	 *
	 * @param display
	 */
	public void displayWatched(State display) {
        this.displayWatched=display;
    }
    
	/**
	 *
	 * @param display
	 */
	public void displayWatchList(TriStateCheckBox.State display) {
        this.displayWatchList=display;
    }
    
	/**
	 *
	 * @param display
	 */
	public void displayFavorite(TriStateCheckBox.State display) {
        this.displayFavorite=display;
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
	 * @param display
	 */
	public void displayMovies(TriStateCheckBox.State display) {
        this.displayMovies=display;
    }
    
	/**
	 *
	 * @param display
	 */
	public void displayHD(TriStateCheckBox.State display) {
        this.displayHD=display;
    }
    
	/**
	 *
	 * @param display
	 */
	public void displayRated(TriStateCheckBox.State display) {
        displayRated=display;
    }
    
	/**
	 *
	 * @param mppaRating
	 */
	public void displayByMppaRating(String mppaRating) {
        if(mppaRating.equals(Inter.get("Label.All"))) {
            this.mppaRating=null;
        }
        else {
            this.mppaRating=mppaRating;
        }
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
	 * @param genre
	 */
	public void displayByGenre(String genre) {
        if(genre.equals(Inter.get("Label.All"))) {
            this.genre=null;
        }
        else {
            this.genre=genre;
        }
    }
    
    @Override
    public boolean include(Entry entry) {
        VideoAbstract videoFile = (VideoAbstract) entry.getValue(2);
        
        return isToDisplaySelected(videoFile.isSelected()) 
                && isToDisplayGenre(videoFile.getGenres())
                && isToDisplayMppaRating(videoFile.getMppaRating())
                && isToDisplayRating(videoFile.getRating())
                && isToDisplayWatched(videoFile.isWatched())
                && isToDisplayWatchList(videoFile.getMyMovieDb().isIsInWatchList())
                && isToDisplayFavorite(videoFile.getMyMovieDb().isIsFavorite())
                && isToDisplayLocal(videoFile.isLocal())
                && isToDisplayMovies(videoFile.isMovie())
                && isToDisplayHD(videoFile.isHD())
                && isToDisplayRated(videoFile.getMyMovieDb().getUserRating().getRating()>0);
    }
    
    private boolean isToDisplaySelected(boolean selected) {
        return isToDisplay(selected, displaySelected);
    }
    
    private boolean isToDisplayWatched(boolean watched) {
        return isToDisplay(watched, displayWatched);
    }
    
    private boolean isToDisplayWatchList(boolean watchList) {
        return isToDisplay(watchList, displayWatchList);
    }
    
    private boolean isToDisplayFavorite(boolean favorite) {
        return isToDisplay(favorite, displayFavorite);
    }
    
    private boolean isToDisplayLocal(boolean local) {
        return isToDisplay(local, displayLocal);
    }
    
    private boolean isToDisplayMovies(boolean local) {
        return isToDisplay(local, displayMovies);
    }
    
    private boolean isToDisplayHD(boolean HD) {
        return isToDisplay(HD, displayHD);
    }
    
    private boolean isToDisplayRated(boolean rated) {
        return isToDisplay(rated, displayRated);
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
    
    private boolean isToDisplayGenre(List<String> genres) {
        return this.genre==null ? true : genres.contains(this.genre);
    }
    
    private boolean isToDisplayMppaRating(String mppaRating) {
        return this.mppaRating==null ? true : mppaRating.equals(this.mppaRating);
    }
    
    private boolean isToDisplayRating(String rating) {
        return this.rating==null ? true : rating.equals(this.rating);
    }
    
}
