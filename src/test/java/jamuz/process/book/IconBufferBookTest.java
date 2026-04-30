package jamuz.process.book;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IconBufferBookTest {

    @Test
    void shouldExposeDimensionsAndReturnNullWhenNotFoundWithoutRead() {
        assertEquals(140, IconBufferBook.ICON_HEIGHT);
        assertEquals(105, IconBufferBook.ICON_WIDTH);
        assertNull(IconBufferBook.getCoverIcon("not-found", "/tmp/missing.jpg", false));
    }
}
