package jamuz.utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogTextTest {

    @Test
    void shouldCreateWriteAndCloseLogFile() throws Exception {
        Path dir = Files.createTempDirectory("logtext");
        LogText log = new LogText(dir.toString());

        assertTrue(log.createFile("a.log"));
        log.add("line1");
        log.close();

        String text = Files.readString(dir.resolve("a.log"), StandardCharsets.UTF_8);
        assertTrue(text.contains("line1"));
    }
}
