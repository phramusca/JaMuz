package jamuz.gui.swing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableModelGenericTest {

    private static final class DummyModel extends TableModelGeneric {
        @Override
        public int getRowCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return "v";
        }

        void configureColumns(String[] cols, Integer[] editable) {
            this.editableColumns = editable;
            setColumnNames(cols);
        }
    }

    @Test
    void shouldExposeColumnsAndEditableFlags() {
        DummyModel model = new DummyModel();
        model.configureColumns(new String[]{"A", "B"}, new Integer[]{1});

        assertEquals(2, model.getColumnCount());
        assertEquals("A", model.getColumnName(0));
        assertFalse(model.isCellEditable(0, 0));
        assertTrue(model.isCellEditable(0, 1));
    }
}
