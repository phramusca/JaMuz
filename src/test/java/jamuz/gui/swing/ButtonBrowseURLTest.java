package jamuz.gui.swing;

import javax.swing.JTable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ButtonBrowseURLTest {

    @Test
    void shouldReturnEditedValueWithoutOpeningWhenStopped() {
        ButtonBrowseURL editor = new ButtonBrowseURL();
        editor.getTableCellEditorComponent(new JTable(), "https://example.com", true, 0, 0);
        editor.stopCellEditing();

        assertEquals("https://example.com", editor.getCellEditorValue());
    }
}
