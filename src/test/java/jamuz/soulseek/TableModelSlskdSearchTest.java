package jamuz.soulseek;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jamuz.gui.swing.ProgressBar;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TableModelSlskdSearchTest {

    private TableModelSlskdSearch tableModel;
    private SlskdSearchResponse mockResponse;

    @BeforeEach
    void setUp() {
        tableModel = new TableModelSlskdSearch();
        mockResponse = mock(SlskdSearchResponse.class);
    }

    @Test
    void addRow_increasesRowCount() {
        tableModel.addRow(mockResponse);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(mockResponse, tableModel.getRow(0));
    }

    @Test
    void replaceRow_replacesAtIndex() {
        tableModel.addRow(mockResponse);
        SlskdSearchResponse newResponse = mock(SlskdSearchResponse.class);
        tableModel.replaceRow(newResponse, 0);
        assertEquals(newResponse, tableModel.getRow(0));
    }

    @Test
    void removeRow_decreasesRowCount() {
        tableModel.addRow(mockResponse);
        tableModel.removeRow(mockResponse);
        assertEquals(0, tableModel.getRowCount());
    }

    @Test
    void clear_removesAllRows() {
        tableModel.addRow(mockResponse);
        tableModel.clear();
        assertEquals(0, tableModel.getRowCount());
    }

    @Test
    void getValueAt_returnsCorrectCellValues() {
        when(mockResponse.getDate()).thenReturn("2023-10-01");
        when(mockResponse.isQueued()).thenReturn(true);
        when(mockResponse.getSearchText()).thenReturn("test search");
        when(mockResponse.getFiles()).thenReturn(List.of());
        when(mockResponse.getBitrate()).thenReturn(320.0);
        when(mockResponse.getSize()).thenReturn(1024.0);
        when(mockResponse.getUploadSpeed()).thenReturn(100.0);
        when(mockResponse.hasFreeUploadSlot()).thenReturn(true);
        when(mockResponse.getQueueLength()).thenReturn(5);
        when(mockResponse.getUsername()).thenReturn("user");
        when(mockResponse.getPath()).thenReturn("/path/to/file");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setValue(50);
        when(mockResponse.getProgressBar()).thenReturn(progressBar);

        tableModel.addRow(mockResponse);

        assertEquals("2023-10-01", tableModel.getValueAt(0, 0));
        assertEquals(true, tableModel.getValueAt(0, 1));
        assertEquals("test search", tableModel.getValueAt(0, 2));
        assertEquals(0, tableModel.getValueAt(0, 3));
        assertEquals(320.0, tableModel.getValueAt(0, 4));
        assertEquals(1024.0, tableModel.getValueAt(0, 5));
        assertEquals(100.0, tableModel.getValueAt(0, 6));
        assertEquals(true, tableModel.getValueAt(0, 7));
        assertEquals(5, tableModel.getValueAt(0, 8));
        assertEquals("user", tableModel.getValueAt(0, 9));
        assertEquals("/path/to/file", tableModel.getValueAt(0, 10));
        assertEquals(progressBar, tableModel.getValueAt(0, 11));
    }

    @Test
    void isCellEditable_alwaysFalse() {
        assertFalse(tableModel.isCellEditable(0, 0));
    }

    @Test
    void isCellEnabled_alwaysTrue() {
        assertTrue(tableModel.isCellEnabled(0, 0));
    }
}
