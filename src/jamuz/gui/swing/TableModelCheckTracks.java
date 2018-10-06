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

import jamuz.process.check.FileInfoDisplay;
import java.util.ArrayList;
import java.util.List;
import jamuz.utils.Inter;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */

public class TableModelCheckTracks extends TableModelGeneric {

    private List<FileInfoDisplay> filesAudio;
    
    /**
	 * Create the table model
	 */
	public TableModelCheckTracks() {
        this.filesAudio = new ArrayList<>();
        index=0;
        //TODO: Add extra columns (if from library): rating, addedDate, ...
        
        //Set column names
        this.columnNames = new String [] {
            Inter.get("Label.File"),   //NOI18N
            Inter.get("Tag.DiscNo")+"<br/>(new)</html>",  //NOI18N
            Inter.get("Tag.DiscNo"),   //NOI18N
            Inter.get("Tag.TrackNo")+"<br/>(new)</html>",   //NOI18N
            Inter.get("Tag.TrackNo"),   //NOI18N
            Inter.get("Tag.Artist")+" (new)",   //NOI18N
            Inter.get("Tag.Artist"),   //NOI18N
            Inter.get("Tag.Title")+" (new)",   //NOI18N
            Inter.get("Tag.Title"),   //NOI18N
            Inter.get("Tag.Genre")+" (new)",   //NOI18N
            Inter.get("Tag.Genre"),   //NOI18N
            Inter.get("Tag.Album")+" (new)",   //NOI18N
			Inter.get("Tag.Album"),   //NOI18N
			Inter.get("Tag.Year")+" (new)",   //NOI18N
            Inter.get("Tag.Year"),   //NOI18N
            Inter.get("Tag.BitRate"),   //NOI18N
            Inter.get("Tag.Length"),   //NOI18N
            Inter.get("Tag.Format"),   //NOI18N
            Inter.get("Tag.Size"),   //NOI18N
            Inter.get("Tag.AlbumArtist")+" (new)",   //NOI18N
			Inter.get("Tag.AlbumArtist"),	  //NOI18N
            Inter.get("Tag.Comment")+" (new)",   //NOI18N
			Inter.get("Tag.Comment"),   //NOI18N
            Inter.get("Tag.Cover"),   //NOI18N
            Inter.get("Tag.BPM")+" (new)",   //NOI18N
			Inter.get("Tag.BPM")   //NOI18N
        };
        
        //Set the editable columns
		editableColumns = new Integer[]{1, 3, 5, 7, 9, 11, 13, 19, 21, 24};
	}

	/**
	 * Return list of files
	 * @return
	 */
	public List<FileInfoDisplay> getFiles() {
		return filesAudio;
	}
  
