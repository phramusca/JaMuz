package jamuz.gui.swing;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableCellListenerTest {
    @Test
    void shouldCreateListener() {
        JTable table = new JTable(1, 1);
        TableCellListener listener = new TableCellListener(table, new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {}
        });
        assertSame(table, listener.getTable());
    }
}
