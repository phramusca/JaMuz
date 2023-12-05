/*
 * Copyright (C) 2011 phramusca <phramusca@gmail.com>
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * a TableModel extension
 * @author phramusca <phramusca@gmail.com>
 */
public class TableModel extends AbstractTableModel {
	private String[] columnNames = {"ToBeDefined"};  //NOI18N
    private Object[][] data = {	{"ToBeDefined"}	};  //NOI18N
	private Integer[] editableColumns = { -1 }; //No column is editable by default

	/**
	 * Sets the model. This is mandatory to initiate the table !
	 * @param columnNames
	 * @param data
	 */
	public void setModel(String[] columnNames, Object[][] data) {
		this.data = data;
		this.columnNames = columnNames;
		this.fireTableStructureChanged();
	}
	
	/**
	 * Set which columns are editable
	 * @param editableColumns
	 */
	public void setEditable(Integer[] editableColumns) {
		this.editableColumns = editableColumns;
	}
	
    /**
    * Add a row to the table
    * @param data
    */
    public void addRow(Object[] data){
		int id = 0, nbRow = this.getRowCount(), nbCol = this.getColumnCount();

		Object temp[][] = this.data;
		this.data = new Object[nbRow+1][nbCol];

		for(Object[] value : temp) {
			this.data[id++] = value;
		}

		this.data[id] = data;

		//Update table
		this.fireTableDataChanged();
    }

	/**
	 * Clears the table
	 */
	public void clear() {
        this.data = new Object[0][0];
        this.fireTableDataChanged();
    }

    private <T> List<List> twoDArrayToList(T[][] twoDArray) {
        List<List> list = new ArrayList<>();
        for (T[] array : twoDArray) {
            List<T> list2 = new ArrayList<>();
            list2.addAll(Arrays.asList(array));
            list.add(list2);
        }
        return list;
    }
    
    /**
     * Get list of table data
     * @param columnIndex
     * @return
     */
    public List getList(int columnIndex) {
        List<String> returnList = new ArrayList<>();
        
        List<List> rows = twoDArrayToList(data);
        for(List columns : rows) {
            returnList.add((String) columns.get(columnIndex));
        }
        return returnList;
    }
    
	/**
     * Remove a row from the table
     * @param position
     */
    public void removeRow(int position){

		int id = 0, id2 = 0, nbRow = this.getRowCount()-1, nbCol = this.getColumnCount();
		Object temp[][] = new Object[nbRow][nbCol];

		for(Object[] value : this.data){
			if(id != position){
					temp[id2++] = value;
			}
			id++;
		}
		this.data = temp;
		//Update table
		this.fireTableDataChanged();
    }
	
	/**
	 * Move a row from fromIndex to toIndex, keeping specified columns in place
	 * @param fromIndex
	 * @param toIndex
	 * @param fixedColumns
	 */
	public void moveRow(int fromIndex, int toIndex, int[] fixedColumns) {
		
		if(fromIndex>=0 && fromIndex<this.data.length && toIndex>=0 && toIndex<this.data.length) {
			Object temp[][] = this.data.clone();
		
			Object oFrom[] = this.data[fromIndex].clone();
			Object oTo[] = this.data[toIndex].clone();
			
			for(int i : fixedColumns) {
				oFrom[i] = this.data[toIndex][i];
				oTo[i] = this.data[fromIndex][i];
			}
			
			temp[fromIndex] = oTo;
			temp[toIndex] = oFrom;

			this.data = temp;
			this.fireTableDataChanged();
		}
	}
	
	/**
	 *
	 * @return
	 */
	@Override
    public int getRowCount() {
        return data.length;
    }

	/**
	 *
	 * @return
	 */
	@Override
    public int getColumnCount() {
        return columnNames.length;
    }

	/**
	 *
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	@Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

	/**
	 *
	 * @param col
	 * @return
	 */
	@Override
    public String getColumnName(int col) {
        return columnNames[col];
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
        if(this.data.length>0 && this.data[0].length>0 && this.data[0].length>col && this.data[0][col] != null) {
            return this.data[0][col].getClass();
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
		this.data[row][col] = value;
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
		//If column has been setup to be editable, return true
		for(int column:this.editableColumns){
			if(col==column) {
				return true;
			}
		}
		//Otherwise return false
		return false;
    }
}