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
package jamuz.gui;

import jamuz.FileInfoInt;
import jamuz.gui.swing.ListElement;
import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ListModelPlayerQueue extends AbstractListModel {

    private final ArrayList<ListElement> queue;
    private int playingIndex;

    /**
     * Create a new queue
     */
    public ListModelPlayerQueue() {
        queue = new ArrayList<>();
        playingIndex = -1;
    }

    /**
     * Add a row to the table
     *
     * @param file
     */
    public void add(ListElement file) {
        queue.add(file);
		this.fireIntervalAdded(this, queue.size()-1, queue.size()-1);
    }

    /**
     * Clear queue
     */
    public void clear() {
        playingIndex = -1;
        if(queue.size()>0) {
            int lastIndex = queue.size() - 1;
            queue.clear();
            this.fireIntervalRemoved(queue, 0, lastIndex);
        }
        enablePreviousAndNext();
    }

    /**
     * Clear but leave nbFilesPlayedToLeave elements in queue
     * @param nbFilesPlayedToLeave
     */
    public void clearButLeave(int nbFilesPlayedToLeave) {
        while (playingIndex > nbFilesPlayedToLeave) {
            queue.remove(0);
            playingIndex--;
        }
    }

	/**
	 *
	 */
	public void clearNotPlayed() {
        while(queue.size()-1>playingIndex) {
            queue.remove(queue.size()-1);
        }
    }
    
    /**
     * Set playing index
     * @param playingIndex
     */
    public void setPlayingIndex(int playingIndex) {
        this.playingIndex = playingIndex;
    }

    /**
     * get playing index
     * @return 
     */
    public int getPlayingIndex() {
        return playingIndex;
    }
    
    /**
     * enables previous and next buttons in GUI
     */
    public void enablePreviousAndNext() {
        PanelMain.enablePreviousAndNext(this.playingIndex > 0 && this.getSize() > 1,
                this.playingIndex >= 0 && this.playingIndex < (this.getSize() - 1) && this.getSize() > 1);
    }

    /**
     * Play next song in list if any or stop player
     */
    public void next() {
        removeBullet();
        PanelMain.fillQueue();
        if (playingIndex < (queue.size() - 1)) {
            playingIndex += 1;
            PanelMain.play(false);
        } else {
			//TODO PLAYER Make the refreshing optional
			boolean refreshQueue=true;
			if(refreshQueue) {
				PanelMain.refreshHiddenQueue(true);
				if (playingIndex < (queue.size() - 1)) {
					playingIndex += 1;
					PanelMain.play(false);
				}
				else {
					 PanelMain.pause();
				}
			} else {
				PanelMain.pause();
			}
        }
    }

    /**
     * Play previous song in list if any
     */
    public void previous() {
        if (playingIndex > 0) {
            removeBullet();
            playingIndex -= 1;
            PanelMain.play(false);
        }
    }

    //TODO: move these 2 functions to ListElement
    //=> Make toString with a param boolean withBullet ?
    /**
     * Add a bullet to list element
     */
    public void addBullet() {
        ListElement element = this.getPlayingSong();
        if (element != null) {
            String display = element.toString().substring(6);
            display = "<html>&bull; " + display; //NOI18N //NOI18N
            element.setDisplay(display);
            this.fireContentsChanged(this, playingIndex, playingIndex);
        }
    }

    /**
     * Remove bullet from list
     */
    public void removeBullet() {
        ListElement element = this.getPlayingSong();
        if (element != null) {
            element.setDisplay(element.toString().replace("&bull; ", "")); //NOI18N
            this.fireContentsChanged(this, playingIndex, playingIndex);
        }
    }

	/**
	 *
	 * @return
	 */
	@Override
    public int getSize() {
        return queue.size();
    }

    /**
     * get playing song
     * @return
     */
    public ListElement getPlayingSong() {
        if (playingIndex >= 0 && playingIndex < this.queue.size()) {
            return this.queue.get(playingIndex);
        }
        return null;
    }

	/**
	 *
	 * @return
	 */
	public ArrayList<ListElement> getQueue() {
        return queue;
    }

	/**
	 *
	 * @param index
	 * @return
	 */
	@Override
    public Object getElementAt(int index) {
        return queue.get(index);
    }
    
	/**
	 *
	 */
	public void refreshPlayingFile() {
        ListElement element = this.queue.get(playingIndex);
        FileInfoInt file = element.getFile();
        element.setDisplay(file.toStringQueue());
        //TODO: Weird way of updating, no ?
        this.fireContentsChanged(this, playingIndex, playingIndex);
    }
    
	/**
	 *
	 * @param fromIndex
	 * @param toIndex
	 * @throws CloneNotSupportedException
	 */
	public void moveRow(int fromIndex, int toIndex) throws CloneNotSupportedException {

		if(fromIndex>=0 && fromIndex<this.queue.size() && toIndex>=0 && toIndex<this.queue.size()) {

            ListElement playingOri = getPlayingSong();
			
			ListElement from = queue.get(fromIndex).clone();
			ListElement to = queue.get(toIndex).clone();
            
			queue.set(fromIndex, to);
			queue.set(toIndex, from);
            
            playingIndex = playingOri==null?-1:queue.indexOf(playingOri);
            
			//Update list
            this.fireContentsChanged(this, fromIndex, toIndex);
		}
	}

}
