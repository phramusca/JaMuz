package jamuz.soulseek;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlskdDownloadDirectoryTest {

    @Test
    void fields_canBeSetAndRead() {
        SlskdDownloadFile file = new SlskdDownloadFile();
        file.filename = "a.mp3";

        SlskdDownloadDirectory dir = new SlskdDownloadDirectory();
        dir.directory = "/music";
        dir.fileCount = 1;
        dir.files = List.of(file);

        assertEquals("/music", dir.directory);
        assertEquals(1, dir.fileCount);
        assertEquals("a.mp3", dir.files.get(0).filename);
    }

    @Test
    void fileCount_defaultIsZero() {
        SlskdDownloadDirectory dir = new SlskdDownloadDirectory();
        assertEquals(0, dir.fileCount);
    }
}
