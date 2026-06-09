package jamuz.process.video;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableModelVideoTest {
    @Test
    void shouldCreateVideoTableModel() {
        TableModelVideo model = new TableModelVideo();
        assertNotNull(model);
        assertTrue(model.getColumnCount() > 0);
    }
}
