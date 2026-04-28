package jamuz.gui.swing;

import javax.swing.JScrollPane;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableHorizontalTest {

    @Test
    void shouldTrackViewportWidthWhenAutoResizeEnabled() {
        TableHorizontal table = new TableHorizontal();
        table.setAutoResizeMode(TableHorizontal.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        JScrollPane pane = new JScrollPane(table);
        pane.setSize(800, 300);
        pane.getViewport().setSize(800, 300);

        assertTrue(table.getScrollableTracksViewportWidth());
        assertNotNull(table.getPreferredSize());
    }
}
