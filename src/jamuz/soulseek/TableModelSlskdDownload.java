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
package jamuz.soulseek;

import jamuz.gui.swing.TableModelGeneric;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class TableModelSlskdDownload extends TableModelGeneric {

    private List<SlskdFile> results;

    /**
	 * Create the table model
	 */
	public TableModelSlskdDownload() {
        this.results = new ArrayList<>();
        this.setColumnNames(new String [] {
            "Date", //NOI18N
			"BitDepth", //NOI18N
			"BitRate", //NOI18N
			"Code", //NOI18N
			"ext", //NOI18N
			"Var. Bitrate", //NOI18N
			"Length", //NOI18N
			"Sample Rate", //NOI18N
			"Locked", //NOI18N
			"State", //NOI18N
			"Size", //NOI18N
			"Speed", //NOI18N
			"Completed", //NOI18N //FIXME !!! Replace with a progressbar
			"User", //NOI18N
            "Path",  //NOI18N
			"More info",  //NOI18N
        });
	}

	/**
	 *
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	@Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SlskdFile searchResponse = results.get(rowIndex);
        switch (columnIndex) {
			case 0: return ""; //searchResponse.startedAt; //FIXME !!! display proper date
            case 1: return searchResponse.bitDepth;
			case 2: return searchResponse.bitRate;
			case 3: return searchResponse.code;
			case 4: return searchResponse.extension;
			case 5: return searchResponse.isVariableBitRate;
            case 6: return searchResponse.length;
			case 7: return searchResponse.sampleRate;
			case 8: return searchResponse.isLocked;
			case 9: return "";//searchResponse.state; //FIXME !!! display state
			case 10: return searchResponse.size;
			case 11: return searchResponse.averageSpeed;
			case 12: return searchResponse.percentComplete;
			case 13: return searchResponse.username;
			case 14: return searchResponse.filename;
			case 15: return searchResponse.getMoreInfo();
		}
        return null;
    }
	
	@Override
    public void setValueAt(Object value, int row, int col) {
		SlskdFile searchResponse = results.get(row);
//        switch (col) {
//			case 3: 
//				searchResponse.setPath((String) value);
//				break;
//		}
		fireTableCellUpdated(row, col);
	}
	
	/**
	 *
	 * @param row
	 * @param col
	 * @return
	 */
	@Override
    public boolean isCellEditable(int row, int col){
		return false;
    }
	
	/**
	 *
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isCellEnabled(int row, int col) {
        return true;
    }
	
	/**
    * Add a row to the table
	 * @param searchResponse
    */
    public void addRow(SlskdFile searchResponse){
		this.results.add(searchResponse);
		this.fireTableDataChanged();
    }
	
	/**
    * Replace a row to the table
	 * @param searchResponse
	 * @param row
    */
    public void replaceRow(SlskdFile searchResponse, int row){
		this.results.set(row, searchResponse);
		this.fireTableDataChanged();
    }

	/**
	 *
	 * @param searchResponse
	 */
	public void removeRow(SlskdFile searchResponse){
		this.results.remove(searchResponse);
		this.fireTableDataChanged();
    }

	/**
	 * Return list of lines
	 * @return
	 */
	public List<SlskdFile> getRows() {
		return results;
	}

    /**
     * get line
     * @param index
     * @return
     */
    public SlskdFile getRow(int index) {
        return this.results.get(index);
    }
   
	/**
	 *
	 * @return
	 */
	@Override
    public int getRowCount() {
        return this.results.size();
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
        this.results = new ArrayList<>();
        this.fireTableDataChanged();
    }
}
