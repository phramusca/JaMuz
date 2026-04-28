package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FileInfoDuplicateReplaceTest {

    @Test
    void shouldInitializeAndCloneDisplayState() throws Exception {
        FileInfoDisplay src = new FileInfoDisplay("song.mp3");
        FileInfoDuplicateReplace rep = new FileInfoDuplicateReplace(src);

        assertEquals("song.mp3", rep.filenameDisplay.getValue());
        assertNotNull(rep.clone());

        FileInfoDuplicateReplace other = new FileInfoDuplicateReplace(src);
        other.artistDisplay.setDisplay("x");
        rep.setFileInfoDuplicate(other);
        assertEquals(other.artistDisplay.getValue(), rep.artistDisplay.getValue());
    }
}
