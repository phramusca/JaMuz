package jamuz.gui.swing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProgressBarTest {

    @Test
    void shouldUpdateProgressAndStrings() {
        ProgressBar bar = new ProgressBar();
        bar.setup(10);
        bar.progress("hello");

        assertEquals(1, bar.getValue());
        assertTrue(bar.getString().contains("1/10"));

        bar.setupAsPercentage();
        bar.progress("ok", 50);
        assertTrue(bar.getString().contains("50%"));
    }
}
