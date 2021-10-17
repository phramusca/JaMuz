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
package jamuz.process.check;

import jamuz.gui.swing.TableModelGeneric;
import jamuz.utils.Inter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */

public class TableModelCheck extends TableModelGeneric {

    private List<FolderInfo> folders;
    
    /**
	 * Create the table model
	 */
	public TableModelCheck() {
        this.folders = new ArrayList<>();
        
        this.setColumnNames(new String [] {
            "", Inter.get("Label.Path") //NOI18N
        });
        
        //Set the editable column
		editableColumns = new Integer[]{0}; //button
        
		this.fireTableStructureChanged();
	}

	/**
	 *
	 * @return
	 */
	@Override
    public int getRowCount() {
        return this.folders.size();
    }

	/**
	 *
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	@Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(folders!=null) { 
            if(rowIndex<folders.size()) {
                FolderInfo folder = folders.get(rowIndex);

                switch (columnIndex) {
                    //"Bouton", "Dossier" 
                    case 0: return folder; //return folder.action.toString();
                    case 1: return folder.toString();
                }
            }
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
	 * Clears the table
	 */
	public void clear() {
        this.folders = new ArrayList<>();
        this.fireTableDataChanged();
    }
	
	/**
    * Add a row to the table
	 * @param folder
    */
    public void addRow(FolderInfo folder){
		this.folders.add(folder);
		this.fireTableDataChanged();
    }

	/**
	 *
	 * @return
	 */
	public List<FolderInfo> getFolders() {
        return folders;
    }
    
    /**
     * remove row from model
     * @param folder
     */
    public void removeRow(FolderInfo folder) {
        this.folders.remove(folder);
        this.fireTableDataChanged();
    }
    
	/**
	 * Move a row from fromIndex to toIndex, keeping specified columns in place
	 * @param fromIndex
	 * @param toIndex
	 * @throws java.lang.CloneNotSupportedException
	 */
	public void moveRow(int fromIndex, int toIndex) throws CloneNotSupportedException {

		if(fromIndex>=0 && fromIndex<this.folders.size() && toIndex>=0 && toIndex<this.folders.size()) {
		
            FolderInfo fromOri = folders.get(fromIndex).clone();
            FolderInfo toOri = folders.get(toIndex).clone();
            
			FolderInfo from = folders.get(fromIndex).clone();
			FolderInfo to = folders.get(toIndex).clone();
            
			folders.set(fromIndex, to);
			folders.set(toIndex, from);

			//Update table
			this.fireTableDataChanged();
		}
	}

}
