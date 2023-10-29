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
import jamuz.utils.StringManager;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class TableModelSlskdDownload extends TableModelGeneric {

    private List<SlskdSearchFile> results;

    /**
	 * Create the table model
	 */
	public TableModelSlskdDownload() {
        this.results = new ArrayList<>();
        this.setColumnNames(new String [] {
            "Date", //NOI18N
			"BitRate", //NOI18N
			"Length", //NOI18N
			"Size", //NOI18N
            "File",  //NOI18N
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
    SlskdSearchFile searchFile = results.get(rowIndex);
    switch (columnIndex) {
        case 0: return searchFile.getDate();
		case 1: return searchFile.bitRate;
		case 2: return StringManager.humanReadableSeconds(searchFile.length);
		case 3: return StringManager.humanReadableByteCount(searchFile.size, true);
		case 4: return FilenameUtils.getName(searchFile.filename);
        case 5: return searchFile.getProgressBar();
    }
    return null;
}
	
	@Override
    public void setValueAt(Object value, int row, int col) {
		SlskdSearchFile searchResponse = results.get(row);
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
    public void addRow(SlskdSearchFile searchResponse){
		this.results.add(searchResponse);
		this.fireTableDataChanged();
    }
	
	/**
    * Replace a row to the table
	 * @param searchResponse
	 * @param row
    */
    public void replaceRow(SlskdSearchFile searchResponse, int row){
		this.results.set(row, searchResponse);
		this.fireTableDataChanged();
    }

	/**
	 *
	 * @param searchResponse
	 */
	public void removeRow(SlskdSearchFile searchResponse){
		this.results.remove(searchResponse);
		this.fireTableDataChanged();
    }

	/**
	 * Return list of lines
	 * @return
	 */
	public List<SlskdSearchFile> getRows() {
		return results;
	}

    /**
     * get line
     * @param index
     * @return
     */
    public SlskdSearchFile getRow(int index) {
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
