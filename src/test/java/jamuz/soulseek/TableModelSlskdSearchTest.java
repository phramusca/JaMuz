package jamuz.soulseek;

import org.junit.Before;
import org.junit.Test;
import jamuz.gui.swing.ProgressBar;

import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;




public class TableModelSlskdSearchTest {

    private TableModelSlskdSearch tableModel;
    private SlskdSearchResponse mockResponse;

    @Before
    public void setUp() {
        tableModel = new TableModelSlskdSearch();
        mockResponse = mock(SlskdSearchResponse.class);
    }

    @Test
    public void testAddRow() {
        tableModel.addRow(mockResponse);
        assertThat(tableModel.getRowCount(), is(1));
        assertThat(tableModel.getRow(0), is(mockResponse));
    }

    @Test
    public void testReplaceRow() {
        tableModel.addRow(mockResponse);
        SlskdSearchResponse newResponse = mock(SlskdSearchResponse.class);
        tableModel.replaceRow(newResponse, 0);
        assertThat(tableModel.getRow(0), is(newResponse));
    }

    @Test
    public void testRemoveRow() {
        tableModel.addRow(mockResponse);
        tableModel.removeRow(mockResponse);
        assertThat(tableModel.getRowCount(), is(0));
    }

    @Test
    public void testClear() {
        tableModel.addRow(mockResponse);
        tableModel.clear();
        assertThat(tableModel.getRowCount(), is(0));
    }

    @Test
    public void testGetValueAt() {
        when(mockResponse.getDate()).thenReturn("2023-10-01");
        when(mockResponse.isQueued()).thenReturn(true);
        when(mockResponse.getSearchText()).thenReturn("test search");
        when(mockResponse.getFiles()).thenReturn(List.of());
        when(mockResponse.getBitrate()).thenReturn(320.0);
        when(mockResponse.getSize()).thenReturn(1024.0);
        when(mockResponse.getUploadSpeed()).thenReturn((double) 100);
        when(mockResponse.hasFreeUploadSlot()).thenReturn(true);
        when(mockResponse.getQueueLength()).thenReturn(5);
        when(mockResponse.getUsername()).thenReturn("user");
        when(mockResponse.getPath()).thenReturn("/path/to/file");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setValue(50);
        when(mockResponse.getProgressBar()).thenReturn(progressBar);

        tableModel.addRow(mockResponse);

        assertThat(tableModel.getValueAt(0, 0), is("2023-10-01"));
        assertThat(tableModel.getValueAt(0, 1), is(true));
        assertThat(tableModel.getValueAt(0, 2), is("test search"));
        assertThat(tableModel.getValueAt(0, 3), is(0));
        assertThat(tableModel.getValueAt(0, 4), is(320.0));
        assertThat(tableModel.getValueAt(0, 5), is(1024.0));
        assertThat(tableModel.getValueAt(0, 6), is((double) 100));
        assertThat(tableModel.getValueAt(0, 7), is(true));
        assertThat(tableModel.getValueAt(0, 8), is(5));
        assertThat(tableModel.getValueAt(0, 9), is("user"));
        assertThat(tableModel.getValueAt(0, 10), is("/path/to/file"));
        assertThat(tableModel.getValueAt(0, 11), is(progressBar));
    }

    @Test
    public void testIsCellEditable() {
        assertThat(tableModel.isCellEditable(0, 0), is(false));
    }

    @Test
    public void testIsCellEnabled() {
        assertThat(tableModel.isCellEnabled(0, 0), is(true));
    }
}