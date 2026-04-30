package jamuz.gui.swing;

import java.awt.Dimension;
import javax.swing.JPanel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WrapLayoutTest {
    @Test
    void shouldComputeLayoutSizes() {
        JPanel panel = new JPanel(new WrapLayout());
        panel.setSize(200, 100);
        Dimension pref = panel.getLayout().preferredLayoutSize(panel);
        assertNotNull(pref);
    }
}
