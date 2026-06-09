package jamuz.gui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SchemaUpgradeProgressTest {

    @Test
    void shouldAllowShowMessageAndDisposeWithoutCrash() {
        SchemaUpgradeProgress progress = SchemaUpgradeProgress.showOnEdt();
        assertNotNull(progress);
        progress.setMessage("upgrading");
        progress.dispose();
    }
}
