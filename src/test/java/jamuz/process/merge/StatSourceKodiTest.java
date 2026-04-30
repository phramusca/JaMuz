package jamuz.process.merge;

import java.util.ArrayList;
import jamuz.FileInfo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

class StatSourceKodiTest {

    @Test
    void shouldExposeConfiguredFlagsAndUnsupportedTags() throws Exception {
        StatSourceKodi src = new StatSourceKodi(TestUnitSettings.getTempDbInfo(), "kodi", "/root/");

        assertFalse(src.isUpdateAddedDate());
        assertTrue(src.isUpdateLastPlayed());
        assertFalse(src.isUpdateBPM());
        assertTrue(src.isUpdatePlayCounter());
        assertFalse(src.isUpdateTags());
        assertFalse(src.isUpdateGenre());
        assertThrows(UnsupportedOperationException.class, () -> src.getTags(new ArrayList<>(), (FileInfo) null));
    }
}
