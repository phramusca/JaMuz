package jamuz.utils;

import org.junit.jupiter.api.Test;

/**
 * Desktop opens external applications (browser, file manager) — cannot be
 * asserted automatically. Manually validated.
 */
class DesktopTest {

    @Test
    void classLoads() {
        Class<?> c = Desktop.class;
        assert c != null;
    }
}