    @Override
    public int getRowCount() {
        return this.filesAudio.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        FileInfoDisplay fileInfoDisplay = filesAudio.get(rowIndex);

        switch (columnIndex) {
            case 0: return fileInfoDisplay.getFilename();
            case 1: return fileInfoDisplay.getDiscNoFull();
            case 2: return fileInfoDisplay.discNoFullDisplay;
            case 3: return fileInfoDisplay.getTrackNoFull();
            case 4: return fileInfoDisplay.trackNoFullDisplay;
			case 5: return fileInfoDisplay.getArtist();
			case 6: return fileInfoDisplay.artistDisplay;
			case 7: return fileInfoDisplay.getTitle();
			case 8: return fileInfoDisplay.titleDisplay;
			case 9: return fileInfoDisplay.getGenre();
			case 10: return fileInfoDisplay.genreDisplay;
			case 11: return fileInfoDisplay.getAlbum();
			case 12: return fileInfoDisplay.albumDisplay;
			case 13: return fileInfoDisplay.getYear();
			case 14: return fileInfoDisplay.yearDisplay;
			case 15: return fileInfoDisplay.bitRateDisplay;
			case 16: return fileInfoDisplay.getLengthDisplay();
			case 17: return fileInfoDisplay.formatDisplay;
			case 18: return fileInfoDisplay.getSizeDisplay();
			case 19: return fileInfoDisplay.getAlbumArtist();
			case 20: return fileInfoDisplay.albumArtistDisplay;
			case 21: return fileInfoDisplay.getComment();
			case 22: return fileInfoDisplay.commentDisplay;
			case 23: return fileInfoDisplay.coverIconDisplay;
			case 24: return fileInfoDisplay.getBPM();
			case 25: return fileInfoDisplay.BPMDisplay;
		}
        return null;
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
	 *
	 * @param value
	 * @param col
	 */
	public void setValueAt(Object value, int col) {
        for(int i=0; i < getRowCount(); i++) {
            setValueAt(value, i, col);
            fireTableDataChanged();
        }
    }
    
	/**
     * Sets given cell value
	 * @param value
	 * @param row
	 * @param col
	 */
    @Override
    public void setValueAt(Object value, int row, int col) {
		FileInfoDisplay fileInfoDisplay = filesAudio.get(row);

        switch (col) {
            case 1: 
				fileInfoDisplay.setDiscNoFull((String) value);
				break;
            case 3: 
				fileInfoDisplay.setTrackNoFull((String) value);
				break;
			case 5: 
				fileInfoDisplay.setArtist((String) value);
				break;
			case 7: 
				fileInfoDisplay.setTitle((String) value);
				break;
			case 9: 
				fileInfoDisplay.setGenre((String) value);
				break;
			case 11: 
				fileInfoDisplay.setAlbum((String) value);
				break;
			case 13: 
				fileInfoDisplay.setYear((String) value);
				break;
			case 19:
				fileInfoDisplay.setAlbumArtist((String) value);
				break;
			case 21:
				fileInfoDisplay.setComment((String) value);
				break;
			case 24:
				fileInfoDisplay.setBPM((float) value);
				break;
		}
    }
	
	/**
	 * Clears the table
	 */
	public void clear() {
		index=0;
        this.filesAudio = new ArrayList<>();
        //Update table
        this.fireTableDataChanged();
    }
	private int index;
	/**
    * Add a row to the table
	 * @param file
    */
    public void addRow(FileInfoDisplay file){
		file.index=index++;
		this.filesAudio.add(file);
		//Update table
		this.fireTableDataChanged();
    }
	
	/**
	 * Move a row from fromIndex to toIndex, keeping specified columns in place
	 * @param fromIndex
	 * @param toIndex
	 * @throws java.lang.CloneNotSupportedException
	 */
	public void moveRow(int fromIndex, int toIndex) 
			throws CloneNotSupportedException {

		if(fromIndex>=0 
				&& fromIndex<this.filesAudio.size() 
				&& toIndex>=0 && toIndex<this.filesAudio.size()) {
		
            FileInfoDisplay fromOri = filesAudio.get(fromIndex).clone();
            FileInfoDisplay toOri = filesAudio.get(toIndex).clone();
            
			FileInfoDisplay from = filesAudio.get(fromIndex).clone();
			FileInfoDisplay to = filesAudio.get(toIndex).clone();
			
            if(toOri.isAudioFile && fromOri.isAudioFile) {
                //those new values (from ReleaseMatch Track) do not move
                //only if all are audio files (not N/A entry)
                from.setArtist(toOri.getArtist());
                from.setGenre(toOri.getGenre());
                from.setYear(toOri.getYear());

                to.setArtist(fromOri.getArtist());
                to.setGenre(fromOri.getGenre());
                to.setYear(fromOri.getYear());
            }
            
            //those new values (from ReleaseMatch Track) do not move
            from.setTitle(toOri.getTitle());
			from.setDiscNo(toOri.getDiscNo());
			from.setDiscTotal(toOri.getDiscTotal());
			from.setTrackNo(toOri.getTrackNo());
			from.setTrackTotal(toOri.getTrackTotal());
			
            to.setTitle(fromOri.getTitle());
			to.setDiscNo(fromOri.getDiscNo());
			to.setDiscTotal(fromOri.getDiscTotal());
			to.setTrackNo(fromOri.getTrackNo());
			to.setTrackTotal(fromOri.getTrackTotal());

			filesAudio.set(fromIndex, to);
			filesAudio.set(toIndex, from);
			//TODO: Do this a documented:
			//https://docs.oracle.com/javase/8/docs/api/java/util/List.html#set-int-E-
			//which should be :
//			FileInfoDisplay fileFrom = files.set(toIndex, files.get(fromIndex));
//			files.set(fromIndex, fileFrom);

			//Update table
			this.fireTableDataChanged();
		}
	}

}
