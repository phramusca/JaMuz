package jamuz.database;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SchemaUpgradeLogTest {

    @AfterEach
    void cleanup() {
        SchemaUpgradeLog.closeQuietly();
    }

    @Test
    void init_line_close_writesSessionHeaderAndLines() throws Exception {
        Path dir = Files.createTempDirectory("schema-upgrade-log");
        SchemaUpgradeLog.init(dir.toString());
        SchemaUpgradeLog.line("line-a");
        SchemaUpgradeLog.line("line-b");
        SchemaUpgradeLog.closeQuietly();

        String content = Files.readString(dir.resolve("schema_upgrade.log"), StandardCharsets.UTF_8);
        assertTrue(content.contains("schema upgrade session"));
        assertTrue(content.contains("line-a"));
        assertTrue(content.contains("line-b"));
    }

    @Test
    void init_withNull_doesNotThrow() {
        assertDoesNotThrow(() -> SchemaUpgradeLog.init(null));
    }

    @Test
    void init_withBlankString_doesNotThrow() {
        assertDoesNotThrow(() -> SchemaUpgradeLog.init("   "));
    }

    @Test
    void line_beforeInit_doesNotThrow() {
        SchemaUpgradeLog.closeQuietly();
        assertDoesNotThrow(() -> SchemaUpgradeLog.line("orphan line"));
    }

    @Test
    void closeQuietly_calledTwice_doesNotThrow() {
        assertDoesNotThrow(() -> {
            SchemaUpgradeLog.closeQuietly();
            SchemaUpgradeLog.closeQuietly();
        });
    }

    @Test
    void init_calledTwice_appendsNewSession() throws Exception {
        Path dir = Files.createTempDirectory("schema-upgrade-log-reinit");
        SchemaUpgradeLog.init(dir.toString());
        SchemaUpgradeLog.line("session-1");
        SchemaUpgradeLog.closeQuietly();

        SchemaUpgradeLog.init(dir.toString());
        SchemaUpgradeLog.line("session-2");
        SchemaUpgradeLog.closeQuietly();

        String content = Files.readString(dir.resolve("schema_upgrade.log"), StandardCharsets.UTF_8);
        assertTrue(content.contains("session-1"));
        assertTrue(content.contains("session-2"));
    }
}
