package jamuz.soulseek;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlskdDownloadFileTest {

    @Test
    void shouldBuildKeyFromSizeAndFilename() {
        SlskdDownloadFile f = new SlskdDownloadFile();
        f.filename = "song.mp3";
        f.size = 123;
        assertEquals("[123]song.mp3", f.getKey());
    }
}
