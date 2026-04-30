package jamuz.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.ImageIcon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ImageUtilsTest {

    @Test
    void getEmptyCover_returns500x500BufferedImage() {
        BufferedImage cover = ImageUtils.getEmptyCover();
        assertNotNull(cover);
        assertEquals(500, cover.getWidth());
        assertEquals(500, cover.getHeight());
    }

    @Test
    void getTestCover_returns400x400BufferedImage() {
        BufferedImage cover = ImageUtils.getTestCover();
        assertNotNull(cover);
        assertEquals(400, cover.getWidth());
        assertEquals(400, cover.getHeight());
    }

    @Test
    void toBufferedImage_fromImageIcon_returnsNonNull() {
        BufferedImage src = ImageUtils.getEmptyCover();
        ImageIcon icon = new ImageIcon(src);
        BufferedImage result = ImageUtils.toBufferedImage(icon);
        assertNotNull(result);
        assertEquals(src.getWidth(), result.getWidth());
        assertEquals(src.getHeight(), result.getHeight());
    }

    @Test
    void scaleImage_preservesAspectRatioForWideImage() {
        BufferedImage wide = new BufferedImage(800, 400, BufferedImage.TYPE_INT_ARGB);
        BufferedImage scaled = ImageUtils.scaleImage(wide, 200, 200);
        assertEquals(200, scaled.getWidth());
        assertEquals(100, scaled.getHeight());
    }

    @Test
    void scaleImage_preservesAspectRatioForTallImage() {
        BufferedImage tall = new BufferedImage(400, 800, BufferedImage.TYPE_INT_ARGB);
        BufferedImage scaled = ImageUtils.scaleImage(tall, 200, 200);
        assertEquals(100, scaled.getWidth());
        assertEquals(200, scaled.getHeight());
    }

    @Test
    void scaleImage_preservesAspectRatioForSquareImage() {
        BufferedImage square = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
        BufferedImage scaled = ImageUtils.scaleImage(square, 100, 100);
        assertEquals(100, scaled.getWidth());
        assertEquals(100, scaled.getHeight());
    }

    @Test
    void shrinkImage_whenSmallerThanMax_returnsOriginal() {
        BufferedImage small = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        assertSame(small, ImageUtils.shrinkImage(small, 100));
    }

    @Test
    void shrinkImage_whenLargerThanMax_returnsScaled() {
        BufferedImage large = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        BufferedImage shrunk = ImageUtils.shrinkImage(large, 100);
        assertNotSame(large, shrunk);
        assertTrue(shrunk.getWidth() <= 100);
        assertTrue(shrunk.getHeight() <= 100);
    }

    @Test
    void shrinkImage_withNull_returnsNull() {
        assertNull(ImageUtils.shrinkImage(null, 100));
    }

    @Test
    void write_thenReadBack_roundTrip() throws IOException {
        File tmp = Files.createTempFile("img", ".jpg").toFile();
        tmp.deleteOnExit();
        ImageIcon icon = new ImageIcon(ImageUtils.getTestCover());
        assertTrue(ImageUtils.write(icon, tmp));
        assertTrue(tmp.length() > 0);
    }
}
