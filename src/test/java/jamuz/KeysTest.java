package jamuz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KeysTest {

    @Test
    void save_alwaysReturnsFalse() {
        assertFalse(new Keys("/not-found.properties").save());
    }

    @Test
    void set_isNoOp_keyRemainsUnresolved() {
        Keys keys = new Keys("/not-found.properties");
        keys.set("k", "v");
        assertEquals("{Missing}", keys.get("k"));
    }

    @Test
    void get_withDefault_returnsMissingWhenNotFound() {
        Keys keys = new Keys("/not-found.properties");
        // get(key, default) is from Options; key not present → Missing placeholder
        String result = keys.get("absent.key");
        assertEquals("{Missing}", result);
    }
}
