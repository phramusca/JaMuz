package jamuz;

import jamuz.IconBuffer.IconVersion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IconBufferTest {

    @Test
    void iconSize_is70() {
        assertEquals(70, IconBuffer.iconSize);
    }

    @Test
    void iconVersion_hasExactlyFourValues() {
        assertEquals(4, IconVersion.values().length);
    }

    @Test
    void iconVersion_hasExpectedConstants() {
        assertEquals(IconVersion.NORMAL_70, IconVersion.valueOf("NORMAL_70"));
        assertEquals(IconVersion.NORMAL_50, IconVersion.valueOf("NORMAL_50"));
        assertEquals(IconVersion.NORMAL_30, IconVersion.valueOf("NORMAL_30"));
        assertEquals(IconVersion.GRAY_30,   IconVersion.valueOf("GRAY_30"));
    }

    @Test
    void getCoverIcon_withMissingFile_returnsNull() {
        assertNull(IconBuffer.getCoverIcon("nonexistent-genre-xyz", "/tmp/"));
    }
}
