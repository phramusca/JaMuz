package jamuz.soulseek;

import jamuz.utils.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class SlskdSearchResponseTest {

    private SlskdSearchResponse searchResponse;

    @BeforeEach
    void setUp() {
        searchResponse = new SlskdSearchResponse();
    }

    @Test
    void settersAndGetters_workCorrectly() {
        List<SlskdSearchFile> files = new ArrayList<>();
        SlskdSearchFile file1 = new SlskdSearchFile("path1/file1.mp3", 320, 180, 5000000);
        SlskdSearchFile file2 = new SlskdSearchFile("path2/file2.mp3", 128, 200, 3000000);
        files.add(file1);
        files.add(file2);

        SlskdSearchResponse sr = new SlskdSearchResponse();
        sr.setFileCount(2);
        sr.setFiles(files);
        sr.setHasFreeUploadSlot(true);
        sr.setLockedFileCount(1);
        sr.setLockedFiles(new ArrayList<>());
        sr.setQueueLength(5);
        sr.setToken(123);
        sr.setUploadSpeed(1.5);
        sr.setUsername("testUser");
        sr.setDate(DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN));
        sr.setCompleted();
        sr.setSearchText("testSearch");
        sr.setQueued();
        sr.setTableModelDownload(null);

        assertEquals(2, sr.getFileCount());
        assertEquals(files, sr.getFiles());
        assertTrue(sr.hasFreeUploadSlot());
        assertEquals(1, sr.getLockedFileCount());
        assertEquals(5, sr.getQueueLength());
        assertEquals(123, sr.getToken());
        assertEquals(1.5, sr.getUploadSpeed(), 0.0);
        assertEquals("testUser", sr.getUsername());
        assertTrue(sr.isCompleted());
        assertEquals("testSearch", sr.getSearchText());
        assertTrue(sr.isQueued());
        assertNotNull(sr.getProgressBar());
        assertNull(sr.getTableModelDownload());
    }

    @Test
    void filterAndSortFiles_filtersExtensionAndSortsByBitrate() {
        List<SlskdSearchFile> files = new ArrayList<>();
        SlskdSearchFile file1 = new SlskdSearchFile("path2/file2.mp3", 128, 200, 3000000);
        SlskdSearchFile file2 = new SlskdSearchFile("path1/file1.mp3", 320, 180, 5000000);
        files.add(file1);
        files.add(file2);
        searchResponse.setFiles(files);

        searchResponse.filterAndSortFiles(List.of("mp3"));

        List<SlskdSearchFile> sortedFiles = searchResponse.getFiles();
        assertEquals("path1/file1.mp3", sortedFiles.get(0).getFilename());
        assertEquals("path2/file2.mp3", sortedFiles.get(1).getFilename());
    }

    @Test
    void cloneWithoutFiles_copiesMetadataNotFiles() {
        SlskdSearchResponse clone = searchResponse.cloneWithoutFiles();
        assertEquals(0, clone.getFileCount());
        assertTrue(clone.getFiles().isEmpty());
        assertEquals(searchResponse.hasFreeUploadSlot(), clone.hasFreeUploadSlot());
        assertEquals(searchResponse.getQueueLength(), clone.getQueueLength());
        assertEquals(searchResponse.getToken(), clone.getToken());
        assertEquals(searchResponse.getUploadSpeed(), clone.getUploadSpeed(), 0.0);
        assertEquals(searchResponse.getUsername(), clone.getUsername());
        assertEquals(searchResponse.getDate(), clone.getDate());
    }

    @Test
    void getBitrate_returnsAverageAcrossFiles() {
        List<SlskdSearchFile> files = new ArrayList<>();
        files.add(new SlskdSearchFile("path1/file1.mp3", 320, 180, 5000000));
        files.add(new SlskdSearchFile("path2/file2.mp3", 128, 200, 3000000));
        searchResponse.setFiles(files);
        assertEquals(224, searchResponse.getBitrate(), 0.0);
    }

    @Test
    void getSize_returnsTotalAcrossFiles() {
        List<SlskdSearchFile> files = new ArrayList<>();
        files.add(new SlskdSearchFile("path1/file1.mp3", 320, 180, 5000000));
        files.add(new SlskdSearchFile("path2/file2.mp3", 128, 200, 3000000));
        searchResponse.setFiles(files);
        assertEquals(8000000, searchResponse.getSize(), 0.0);
    }

    @Test
    void getPath_returnsDirectoryOfFirstFile() {
        searchResponse.setFiles(List.of(new SlskdSearchFile("path1/file1.mp3", 320, 180, 5000000)));
        assertEquals("path1", searchResponse.getPath());
    }

    @Test
    void getTableModel_createsModelWithCorrectRowCount() {
        searchResponse.setFiles(List.of(new SlskdSearchFile("path1/file1.mp3", 320, 180, 5000000)));
        TableModelSlskdDownload tableModel = searchResponse.getTableModel();
        assertNotNull(tableModel);
        assertEquals(1, tableModel.getRowCount());
        assertEquals("file1.mp3", tableModel.getValueAt(0, 4));
    }

    @Test
    void update_setsProgressBarValue() {
        searchResponse.update("test", 1);
        assertEquals(1, searchResponse.getProgressBar().getValue());
    }
}
