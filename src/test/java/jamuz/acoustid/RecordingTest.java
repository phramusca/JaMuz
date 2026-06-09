package jamuz.acoustid;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecordingTest {

    @Test
    void constructor_storesId() {
        Recording r = new Recording("rec-1");
        assertEquals("rec-1", r.getId());
    }

    @Test
    void constructor_withEmptyId_storesEmptyId() {
        assertEquals("", new Recording("").getId());
    }

    @Test
    void constructor_withNullId_storesNull() {
        assertNull(new Recording(null).getId());
    }
}
