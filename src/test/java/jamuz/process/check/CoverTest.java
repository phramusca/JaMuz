package jamuz.process.check;

import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoverTest {

    @Test
    void coverTypeEnum_hasMbTagFileUrlValues() {
        Cover.CoverType[] values = Cover.CoverType.values();
        assertTrue(values.length >= 4);
        // Spot-check expected enum constants
        assertNotNull(Cover.CoverType.MB);
        assertNotNull(Cover.CoverType.TAG);
        assertNotNull(Cover.CoverType.FILE);
        assertNotNull(Cover.CoverType.URL);
    }

    @Test
    void constructor_exposesTypeValueAndName() {
        Cover cover = new Cover(Cover.CoverType.FILE, "/path/img.png", "My Cover");
        assertEquals(Cover.CoverType.FILE, cover.getType());
        assertEquals("/path/img.png", cover.getValue());
        assertEquals("My Cover", cover.getName());
    }

    @Test
    void setAndGetImage_roundTrip() {
        Cover cover = new Cover(Cover.CoverType.TAG, "", "cover");
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        cover.setImage(img);
        assertSame(img, cover.getImage());
    }

    @Test
    void equals_withSameName_returnsTrue() {
        Cover a = new Cover(Cover.CoverType.FILE, "/a.png", "same");
        Cover b = new Cover(Cover.CoverType.MB,   "/b.png", "same");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_withDifferentName_returnsFalse() {
        Cover a = new Cover(Cover.CoverType.FILE, "/a.png", "alpha");
        Cover b = new Cover(Cover.CoverType.FILE, "/b.png", "beta");
        assertNotEquals(a, b);
    }

    @Test
    void compareTo_withNullImage_isLarger() {
        // Cover without image → compareTo returns 1 (placed last)
        Cover noImage   = new Cover(Cover.CoverType.FILE, "", "c");
        Cover withImage = new Cover(Cover.CoverType.FILE, "", "c");
        withImage.setImage(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB));
        assertTrue(noImage.compareTo(withImage) > 0, "Cover without image should sort after one with image");
    }

    @Test
    void compareTo_otherHasNullImage_isSmaller() {
        Cover withImage = new Cover(Cover.CoverType.FILE, "", "c");
        withImage.setImage(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB));
        Cover noImage = new Cover(Cover.CoverType.FILE, "", "c");
        assertTrue(withImage.compareTo(noImage) < 0, "Cover with image should sort before one without");
    }
}
