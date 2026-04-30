package jamuz.gui.swing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableColumnModelTest {
    @Test
    void shouldInstantiateColumnModel() {
        assertNotNull(new TableColumnModel());
    }
}
