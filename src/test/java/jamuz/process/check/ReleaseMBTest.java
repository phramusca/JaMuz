package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReleaseMBTest {
    @Test
    void shouldInstantiateReleaseMB() {
        assertNotNull(new ReleaseMB(new jamuz.gui.swing.ProgressBar()));
    }
}
