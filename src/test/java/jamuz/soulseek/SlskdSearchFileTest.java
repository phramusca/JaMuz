package jamuz.soulseek;

import jamuz.gui.swing.ProgressBar;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SlskdSearchFileTest {

    private SlskdSearchFile searchFile;

    @Before
    public void setUp() {
        searchFile = new SlskdSearchFile();
    }

    @Test
    public void testConstructor() {
        SlskdSearchFile file = new SlskdSearchFile("path/file.mp3", 320, 180, 5000000);
        assertEquals("path/file.mp3", file.getFilename());
        assertEquals(320, file.getBitRate());
        assertEquals(180, file.getLength());
        assertEquals(5000000, file.getSize());
        assertNotNull(file.getProgressBar());
    }

    @Test
    public void testGettersAndSetters() {
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
    public void testGetPath() {
        searchFile.setFilename("path/file.mp3");
        assertEquals("path", searchFile.getPath());
    }

    @Test
    public void testUpdate() {
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
    public void testGetDate() {
        searchFile.requestedAt = "2023-10-10T08:00:00Z";
        assertEquals("10/10/2023 10:00:00", searchFile.getDate());

        searchFile.enqueuedAt = "2023-10-10T09:00:00Z";
        assertEquals("10/10/2023 11:00:00", searchFile.getDate());

        searchFile.startedAt = "2023-10-10T09:30:00Z";
        assertEquals("10/10/2023 11:30:00", searchFile.getDate());

        searchFile.endedAt = "2023-10-10T10:00:00Z";
        assertEquals("10/10/2023 12:00:00", searchFile.getDate());

        searchFile.requestedAt = "null";
        searchFile.enqueuedAt = "null";
        searchFile.startedAt = "null";
        searchFile.endedAt = "null";
        searchFile.searchedAt = "NoMatterWhat";
        assertEquals("NoMatterWhat", searchFile.getDate());
    }

    @Test
    public void testGetKey() {
        searchFile.setFilename("path/file.mp3");
        searchFile.setSize(5000000);
        assertEquals("[5000000]path/file.mp3", searchFile.getKey());
    }

    @Test
    public void testGetDateWithNoDates() {
        assertEquals("null", searchFile.getDate());
    }
}