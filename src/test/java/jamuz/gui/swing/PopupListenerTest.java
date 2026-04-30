package jamuz.gui.swing;

import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PopupListenerTest {

    @Test
    void shouldIgnoreRegularMouseEvent() {
        JPopupMenu menu = new JPopupMenu();
        PopupListener listener = new PopupListener(menu);
        MouseEvent evt = new MouseEvent(new JLabel("x"), MouseEvent.MOUSE_PRESSED, 0L, 0, 1, 1, 1, false);

        assertDoesNotThrow(() -> listener.mousePressed(evt));
        assertFalse(menu.isVisible());
    }
}
