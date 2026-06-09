package jamuz.soulseek;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jamuz.gui.swing.ProgressBar;
import static org.junit.jupiter.api.Assertions.*;

class TableModelSlskdDownloadTest {

    private TableModelSlskdDownload tableModel;
    private SlskdSearchFile searchFile;

    @BeforeEach
    void setUp() {
        tableModel = new TableModelSlskdDownload();
        searchFile = new SlskdSearchFile();
        searchFile.bitRate = 320;
        searchFile.length = 180;
        searchFile.size = 5000000;
        searchFile.filename = "test.mp3";
    }

    @Test
    void addRow_increasesRowCount() {
        tableModel.addRow(searchFile);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(searchFile, tableModel.getRow(0));
    }

    @Test
    void replaceRow_replacesAtIndex() {
        tableModel.addRow(searchFile);
        SlskdSearchFile newFile = new SlskdSearchFile();
        newFile.bitRate = 128;
        newFile.length = 200;
        newFile.size = 3000000;
        newFile.filename = "new_test.mp3";
        tableModel.replaceRow(newFile, 0);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(newFile, tableModel.getRow(0));
    }

    @Test
    void removeRow_decreasesRowCount() {
        tableModel.addRow(searchFile);
        tableModel.removeRow(searchFile);
        assertEquals(0, tableModel.getRowCount());
    }

    @Test
    void clear_removesAllRows() {
        tableModel.addRow(searchFile);
        tableModel.clear();
        assertEquals(0, tableModel.getRowCount());
    }

    @Test
    void getValueAt_returnsCorrectCellValues() {
        tableModel.addRow(searchFile);
        assertEquals("null", tableModel.getValueAt(0, 0));
        assertEquals(320, tableModel.getValueAt(0, 1));
        assertEquals("03m", tableModel.getValueAt(0, 2));
        assertEquals("5,0 Mo", tableModel.getValueAt(0, 3));
        assertEquals("test.mp3", tableModel.getValueAt(0, 4));
        assertNotNull(tableModel.getValueAt(0, 5));
    }

    @Test
    void isCellEditable_alwaysFalse() {
        assertFalse(tableModel.isCellEditable(0, 0));
    }

    @Test
    void getColumnClass_returnsCorrectTypes() {
        tableModel.addRow(searchFile);
        assertEquals(String.class, tableModel.getColumnClass(0));
        assertEquals(Integer.class, tableModel.getColumnClass(1));
        assertEquals(String.class, tableModel.getColumnClass(2));
        assertEquals(String.class, tableModel.getColumnClass(3));
        assertEquals(String.class, tableModel.getColumnClass(4));
        assertEquals(ProgressBar.class, tableModel.getColumnClass(5));
    }
}
