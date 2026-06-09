package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableModelReplaceTest {
    @Test
    void shouldStartEmptyAndExposeColumns() {
        TableModelReplace model = new TableModelReplace();
        assertEquals(0, model.getRowCount());
        assertTrue(model.getColumnCount() > 0);
    }
}
