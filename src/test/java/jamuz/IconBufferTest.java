package jamuz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IconBufferTest {
    @Test
    void shouldExposeIconSizeAndEnumValues() {
        assertEquals(70, IconBuffer.iconSize);
        assertTrue(IconBuffer.IconVersion.values().length >= 4);
    }
}
