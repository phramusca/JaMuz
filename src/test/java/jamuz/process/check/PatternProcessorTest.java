package jamuz.process.check;

import java.io.File;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PatternProcessorTest {

    private static final String SEP = File.separator;

    @Test
    void getMap_extractsArtistAndAlbumFromPattern() {
        // Pattern has 2 separators, path has 3 → leading "root" segment is stripped
        String path    = "root" + SEP + "Artist" + SEP + "Album" + SEP + "01 Title.mp3";
        String pattern = "%A" + SEP + "%B" + SEP + "%T.mp3";

        Map<String, String> out = PatternProcessor.getMap(path, pattern);

        assertEquals("Artist", out.get("%A"));
        assertEquals("Album",  out.get("%B"));
    }

    @Test
    void getMap_withExactDepth_extractsAllMiddleTokens() {
        // Path depth matches pattern depth exactly
        String path    = "Artist" + SEP + "Album" + SEP + "Track.mp3";
        String pattern = "%A" + SEP + "%B" + SEP + "%T.mp3";

        Map<String, String> out = PatternProcessor.getMap(path, pattern);

        assertEquals("Artist", out.get("%A"));
        assertEquals("Album",  out.get("%B"));
    }

    @Test
    void getMap_returnsEmptyMap_whenPatternDoesNotMatch() {
        // Separator is not present in value → early return
        String path    = "NoSeparatorHere";
        String pattern = "%A" + SEP + "%B";

        Map<String, String> out = PatternProcessor.getMap(path, pattern);

        assertTrue(out.isEmpty() || !out.containsKey("%B"),
                "Should not extract %B when separator is missing");
    }

    @Test
    void getMap_withSingleCapture_extractsValue() {
        String path    = "prefix" + SEP + "TheArtist" + SEP + "rest";
        String pattern = "%A" + SEP + "rest";

        Map<String, String> out = PatternProcessor.getMap(path, pattern);

        assertEquals("TheArtist", out.get("%A"));
    }
}
