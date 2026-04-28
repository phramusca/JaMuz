package jamuz.process.video;

import javax.swing.JTable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ButtonOpenVideoTest {

    @Test
    void shouldReturnEditedValueWithoutOpeningWhenStopped() {
        ButtonOpenVideo editor = new ButtonOpenVideo();
        editor.getTableCellEditorComponent(new JTable(), "/tmp", true, 0, 0);
        editor.stopCellEditing();

        assertEquals("/tmp", editor.getCellEditorValue());
    }
}
