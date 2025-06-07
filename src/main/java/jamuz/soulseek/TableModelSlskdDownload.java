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
import jamuz.utils.StringManager;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class TableModelSlskdDownload extends TableModelGeneric {

    private List<SlskdSearchFile> results;
    private SlskdSearchResponse searchResponse;

    /**
     * Create the table model
     * @param searchResponse
     */
    public TableModelSlskdDownload(SlskdSearchResponse searchResponse) {
        this();
        this.searchResponse = searchResponse;
    }

    TableModelSlskdDownload() {
        this.results = new ArrayList<>();
        this.setColumnNames(new String[]{
            "Date", //NOI18N
            "BitRate", //NOI18N
            "Length", //NOI18N
            "Size", //NOI18N
            "File",  //NOI18N
            "Progress", //NOI18N
        });
    }

    public SlskdSearchResponse getSearchResponse() {
        return searchResponse;
    }

    /**
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SlskdSearchFile searchFile = results.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return searchFile.getDate();
            case 1:
                return searchFile.bitRate;
            case 2:
                return StringManager.humanReadableSeconds(searchFile.length);
            case 3:
                return StringManager.humanReadableByteCount(searchFile.size, true);
            case 4:
                return FilenameUtils.getName(searchFile.filename);
            case 5:
                return searchFile.getProgressBar();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        // Uncomment and implement if needed
		// SlskdSearchFile searchFile = results.get(row);
        // switch (col) {
        //     case 3:
        //         searchFile.setPath((String) value);
        //         break;
        // }
        fireTableCellUpdated(row, col);
    }

    /**
     * @param row
     * @param col
     * @return
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
     * @param row
     * @param col
     * @return
     */
    public boolean isCellEnabled(int row, int col) {
        return true;
    }

    /**
     * Add a row to the table
     * @param searchFile
     */
    public void addRow(SlskdSearchFile searchFile) {
        this.results.add(searchFile);
        this.fireTableDataChanged();
    }

    /**
     * Replace a row in the table
     * @param searchFile
     * @param row
     */
    public void replaceRow(SlskdSearchFile searchFile, int row) {
        this.results.set(row, searchFile);
        this.fireTableDataChanged();
    }

    /**
     * Remove a row from the table
     * @param searchFile
     */
    public void removeRow(SlskdSearchFile searchFile) {
        this.results.remove(searchFile);
        this.fireTableDataChanged();
    }

    /**
     * Return list of rows
     * @return
     */
    public List<SlskdSearchFile> getRows() {
        return results;
    }

    /**
     * Get a row by index
     * @param index
     * @return
     */
    public SlskdSearchFile getRow(int index) {
        return this.results.get(index);
    }

    /**
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
    public Class<?> getColumnClass(int col) {
        // Note: since all data on a given column are all the same
        // we return data class of given column first row
        return this.getValueAt(0, col).getClass();
    }

    /**
     * Clears the table
     */
    public void clear() {
        this.results.clear();
        this.fireTableDataChanged();
    }
}
