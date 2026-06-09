package jamuz.utils;

import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * QRCode generation wraps ZXing. We verify basic API contracts without
 * checking pixel-level output.
 */
class QRCodeTest {

    @Test
    void create_withTextAndSize_returnsNonNullImage() {
        BufferedImage img = QRCode.create("https://github.com/phramusca/JaMuz", 200);
        assertNotNull(img, "QRCode.create() should return a non-null BufferedImage");
        assertEquals(200, img.getWidth());
        assertEquals(200, img.getHeight());
    }
}
