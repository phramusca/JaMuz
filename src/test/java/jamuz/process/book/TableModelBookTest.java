package jamuz.process.book;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableModelBookTest {
    @Test
    void shouldCreateBookTableModel() {
        TableModelBook model = new TableModelBook();
        assertNotNull(model);
        assertTrue(model.getColumnCount() > 0);
    }
}
