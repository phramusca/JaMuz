package jamuz.gui.swing;

import javax.swing.ImageIcon;
import javax.swing.JList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ComboBoxRendererTest {

    @Test
    void shouldSelectIconFromIndexValue() {
        ImageIcon[] icons = new ImageIcon[]{new ImageIcon(), new ImageIcon()};
        ComboBoxRenderer renderer = new ComboBoxRenderer(icons);

        renderer.getListCellRendererComponent(new JList<>(), "1", 0, true, false);
        assertSame(icons[1], renderer.getIcon());
    }
}
