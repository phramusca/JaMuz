package jamuz.process.merge;

import java.util.ArrayList;
import jamuz.FileInfo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

class StatSourceJaMuzRemoteTest {

    @Test
    void shouldExposeConfiguredFlagsAndNoOpBehavior() throws Exception {
        StatSourceJaMuzRemote src = new StatSourceJaMuzRemote(TestUnitSettings.getTempDbInfo(), "remote", "/root/");

        assertEquals("remote", src.getName());
        assertEquals("/root/", src.getRootPath());
        assertTrue(src.isUpdateAddedDate());
        assertTrue(src.isUpdateLastPlayed());
        assertFalse(src.isUpdateBPM());
        assertTrue(src.isUpdatePlayCounter());
        assertTrue(src.isUpdateTags());
        assertTrue(src.isUpdateGenre());
        assertTrue(src.setUp(false));
        assertTrue(src.getTags(new ArrayList<>(), (FileInfo) null));
    }
}
