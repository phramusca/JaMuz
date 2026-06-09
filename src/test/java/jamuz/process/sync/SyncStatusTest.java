package jamuz.process.sync;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SyncStatusTest {

    @Test
    void shouldContainExpectedEnumValues() {
        assertEquals(SyncStatus.NEW, SyncStatus.valueOf("NEW"));
        assertEquals(SyncStatus.INFO, SyncStatus.valueOf("INFO"));
        assertEquals(2, SyncStatus.values().length);
    }
}
