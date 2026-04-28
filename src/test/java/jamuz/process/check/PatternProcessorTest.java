package jamuz.process.check;

import java.io.File;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PatternProcessorTest {

    @Test
    void shouldExtractPatternVariablesFromPath() {
        String sep = File.separator;
        String path = "a" + sep + "Artist" + sep + "Album" + sep + "01 Title.mp3";
        String pattern = "%A" + sep + "%B" + sep + "%T";

        Map<String, String> out = PatternProcessor.getMap(path, pattern);
        assertEquals("Artist", out.get("%A"));
        assertEquals("Album", out.get("%B"));
        assertTrue(out.isEmpty() || out.containsKey("%A"));
    }
}
