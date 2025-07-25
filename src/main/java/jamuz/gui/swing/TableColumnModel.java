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
import java.util.List;
import javax.swing.table.*;

/**
 * <code>XTableColumnModel</code> extends the DefaultTableColumnModel .
 * It provides a comfortable way to hide/show columns.
 * Columns keep their positions when hidden and shown again.
 *
 * In order to work with JTable it cannot add any events to <code>TableColumnModelListener</code>.
 * Therefore hiding a column will result in <code>columnRemoved</code> event and showing it
 * again will notify listeners of a <code>columnAdded</code>, and possibly a <code>columnMoved</code> event.
 * For the same reason the following methods still deal with visible columns only:
 * getColumnCount(), getColumns(), getColumnIndex(), getColumn()
 * There are overloaded versions of these methods that take a parameter <code>onlyVisible</code> which let's
 * you specify wether you want invisible columns taken into account.
 *
 * @version 0.9 04/03/01
 * @author Stephen Kelvin, mail@StephenKelvin.de
 * @see DefaultTableColumnModel
 */
public class TableColumnModel extends DefaultTableColumnModel {
    /** Array of TableColumn objects in this model.
     *  Holds all column objects, regardless of their visibility
     */
    protected List allTableColumns = new ArrayList<>();
    
    /**
     * Creates an extended table column model.
     */
    public TableColumnModel() {
    }
    
    /**
     * Sets the visibility of the specified TableColumn.
     * The call is ignored if the TableColumn is not found in this column model
     * or its visibility status did not change.
     * <p>
     *
	 * @param column 
	 * @param visible its new visibility status
 */
    // listeners will receive columnAdded()/columnRemoved() event
    public void setColumnVisible(TableColumn column, boolean visible) {
        if(!visible) {
            super.removeColumn(column);
        }
        else {
            // find the visible index of the column:
            // iterate through both collections of visible and all columns, counting
            // visible columns up to the one that's about to be shown again
            int noVisibleColumns    = tableColumns.size();
            int noInvisibleColumns  = allTableColumns.size();
            int visibleIndex        = 0;
            
            for(int invisibleIndex = 0; invisibleIndex < noInvisibleColumns; ++invisibleIndex) {
                TableColumn visibleColumn   = (visibleIndex < noVisibleColumns ? tableColumns.get(visibleIndex) : null);
                TableColumn testColumn      = (TableColumn)allTableColumns.get(invisibleIndex);
                
                if(testColumn == column) {
                    if(visibleColumn != column) {
                        super.addColumn(column);
                        super.moveColumn(tableColumns.size() - 1, visibleIndex);
                    }
                    return; // ####################
                }
                if(testColumn == visibleColumn) {
                    ++visibleIndex;
                }
            }
        }
    }
    
    /**
     * Makes all columns in this model visible
     */
    public void setAllColumnsVisible() {
        int noColumns       = allTableColumns.size();
        
        for(int columnIndex = 0; columnIndex < noColumns; ++columnIndex) {
            TableColumn visibleColumn = (columnIndex < tableColumns.size() ? tableColumns.get(columnIndex) : null);
            TableColumn invisibleColumn = (TableColumn)allTableColumns.get(columnIndex);
            
            if(visibleColumn != invisibleColumn) {
                super.addColumn(invisibleColumn);
                super.moveColumn(tableColumns.size() - 1, columnIndex);
            }
        }
    }
    
   /**
    * Maps the index of the column in the table model at
    * <code>modelColumnIndex</code> to the TableColumn object.
    * There may me multiple TableColumn objects showing the same model column, though this is uncommon.
    * This method will always return the first visible or else the first invisible column with the specified index.
    * @param modelColumnIndex index of column in table model
    * @return table column object or null if no such column in this column model
 */
    public TableColumn getColumnByModelIndex(int modelColumnIndex) {
        for (int columnIndex = 0; columnIndex < allTableColumns.size(); ++columnIndex) {
            TableColumn column = (TableColumn)allTableColumns.get(columnIndex);
            if(column.getModelIndex() == modelColumnIndex) {
                return column;
            }
        }
        return null;
    }
    
/** Checks wether the specified column is currently visible.
 * @param aColumn column to check
 * @return visibility of specified column (false if there is no such column at all. [It's not visible, right?])
 */    
    public boolean isColumnVisible(TableColumn aColumn) {
        return (tableColumns.indexOf(aColumn) >= 0);
    }
    
/** Append <code>column</code> to the right of exisiting columns.
 * Posts <code>columnAdded</code> event.
 * @param column The column to be added
 * @see #removeColumn
 * @exception IllegalArgumentException if <code>column</code> is <code>null</code>
 */    
	@Override
    public void addColumn(TableColumn column) {
        allTableColumns.add(column);
        super.addColumn(column);
    }
    
/** Removes <code>column</code> from this column model.
 * Posts <code>columnRemoved</code> event.
 * Will do nothing if the column is not in this model.
 * @param column the column to be added
 * @see #addColumn
 */    
	@Override
    public void removeColumn(TableColumn column) {
        int allColumnsIndex = allTableColumns.indexOf(column);
        if(allColumnsIndex != -1) {
            allTableColumns.remove(allColumnsIndex);
        }
        super.removeColumn(column);
    }
    
