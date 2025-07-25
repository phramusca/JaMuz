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
package jamuz.soulseek;

import jamuz.gui.swing.TableModelGeneric;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class TableModelSlskdSearch extends TableModelGeneric {

    private List<SlskdSearchResponse> results;

    /**
	 * Create the table model
	 */
	public TableModelSlskdSearch() {
        this.results = new CopyOnWriteArrayList<>();
        this.setColumnNames(new String [] {
            "Date", //NOI18N
            "Queued", //NOI18N
            "Search", //NOI18N
			"Nb", //NOI18N
			"BitRate", //NOI18N
			"Size", //NOI18N
			"Speed", //NOI18N
			"Free upload spots", //NOI18N
			"Queue length", //NOI18N
			"User", //NOI18N
            "Path",  //NOI18N
            "Progress", //NOI18N
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
            case 1: return searchResponse.isQueued();
            case 2: return searchResponse.getSearchText();
            case 3: return searchResponse.getFiles().size();
			case 4: return searchResponse.getBitrate();
			case 5: return searchResponse.getSize();
			case 6: return searchResponse.getUploadSpeed();
			case 7: return searchResponse.hasFreeUploadSlot();
			case 8: return searchResponse.getQueueLength();
			case 9: return searchResponse.getUsername();
            case 10: return searchResponse.getPath();
            case 11: return searchResponse.getProgressBar();
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
        this.results = new CopyOnWriteArrayList<>();
        this.fireTableDataChanged();
    }
}
