package jamuz.utils;

import java.awt.GraphicsEnvironment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class ClipboardTextTest {

    @Test
    void setThenGet_returnsOriginalString() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Skipped: no clipboard in headless mode");
        String text = "the beautiful text I'm sending, wouah !";
        ClipboardText instance = new ClipboardText();
        instance.setClipboardContents(text);
        assertEquals(text, instance.getClipboardContents());
    }
}
