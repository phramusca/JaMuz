package jamuz.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringManagerTest {
    @Test
    void shouldProvideBasicStringHelpers() {
        assertEquals("ab", StringManager.Left("abcd", 2));
        assertEquals("cd", StringManager.Right("abcd", 2));
        assertNotNull(StringManager.removeIllegal("a:b"));
        assertEquals("null", StringManager.getNullableText(null));
    }
}
