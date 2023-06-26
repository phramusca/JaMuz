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
public class TableModelSoulseek extends TableModelGeneric {

    private List<SoulseekResult> results;

    /**
	 * Create the table model
	 */
	public TableModelSoulseek() {
        this.results = new ArrayList<>();
        this.setColumnNames(new String [] {
            "Date", //NOI18N
			"Down.", //NOI18N
            "Tot.", //NOI18N
			"Status", //NOI18N
			"BitRate", //NOI18N
			"Size", //NOI18N
			"Speed", //NOI18N
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
        SoulseekResult soulseekDownload = results.get(rowIndex);
        switch (columnIndex) {
			case 0: return soulseekDownload.getDate();
			case 1: return soulseekDownload.getNbDownloaded();
            case 2: return soulseekDownload.getNbOfFiles();
            case 3: return soulseekDownload.getStatus().name();
			case 4: return soulseekDownload.getBitrate();
			case 5: return soulseekDownload.getSize();
			case 6: return soulseekDownload.getSpeed();
			case 7: return soulseekDownload.getUsername();
            case 8: return soulseekDownload.getPath();
		}
        return null;
    }
	
	@Override
    public void setValueAt(Object value, int row, int col) {
		SoulseekResult result = results.get(row);
//        switch (col) {
//			case 3: 
//				result.setPath((String) value);
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
	 * @param result
    */
    public void addRow(SoulseekResult result){
		this.results.add(result);
		this.fireTableDataChanged();
    }
	
	/**
    * Replace a row to the table
	 * @param result
	 * @param row
    */
    public void replaceRow(SoulseekResult result, int row){
		this.results.set(row, result);
		this.fireTableDataChanged();
    }

	/**
	 *
	 * @param file
	 */
	public void removeRow(SoulseekResult file){
		this.results.remove(file);
		this.fireTableDataChanged();
    }

	/**
	 * Return list of lines
	 * @return
	 */
	public List<SoulseekResult> getRows() {
		return results;
	}

    /**
     * get line
     * @param index
     * @return
     */
    public SoulseekResult getRow(int index) {
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
