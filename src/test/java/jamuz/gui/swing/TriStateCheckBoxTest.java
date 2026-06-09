package jamuz.gui.swing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TriStateCheckBoxTest {
    @Test
    void shouldExposeStateTransitions() {
        TriStateCheckBox box = new TriStateCheckBox();
        box.setState(TriStateCheckBox.State.SELECTED);
        assertEquals(TriStateCheckBox.State.SELECTED, box.getState());
        box.setSelected(false);
        assertEquals(TriStateCheckBox.State.UNSELECTED, box.getState());
    }
}
