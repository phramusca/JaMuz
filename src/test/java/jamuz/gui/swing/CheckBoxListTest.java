package jamuz.gui.swing;

import javax.swing.DefaultListModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CheckBoxListTest {

    @Test
    void shouldUseDefaultModelAndSingleSelection() {
        CheckBoxList list = new CheckBoxList();
        assertTrue(list.getModel() instanceof DefaultListModel);
        assertEquals(javax.swing.ListSelectionModel.SINGLE_SELECTION, list.getSelectionMode());
    }
}
