package jamuz.utils;

import java.awt.GraphicsEnvironment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * ClipboardImage reads the system clipboard. In headless CI environments
 * there is no clipboard, so the test is skipped automatically.
 */
class ClipboardImageTest {

    @Test
    void getImageFromClipboard_doesNotThrow() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Skipped: no display/clipboard in headless mode");
        assertDoesNotThrow(ClipboardImage::getImageFromClipboard);
    }
}
