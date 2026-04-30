package jamuz.process.merge;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

class StatSourceGuayadequeTest {
    @Test
    void shouldExposeConfiguredCapabilities() throws Exception {
        StatSourceGuayadeque src = new StatSourceGuayadeque(TestUnitSettings.getTempDbInfo(), "g", "/root/");
        assertNotNull(src);
        assertTrue(src.isUpdateAddedDate());
        assertTrue(src.isUpdateLastPlayed());
        assertFalse(src.isUpdateBPM());
        assertTrue(src.isUpdatePlayCounter());
        assertTrue(src.isUpdateTags());
        assertFalse(src.isUpdateGenre());
    }
}
