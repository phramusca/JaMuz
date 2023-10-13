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
public class TableModelSlskdSearch extends TableModelGeneric {

    private List<SlskdSearchResponse> results;

    /**
	 * Create the table model
	 */
	public TableModelSlskdSearch() {
        this.results = new ArrayList<>();
        this.setColumnNames(new String [] {
            "Date", //NOI18N
			"Nb", //NOI18N
			"BitRate", //NOI18N
			"Size", //NOI18N
			"Speed", //NOI18N
			"Free upload spots", //NOI18N
			"Queue length", //NOI18N
			"User", //NOI18N
            "Path",  //NOI18N
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
        SlskdSearchResponse searchResponse = results.get(rowIndex);
        switch (columnIndex) {
			case 0: return searchResponse.getDate();
            case 1: return searchResponse.fileCount;
			case 2: return searchResponse.getBitrate();
			case 3: return searchResponse.getSize();
			case 4: return searchResponse.getSpeed();
			case 5: return searchResponse.hasFreeUploadSlot;
			case 6: return searchResponse.queueLength;
			case 7: return searchResponse.username;
            case 8: return searchResponse.getPath();
		}
        return null;
    }
	
	@Override
    public void setValueAt(Object value, int row, int col) {
		SlskdSearchResponse searchResponse = results.get(row);
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
    public void addRow(SlskdSearchResponse searchResponse){
		this.results.add(searchResponse);
		this.fireTableDataChanged();
    }
	
	/**
    * Replace a row to the table
	 * @param searchResponse
	 * @param row
    */
    public void replaceRow(SlskdSearchResponse searchResponse, int row){
		this.results.set(row, searchResponse);
		this.fireTableDataChanged();
    }

	/**
	 *
	 * @param searchResponse
	 */
	public void removeRow(SlskdSearchResponse searchResponse){
		this.results.remove(searchResponse);
		this.fireTableDataChanged();
    }

	/**
	 * Return list of lines
	 * @return
	 */
	public List<SlskdSearchResponse> getRows() {
		return results;
	}

    /**
     * get line
     * @param index
     * @return
     */
    public SlskdSearchResponse getRow(int index) {
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