    /** Moves the column from <code>oldIndex</code> to <code>newIndex</code>.
     * Posts  <code>columnMoved</code> event.
     * Will not move any columns if <code>oldIndex</code> equals <code>newIndex</code>.
     *
     * @param	oldIndex			index of column to be moved
     * @param	newIndex			new index of the column
     * @exception IllegalArgumentException	if either <code>oldIndex</code> or
     * 						<code>newIndex</code>
     *						are not in [0, getColumnCount() - 1]
     */
	@Override
    public void moveColumn(int oldIndex, int newIndex) {
	if ((oldIndex < 0) || (oldIndex >= getColumnCount()) ||
	    (newIndex < 0) || (newIndex >= getColumnCount()))
        {
            throw new IllegalArgumentException("moveColumn() - Index out of range"); //NOI18N
        } 
        
        TableColumn fromColumn  = tableColumns.get(oldIndex);
        TableColumn toColumn    = tableColumns.get(newIndex);
        
        int allColumnsOldIndex  = allTableColumns.indexOf(fromColumn);
        int allColumnsNewIndex  = allTableColumns.indexOf(toColumn);

        if(oldIndex != newIndex) {
            allTableColumns.remove(allColumnsOldIndex);
            allTableColumns.add(allColumnsNewIndex, fromColumn);
        }
        
        super.moveColumn(oldIndex, newIndex);
    }

    /**
     * Returns the total number of columns in this model.
     *
     * @param   onlyVisible   if set only visible columns will be counted
     * @return	the number of columns in the <code>tableColumns</code> array
     * @see	#getColumns
     */
    public int getColumnCount(boolean onlyVisible) {
        List columns = (onlyVisible ? tableColumns : allTableColumns);
	return columns.size();
    }

    /**
     * Returns an <code>Enumeration</code> of all the columns in the model.
     *
     * @param   onlyVisible   if set all invisible columns will be missing from the enumeration.
     * @return an <code>Enumeration</code> of the columns in the model
     */
//    public Enumeration getColumns(boolean onlyVisible) {
//        List columns = (onlyVisible ? tableColumns : allTableColumns);
//        
//	return columns.listIterator();
//    }

    /**
     * Returns the position of the first column whose identifier equals <code>identifier</code>.
     * Position is the the index in all visible columns if <code>onlyVisible</code> is true or
     * else the index in all columns.
     *
     * @param	identifier   the identifier object to search for
     * @param	onlyVisible  if set searches only visible columns
     *
     * @return		the index of the first column whose identifier
     *			equals <code>identifier</code>
     *
     * @exception       IllegalArgumentException  if <code>identifier</code>
     *				is <code>null</code>, or if no
     *				<code>TableColumn</code> has this
     *				<code>identifier</code>
     * @see		#getColumn
     */
    public int getColumnIndex(Object identifier, boolean onlyVisible) {
	if (identifier == null) {
	    throw new IllegalArgumentException("Identifier is null");  //NOI18N
	}

        List      columns     = (onlyVisible ? tableColumns : allTableColumns);
        int         noColumns   = columns.size();
        TableColumn column;
        
        for(int columnIndex = 0; columnIndex < noColumns; ++columnIndex) {
	    column = (TableColumn)columns.get(columnIndex);

            if(identifier.equals(column.getIdentifier())) {
				return columnIndex;
		}
        }
        
	throw new IllegalArgumentException("Identifier not found");  //NOI18N
    }

    /**
     * Returns the <code>TableColumn</code> object for the column
     * at <code>columnIndex</code>.
     *
     * @param	columnIndex	the index of the column desired
     * @param	onlyVisible	if set columnIndex is meant to be relative to all visible columns only
     *                          else it is the index in all columns
     *
     * @return	the <code>TableColumn</code> object for the column
     *				at <code>columnIndex</code>
     */
    public TableColumn getColumn(int columnIndex, boolean onlyVisible) {
		return tableColumns.elementAt(columnIndex);
    }
}