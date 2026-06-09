package jamuz.gui.swing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ListModelSelectorTest {

    @Test
    void shouldInstantiateAndExposeEmptyModel() {
        ListModelSelector model = new ListModelSelector();
        assertEquals(0, model.getSize());
    }
}
