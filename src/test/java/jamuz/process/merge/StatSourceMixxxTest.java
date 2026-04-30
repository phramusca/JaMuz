package jamuz.process.merge;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

class StatSourceMixxxTest {
    @Test
    void shouldExposeConfiguredCapabilities() throws Exception {
        StatSourceMixxx src = new StatSourceMixxx(TestUnitSettings.getTempDbInfo(), "mixxx", "/root/");
        assertNotNull(src);
        assertTrue(src.isUpdateAddedDate());
        assertFalse(src.isUpdateLastPlayed());
        assertTrue(src.isUpdateBPM());
        assertTrue(src.isUpdatePlayCounter());
        assertTrue(src.isUpdateTags());
        assertFalse(src.isUpdateGenre());
    }
}
