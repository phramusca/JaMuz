package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FileInfoDisplayTest {

    @Test
    void constructor_storesFilename() {
        FileInfoDisplay d = new FileInfoDisplay("song.mp3");
        assertEquals("song.mp3", d.getFilename());
    }

    @Test
    void defaultTableValues_areEmpty() {
        FileInfoDisplay d = new FileInfoDisplay("x.mp3");
        assertEquals("", d.artistDisplay.getValue());
        assertEquals("", d.albumDisplay.getValue());
        assertEquals("", d.titleDisplay.getValue());
        assertEquals("", d.genreDisplay.getValue());
    }

    @Test
    void clone_producesEqualInstance() throws CloneNotSupportedException {
        FileInfoDisplay d = new FileInfoDisplay("track.mp3");
        FileInfoDisplay cloned = d.clone();
        assertNotSame(d, cloned);
        assertEquals(d.getFilename(), cloned.getFilename());
    }

    @Test
    void isAudioFile_isFalseByDefault() {
        assertFalse(new FileInfoDisplay("file.mp3").isAudioFile);
    }
}
