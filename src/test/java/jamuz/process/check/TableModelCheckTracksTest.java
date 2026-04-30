package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableModelCheckTracksTest {
    @Test
    void shouldCreateTrackTableModel() {
        TableModelCheckTracks model = new TableModelCheckTracks();
        assertNotNull(model);
        assertTrue(model.getColumnCount() > 0);
    }
}
