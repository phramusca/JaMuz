package jamuz.database;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SchemaUpgradeLogTest {

    @Test
    void shouldWriteSessionHeaderAndLines() throws Exception {
        Path dir = Files.createTempDirectory("schema-upgrade-log");
        try {
            SchemaUpgradeLog.init(dir.toString());
            SchemaUpgradeLog.line("line-a");
            SchemaUpgradeLog.closeQuietly();

            Path file = dir.resolve("schema_upgrade.log");
            assertTrue(Files.exists(file));
            String content = Files.readString(file, StandardCharsets.UTF_8);
            assertTrue(content.contains("schema upgrade session"));
            assertTrue(content.contains("line-a"));
        } finally {
            SchemaUpgradeLog.closeQuietly();
        }
    }
}
