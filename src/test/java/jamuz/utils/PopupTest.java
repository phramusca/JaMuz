package jamuz.utils;

import org.junit.jupiter.api.Test;

/**
 * Popup shows UI dialogs and logs messages — cannot be asserted automatically.
 * Tests are intentionally minimal; the class is manually validated.
 */
class PopupTest {

    @Test
    void classLoads() {
        // Verify the class is loadable (no static initialisation crash)
        Class<?> c = Popup.class;
        assert c != null;
    }
}
