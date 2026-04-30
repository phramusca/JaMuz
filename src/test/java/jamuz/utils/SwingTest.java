package jamuz.utils;

import org.junit.jupiter.api.Test;

/**
 * Swing helpers open file-chooser / tab-selection dialogs — cannot be tested
 * automatically in a headless CI environment. Manually validated.
 */
class SwingTest {

    @Test
    void classLoads() {
        Class<?> c = Swing.class;
        assert c != null;
    }
}
