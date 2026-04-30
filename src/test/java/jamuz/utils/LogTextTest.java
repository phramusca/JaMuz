package jamuz.utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogTextTest {

    @Test
    void createFile_thenAdd_thenClose_writesAllLines() throws Exception {
        Path dir = Files.createTempDirectory("logtext");
        LogText log = new LogText(dir.toString());

        assertTrue(log.createFile("a.log"));
        log.add("line1");
        log.add("line2");
        log.close();

        String text = Files.readString(dir.resolve("a.log"), StandardCharsets.UTF_8);
        assertTrue(text.contains("line1"));
        assertTrue(text.contains("line2"));
    }

    @Test
    void close_calledTwice_doesNotThrow() throws Exception {
        Path dir = Files.createTempDirectory("logtext-close2");
        LogText log = new LogText(dir.toString());
        assertTrue(log.createFile("b.log"));
        log.close();
        assertDoesNotThrow(log::close);
    }

    @Test
    void createFile_withSubdirectoryPath_createsFile() throws Exception {
        Path dir = Files.createTempDirectory("logtext-sub");
        LogText log = new LogText(dir.toString());
        assertTrue(log.createFile("sub.log"));
        assertTrue(Files.exists(dir.resolve("sub.log")));
    }
}
