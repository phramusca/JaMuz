package jamuz;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class OptionsTest {

    @TempDir
    Path tempDir;

    @Test
    void setAndSave_thenRead_returnsStoredValue() throws Exception {
        Path file = tempDir.resolve("options.properties");
        Options write = new Options(file.toString());
        write.set("k", "v");
        assertTrue(write.save());

        Options read = new Options(file.toString());
        assertTrue(read.read());
        assertEquals("v", read.get("k"));
    }

    @Test
    void get_missingKey_returnsMissingPlaceholder() throws Exception {
        Path file = tempDir.resolve("opts.properties");
        Options o = new Options(file.toString());
        o.save();
        o.read();
        assertEquals("{Missing}", o.get("nonexistent"));
    }

    @Test
    void get_withDefault_returnsFallback() throws Exception {
        Path file = tempDir.resolve("opts2.properties");
        Options o = new Options(file.toString());
        o.save();
        o.read();
        assertEquals("fallback", o.get("nope", "fallback"));
    }

    @Test
    void save_withNonWritablePath_returnsFalse() {
        Options o = new Options("/proc/unit-test-readonly-path/options.properties");
        assertFalse(o.save());
    }

    @Test
    void read_withMissingFile_returnsFalse() {
        Options o = new Options("/tmp/this-file-does-not-exist-xyz.properties");
        assertFalse(o.read());
    }

    @Test
    void multipleProperties_areAllStoredAndRetrieved() throws Exception {
        Path file = tempDir.resolve("multi.properties");
        Options write = new Options(file.toString());
        write.set("a", "1");
        write.set("b", "2");
        write.set("c", "3");
        write.save();

        Options read = new Options(file.toString());
        read.read();
        assertEquals("1", read.get("a"));
        assertEquals("2", read.get("b"));
        assertEquals("3", read.get("c"));
    }
}
