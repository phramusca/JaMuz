package jamuz.soulseek;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the static pure-logic methods of {@link Slsk} without requiring a live slskd server.
 */
class SlskTest {

    @Test
    void getDirectory_extractsParentAndFilenameFromBackslashPath() {
        Pair<String, String> result = Slsk.getDirectory("user\\Music\\Album\\song.mp3");
        assertEquals("Album", result.getLeft());
        assertEquals("song.mp3", result.getRight());
    }

    @Test
    void getDirectory_worksWithDeepPath() {
        Pair<String, String> result = Slsk.getDirectory("share\\a\\b\\c\\track.flac");
        assertEquals("c", result.getLeft());
        assertEquals("track.flac", result.getRight());
    }

    @Test
    void getDirectory_worksWithTwoSegments() {
        Pair<String, String> result = Slsk.getDirectory("folder\\file.mp3");
        assertEquals("folder", result.getLeft());
        assertEquals("file.mp3", result.getRight());
    }
}
