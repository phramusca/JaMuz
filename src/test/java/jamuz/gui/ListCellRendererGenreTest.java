package jamuz.gui;

import javax.swing.JList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ListCellRendererGenreTest {

    @Test
    void shouldRenderDefaultGenreTextForPercentToken() {
        ListCellRendererGenre renderer = new ListCellRendererGenre();
        renderer.getListCellRendererComponent(new JList<>(), "%", 0, false, false);
        assertNotNull(renderer.getText());
    }
}
