package jamuz.gui.swing;

import javax.swing.table.TableColumn;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableColumnModelBehaviorTest {
    @Test
    void shouldAddHideAndShowColumns() {
        TableColumnModel model = new TableColumnModel();
        TableColumn column = new TableColumn(0);
        column.setIdentifier("id");
        model.addColumn(column);
        assertEquals(1, model.getColumnCount());
        model.setColumnVisible(column, false);
        assertEquals(0, model.getColumnCount());
        model.setColumnVisible(column, true);
        assertEquals(1, model.getColumnCount());
    }
}
