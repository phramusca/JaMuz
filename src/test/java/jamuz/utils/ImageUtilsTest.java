package jamuz.utils;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ImageUtilsTest {
    @Test
    void shouldExposeEmptyCoverAndConvertIcon() {
        BufferedImage cover = ImageUtils.getEmptyCover();
        assertNotNull(cover);
        ImageIcon icon = new ImageIcon(cover);
        assertNotNull(ImageUtils.toBufferedImage(icon));
    }
}
