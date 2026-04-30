package jamuz.process.check;

import jamuz.gui.swing.ProgressBar;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReleaseMBTest {

    @Test
    void constructor_createsInstance() {
        assertNotNull(new ReleaseMB(new ProgressBar()));
    }

    @Test
    void getCoverList_beforeSearch_isNull() {
        ReleaseMB r = new ReleaseMB(new ProgressBar());
        assertNull(r.getCoverList(),
                "getCoverList should return null before any search is performed");
    }

}
