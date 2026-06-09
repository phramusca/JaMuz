package jamuz.gui.swing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableModelTest {
    @Test
    void shouldSupportBasicTableOperations() {
        TableModel model = new TableModel();
        model.setModel(new String[]{"A"}, new Object[][]{{"x"}});
        assertEquals(1, model.getRowCount());
        model.addRow(new Object[]{"y"});
        assertEquals(2, model.getRowCount());
        model.removeRow(0);
        assertEquals(1, model.getRowCount());
    }
}
