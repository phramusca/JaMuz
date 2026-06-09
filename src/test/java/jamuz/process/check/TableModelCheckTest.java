package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableModelCheckTest {
    @Test
    void shouldStartEmptyAndExposeColumnCount() {
        TableModelCheck model = new TableModelCheck();
        assertEquals(0, model.getRowCount());
        assertTrue(model.getColumnCount() > 0);
    }
}
