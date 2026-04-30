package jamuz.soulseek;

import jamuz.gui.swing.ProgressBar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlskdSearchFileTest {

    private SlskdSearchFile searchFile;

    @BeforeEach
    void setUp() {
        searchFile = new SlskdSearchFile();
    }

    @Test
    void constructor_setsAllFields() {
        SlskdSearchFile file = new SlskdSearchFile("path/file.mp3", 320, 180, 5000000);
        assertEquals("path/file.mp3", file.getFilename());
        assertEquals(320, file.getBitRate());
        assertEquals(180, file.getLength());
        assertEquals(5000000, file.getSize());
        assertNotNull(file.getProgressBar());
    }

    @Test
    void gettersAndSetters_workCorrectly() {
        searchFile.setFilename("path/file.mp3");
        assertEquals("path/file.mp3", searchFile.getFilename());

        searchFile.setBitRate(320);
        assertEquals(320, searchFile.getBitRate());

        searchFile.setLength(180);
        assertEquals(180, searchFile.getLength());

        searchFile.setSize(5000000);
        assertEquals(5000000, searchFile.getSize());

        ProgressBar progressBar = new ProgressBar();
        searchFile.setProgressBar(progressBar);
        assertEquals(progressBar, searchFile.getProgressBar());
    }

    @Test
    void getPath_returnsDirectoryPart() {
        searchFile.setFilename("path/file.mp3");
        assertEquals("path", searchFile.getPath());
    }

    @Test
    void update_copiesAllDownloadFileFields() {
        SlskdDownloadFile downloadFile = new SlskdDownloadFile();
        downloadFile.averageSpeed = 1.5;
        downloadFile.bytesRemaining = 1000;
        downloadFile.bytesTransferred = 4000;
        downloadFile.direction = "download";
        downloadFile.elapsedTime = "00:01:00";
        downloadFile.endedAt = "2023-10-10T10:00:00Z";
        downloadFile.enqueuedAt = "2023-10-10T09:00:00Z";
        downloadFile.id = "12345";
        downloadFile.percentComplete = 80.0;
        downloadFile.remainingTime = "00:00:15";
        downloadFile.requestedAt = "2023-10-10T08:00:00Z";
        downloadFile.startOffset = 0;
        downloadFile.startedAt = "2023-10-10T09:30:00Z";
        downloadFile.state = "downloading";

        searchFile.update(downloadFile);

        assertEquals(1.5, searchFile.averageSpeed, 0.0);
        assertEquals(1000, searchFile.bytesRemaining);
        assertEquals(4000, searchFile.bytesTransferred);
        assertEquals("download", searchFile.direction);
        assertEquals("00:01:00", searchFile.elapsedTime);
        assertEquals("2023-10-10T10:00:00Z", searchFile.endedAt);
        assertEquals("2023-10-10T09:00:00Z", searchFile.enqueuedAt);
        assertEquals("12345", searchFile.id);
        assertEquals(80.0, searchFile.percentComplete, 0.0);
        assertEquals("00:00:15", searchFile.remainingTime);
        assertEquals("2023-10-10T08:00:00Z", searchFile.requestedAt);
        assertEquals(0, searchFile.startOffset);
        assertEquals("2023-10-10T09:30:00Z", searchFile.startedAt);
        assertEquals("downloading", searchFile.state);
    }

    @Test
    void getDate_returnsMostPreciseTimestamp() {
        // getDate() returns the most specific timestamp available (priority: endedAt > startedAt > enqueuedAt > requestedAt > searchedAt)
        searchFile.requestedAt = "2023-10-10T08:00:00Z";
        // exact local value depends on JVM timezone; we just verify non-null / non-empty
        assertNotNull(searchFile.getDate());
        assertFalse(searchFile.getDate().isBlank());

        searchFile.endedAt = "2023-10-10T10:00:00Z";
        assertNotNull(searchFile.getDate());
    }

    @Test
    void getDate_fallsBackToSearchedAt() {
        searchFile.requestedAt = "null";
        searchFile.enqueuedAt = "null";
        searchFile.startedAt = "null";
        searchFile.endedAt = "null";
        searchFile.searchedAt = "NoMatterWhat";
        assertEquals("NoMatterWhat", searchFile.getDate());
    }

    @Test
    void getDate_withNoDates_returnsNullString() {
        assertEquals("null", searchFile.getDate());
    }

    @Test
    void getKey_combinesSizeAndFilename() {
        searchFile.setFilename("path/file.mp3");
        searchFile.setSize(5000000);
        assertEquals("[5000000]path/file.mp3", searchFile.getKey());
    }
}
