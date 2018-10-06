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

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */


public abstract class TableModelGeneric extends AbstractTableModel {
    protected String[] columnNames = {"ToBeDefined"};  //NOI18N

	/**
	 *
	 */
	protected Integer[] editableColumns = { -1 }; //No column is editable by default
    
    /**
	 * Sets the model. This is mandatory to initiate the table !
	 * @param columnNames
	 */
	protected void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
		this.fireTableStructureChanged();
	}
    
//    /**
//	 * Set which columns are editable
//	 * @param editableColumns
//	 */
//	protected void setEditable(Integer[] editableColumns) {
//		this.editableColumns = editableColumns;
//	}
    
    @Override
    public boolean isCellEditable(int row, int col){
		//If column has been setup to be editable, return true
		for(int column:this.editableColumns){
			if(col==column)
				return true;
		}
		//Otherwise return false
		return false;
    }
	
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
	
	@Override
    public String getColumnName(int col) {
        return columnNames[col];
	}
}
