package jamuz.soulseek;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlskdDownloadFileTest {

    @Test
    void getKey_combinesSizeAndFilename() {
        SlskdDownloadFile f = new SlskdDownloadFile();
        f.filename = "song.mp3";
        f.size = 123;
        assertEquals("[123]song.mp3", f.getKey());
    }

    @Test
    void getKey_withZeroSize_includesZero() {
        SlskdDownloadFile f = new SlskdDownloadFile();
        f.filename = "a.flac";
        f.size = 0;
        assertEquals("[0]a.flac", f.getKey());
    }

    @Test
    void defaultFieldValues_areNotNull() {
        SlskdDownloadFile f = new SlskdDownloadFile();
        assertNotNull(f.filename);
        assertNotNull(f.state);
        assertNotNull(f.id);
    }
}
