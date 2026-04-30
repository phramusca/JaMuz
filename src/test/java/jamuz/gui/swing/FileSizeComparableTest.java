package jamuz.gui.swing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FileSizeComparableTest {

    @Test
    void shouldCompareByLengthAndRenderReadableText() {
        FileSizeComparable a = new FileSizeComparable(100L);
        FileSizeComparable b = new FileSizeComparable(200L);

        assertTrue(a.compareTo(b) < 0);
        assertEquals(100L, a.getLength());
        assertFalse(a.toString().isBlank());
    }
}
