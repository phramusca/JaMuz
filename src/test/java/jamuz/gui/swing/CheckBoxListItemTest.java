package jamuz.gui.swing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CheckBoxListItemTest {

    @Test
    void shouldStartSelectedAndExposeWrappedObject() {
        CheckBoxListItem item = new CheckBoxListItem("abc");

        assertTrue(item.isSelected());
        assertEquals("abc", item.getObject());
        assertEquals("abc", item.toString());

        item.setSelected(false);
        assertFalse(item.isSelected());
    }
}
