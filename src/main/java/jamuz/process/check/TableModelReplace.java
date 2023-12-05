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
package jamuz.process.check;

import jamuz.gui.PanelMain;
import jamuz.gui.swing.TableModelGeneric;
import jamuz.utils.Inter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */

public class TableModelReplace extends TableModelGeneric {

    private List<FileInfoDuplicateReplace> files;
    
    /**
	 * Create the table model
	 */
	public TableModelReplace() {
        files = new ArrayList<>();
        index=0;
		columnNames = new String [] {
            Inter.get("Label.File")+" (new)",   //NOI18N
			Inter.get("Label.File"),   //NOI18N
            Inter.get("Tag.DiscNo")+"<br/>(new)</html>",  //NOI18N
            Inter.get("Tag.DiscNo"),   //NOI18N
            Inter.get("Tag.TrackNo")+"<br/>(new)</html>",   //NOI18N
            Inter.get("Tag.TrackNo"),   //NOI18N
            Inter.get("Tag.Artist")+" (new)",   //NOI18N
            Inter.get("Tag.Artist"),   //NOI18N
            Inter.get("Tag.Title")+" (new)",   //NOI18N
            Inter.get("Tag.Title"),
			Inter.get("Stat.Rating")
        };
	}

	/**
	 * Return list of files
	 * @return
	 */
	public List<FileInfoDuplicateReplace> getFiles() {
		return files;
	}
  
	/**
	 *
	 * @return
	 */
	@Override
    public int getRowCount() {
        return files.size();
    }

	/**
	 *
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	@Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        FileInfoDuplicateReplace fileInfoDisplay = files.get(rowIndex);

        switch (columnIndex) {
            case 0: return fileInfoDisplay.getFilename();
			case 1: return fileInfoDisplay.filenameDisplay;
            case 2: return fileInfoDisplay.getDiscNoFull();
            case 3: return fileInfoDisplay.discNoFullDisplay;
            case 4: return fileInfoDisplay.getTrackNoFull();
            case 5: return fileInfoDisplay.trackNoFullDisplay;
			case 6: return fileInfoDisplay.getArtist();
			case 7: return fileInfoDisplay.artistDisplay;
			case 8: return fileInfoDisplay.getTitle();
			case 9: return fileInfoDisplay.titleDisplay;
			case 10:
				return fileInfoDisplay.getRating()>=0
						?PanelMain.getRatingIcon(fileInfoDisplay.getRating())
						:fileInfoDisplay.getRating();
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
	 * Clears the table
	 */
	public void clear() {
		index=0;
        this.files = new ArrayList<>();
        //Update table
        this.fireTableDataChanged();
    }
	private int index;
	
	/**
    * Add a row to the table
	 * @param file
    */
    public void addRow(FileInfoDuplicateReplace file){
		file.index=index++;
		this.files.add(file);
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
				&& fromIndex<this.files.size() 
				&& toIndex>=0 && toIndex<this.files.size()) {
		
            FileInfoDuplicateReplace fromOri = files.get(fromIndex).clone();
            FileInfoDuplicateReplace toOri = files.get(toIndex).clone();
            
			FileInfoDuplicateReplace from = files.get(fromIndex).clone();
			FileInfoDuplicateReplace to = files.get(toIndex).clone();
			
            if(toOri.isAudioFile && fromOri.isAudioFile) {
                //those new values (from ReleaseMatch Track) do not move
                //only if all are audio files (not N/A entry)
				
            }
            
            //those new values (from duplicate) do not move
			from.setFileInfoDuplicate(toOri);
			to.setFileInfoDuplicate(fromOri);

			files.set(fromIndex, to);
			files.set(toIndex, from);

			//Update table
			this.fireTableDataChanged();
		}
	}

}
