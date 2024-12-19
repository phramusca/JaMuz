package jamuz.soulseek;

import org.junit.Before;
import org.junit.Test;

import jamuz.gui.swing.ProgressBar;

import static org.junit.Assert.*;

public class TableModelSlskdDownloadTest {

    private TableModelSlskdDownload tableModel;
    private SlskdSearchFile searchFile;

    @Before
    public void setUp() {
        tableModel = new TableModelSlskdDownload();
        searchFile = new SlskdSearchFile();
        searchFile.bitRate = 320;
        searchFile.length = 180;
        searchFile.size = 5000000;
        searchFile.filename = "test.mp3";
    }

    @Test
    public void testAddRow() {
        tableModel.addRow(searchFile);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(searchFile, tableModel.getRow(0));
    }

    @Test
    public void testReplaceRow() {
        tableModel.addRow(searchFile);
        SlskdSearchFile newSearchFile = new SlskdSearchFile();
        newSearchFile.bitRate = 128;
        newSearchFile.length = 200;
        newSearchFile.size = 3000000;
        newSearchFile.filename = "new_test.mp3";
        tableModel.replaceRow(newSearchFile, 0);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(newSearchFile, tableModel.getRow(0));
    }

    @Test
    public void testRemoveRow() {
        tableModel.addRow(searchFile);
        tableModel.removeRow(searchFile);
        assertEquals(0, tableModel.getRowCount());
    }

    @Test
    public void testClear() {
        tableModel.addRow(searchFile);
        tableModel.clear();
        assertEquals(0, tableModel.getRowCount());
    }

    @Test
    public void testGetValueAt() {
        tableModel.addRow(searchFile);
        assertEquals("null", tableModel.getValueAt(0, 0));
        assertEquals(320, tableModel.getValueAt(0, 1));
        assertEquals("03m", tableModel.getValueAt(0, 2));
        assertEquals("5,0 Mo", tableModel.getValueAt(0, 3));
        assertEquals("test.mp3", tableModel.getValueAt(0, 4));
        assertNotNull(tableModel.getValueAt(0, 5));
    }

    @Test
    public void testIsCellEditable() {
        assertFalse(tableModel.isCellEditable(0, 0));
    }

    @Test
    public void testGetColumnClass() {
        tableModel.addRow(searchFile);
        assertEquals(String.class, tableModel.getColumnClass(0));
        assertEquals(Integer.class, tableModel.getColumnClass(1));
        assertEquals(String.class, tableModel.getColumnClass(2));
        assertEquals(String.class, tableModel.getColumnClass(3));
        assertEquals(String.class, tableModel.getColumnClass(4));
        assertEquals(ProgressBar.class, tableModel.getColumnClass(5));
    }
}
