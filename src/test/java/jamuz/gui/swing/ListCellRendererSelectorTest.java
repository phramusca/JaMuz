package jamuz.gui.swing;

import javax.swing.JList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ListCellRendererSelectorTest {

    @Test
    void shouldRenderTextForCategoryElements() {
        ListCellRendererSelector renderer = new ListCellRendererSelector();
        ListElement element = new ListElement("artist", "artist");

        renderer.getListCellRendererComponent(new JList<>(), element, 0, false, false);
        assertTrue(renderer.getText().contains("Artiste") || renderer.getText().contains("artist"));
    }
}
