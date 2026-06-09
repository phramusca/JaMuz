package jamuz.acoustid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AcoustIDTest {

    @Test
    void shouldThrowWhenFpcalcCommandMissing() throws Exception {
        File f = Files.createTempFile("acoustid", ".mp3").toFile();
        assertThrows(IOException.class, () -> AcoustID.chromaprint(f, "command-that-does-not-exist"));
    }
}
