package jamuz.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Inter is a thin i18n wrapper; visually validated on each run.
 * We verify that the API returns non-null, non-empty strings for known keys.
 */
class InterTest {

    @Test
    void get_withKnownKey_returnsNonNull() {
        String value = Inter.get("Button.Cancel");
        assertNotNull(value);
        assertFalse(value.isBlank());
    }
}
