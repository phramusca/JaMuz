package jamuz;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OptionsTest {

    @Test
    void shouldSetSaveAndReadProperties() throws Exception {
        Path file = Files.createTempFile("options", ".properties");
        Options write = new Options(file.toString());
        write.set("k", "v");
        assertTrue(write.save());

        Options read = new Options(file.toString());
        assertTrue(read.read());
        assertEquals("v", read.get("k"));
        assertEquals("d", read.get("missing", "d"));
    }
}
