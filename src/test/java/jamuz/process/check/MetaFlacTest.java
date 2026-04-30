package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MetaFlacTest {

    @Test
    void constructor_storesPath() {
        MetaFlac metaFlac = new MetaFlac("/tmp");
        assertNotNull(metaFlac);
    }

    @Test
    void process_withNonExistentPath_returnsFalse() {
        MetaFlac metaFlac = new MetaFlac("/non-existent-xyz");
        assertFalse(metaFlac.process(), "Should return false for a non-existent path");
    }
}
