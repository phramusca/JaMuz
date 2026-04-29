package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FileInfoDisplayTest {
    @Test
    void shouldCreateFromFilename() {
        FileInfoDisplay display = new FileInfoDisplay("a.mp3");
        assertNotNull(display);
        assertEquals("a.mp3", display.getFilename());
    }
}
