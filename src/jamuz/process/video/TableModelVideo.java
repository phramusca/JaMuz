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
package jamuz.process.video;

import jamuz.gui.swing.TableModelGeneric;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.ProcessAbstract;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */

public class TableModelVideo extends TableModelGeneric {

    private List<VideoAbstract> files;
    private long lengthAll;
    private long lengthSelected;
    private int nbSelected;
    
    /**
	 * Create the table model
	 */
	public TableModelVideo() {
        files = new ArrayList<>();
		columnNames=new String [] {
            "", //NOI18N
            "", //NOI18N
            Inter.get("Label.Informations"), 
            Inter.get("Tag.Year"),   //NOI18N
            Inter.get("Label.Synopsis"), 
            Inter.get("Tag.Size"),
            "Watched",
            "Rating",
            "WatchList",
            "Favorite"
        };
	}

    @Override
    public boolean isCellEditable(int row, int col){
		if(col==0) { //Selected checkbox
            return true;
        }
        else if(col==7 || col==8 || col==9) { //TheMovieDb: Rating, WatchList and Favorite
            VideoAbstract fileInfoVideo = files.get(row);
            if(fileInfoVideo.getMyMovieDb().getId()!=0) {
                return true;
            }
        }
		return false;
    }
    
	/**
	 *
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isCellEnabled(int row, int col) {
        return false;
    }
    
	/**
	 * Return list of files
	 * @return
	 */
	public List<VideoAbstract> getFiles() {
		return files;
	}

    /**
     * get list of files
     * @param index
     * @return
     */
    public VideoAbstract getFile(int index) {
        return this.files.get(index);
    }

    /**
     * Return files' total length
     * @return
     */
    public long getLengthAll() {
        return lengthAll;
    }

    /**
     * Return selected file's length
     * @return
     */
    public long getLengthSelected() {
        return lengthSelected;
    }

    /**
     *
     * @return
     */
    public int getNbSelected() {
        return nbSelected;
    }
    
    @Override
    public int getRowCount() {
        return this.files.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        VideoAbstract fileInfoVideo = files.get(rowIndex);

        switch (columnIndex) {
            case 0: return fileInfoVideo.isSelected();
            case 1: return (fileInfoVideo.getThumbnail(false)!= null ? fileInfoVideo.getThumbnail(false): new ImageIcon());
            case 2: return fileInfoVideo; //need to return object for the filter (.toString() is auto anyway)
            case 3: return fileInfoVideo.getYear();
            case 4: return fileInfoVideo.getSynopsis();
            case 5: return fileInfoVideo.getLength();
            case 6: return fileInfoVideo.isWatched();
            case 7: return fileInfoVideo.getMyMovieDb()==null?-1:fileInfoVideo.getMyMovieDb().getUserRating();
            case 8: return fileInfoVideo.getMyMovieDb()==null?"??":fileInfoVideo.getMyMovieDb().isIsInWatchList();
            case 9: return fileInfoVideo.getMyMovieDb()==null?"??":fileInfoVideo.getMyMovieDb().isIsFavorite();
		}
        return null;
    }
    
    /**
     * Sets given cell value
	 * @param value
	 * @param row
	 * @param col
	 */
    @Override
    public void setValueAt(Object value, int row, int col) {
		VideoAbstract fileInfoVideo = files.get(row);

        switch (col) {
            case 0: 
                select(fileInfoVideo, (boolean)value);
                break;
            case 7:
                fileInfoVideo.setRating((VideoRating) value);
                break;
            case 8:
                if((boolean)value) {
                    fileInfoVideo.addToWatchList();
                }
                else {
                    fileInfoVideo.removeFromWatchList();
                }
                break;
            case 9:
                if((boolean)value) {
                    fileInfoVideo.addFavorite();
                }
                else {
                    fileInfoVideo.removeFavorite();
                }
                break;
		}
    }

	/**
	 *
	 * @param fileInfoVideo
	 * @param selected
	 */
	public void select(VideoAbstract fileInfoVideo, boolean selected) {
        fileInfoVideo.setSelected(selected);
        if(selected) {
            lengthSelected+=fileInfoVideo.getLength().getLength(); //Add 
            nbSelected+=1;
        }
        else {
            lengthSelected-=fileInfoVideo.getLength().getLength(); //Substract
            nbSelected-=1;
        }
        PanelVideo.diplayLength(); //as it has changed, easier than a listener
    }
    
    /**
	* Returns given column's data class
    * @param col
     * @return 
    */
    @Override
    public Class getColumnClass(int col){
        //Note: since all data on a given column are all the same
		//we return data class of given column first row
        return this.getValueAt(0, col).getClass();
    }

	/**
	 * Clears the table
	 */
	public void clear() {
        this.files = new ArrayList<>();
        this.lengthAll=0;
        this.lengthSelected=0;
        this.nbSelected=0;
        //Update table
        this.fireTableDataChanged();
    }
	
	/**
    * Add a row to the table
	 * @param file
    */
    public void addRow(VideoAbstract file){
		this.files.add(file);
        this.lengthAll+=file.getLength().getLength();
		//Update table
		this.fireTableDataChanged();
    }

	/**
	 *
	 * @param file
	 */
	public void removeRow(VideoAbstract file){
		this.files.remove(file);
        this.lengthAll-=file.getLength().getLength();
		//Update table
		this.fireTableDataChanged();
    }
    
    private static LoadIconsThread tLoadIcons;

    /**
     * Load thimbnails
     */
    public void loadThumbnails() {
        //Stop any previously running thread and wait for it to end
		if(tLoadIcons!=null) {
            tLoadIcons.abort();
			try {
				tLoadIcons.join();
			} catch (InterruptedException ex) {
				Popup.error(ex);
			}
		}
		tLoadIcons = new LoadIconsThread("Thread.TableModelVideo.loadThumbnails");
		tLoadIcons.start();
	}
    
    private class LoadIconsThread extends ProcessAbstract {

        public LoadIconsThread(String name) {
            super(name);
        }
		
        @Override
		public void run() {
            try {
                for(VideoAbstract fileInfoVideo : files) {
					tLoadIcons.checkAbort();
					fileInfoVideo.getThumbnail(true);
					//this.fireTableDataChanged();
				}
            }
            catch (InterruptedException ex) {
                Popup.info(Inter.get("Msg.Process.Aborted"));  //NOI18N
            } 
		}
	}
}
