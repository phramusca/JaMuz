package jamuz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KeysTest {

    @Test
    void shouldRejectSaveAndIgnoreSetOperations() {
        Keys keys = new Keys("/not-found.properties");
        assertFalse(keys.save());

        keys.set("k", "v");
        assertEquals("{Missing}", keys.get("k"));
    }
}
